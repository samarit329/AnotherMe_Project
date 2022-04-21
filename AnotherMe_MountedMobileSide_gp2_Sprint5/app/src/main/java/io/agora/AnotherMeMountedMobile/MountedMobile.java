package io.agora.AnotherMeMountedMobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class MountedMobile extends AppCompatActivity {

    private static final String LOG_TAG = MountedMobile.class.getSimpleName();

    // create variables for all permissions
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    // create variable for connection
    private RtcEngine mRtcEngine;

    // create handler when user want to start or stop streaming
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        // when the user left the connection call onRemoteUserLeft
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        // when the user want to mute video call onRemoteUserVideoMuted
        public void onUserMuteVideo(final int uid, final boolean muted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoMuted(uid, muted);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
       // check the permissions on the mainfest for the audio,video, and camera
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
         // call initAgoraEngineAndJoinChannel when the permissions granted
            initAgoraEngineAndJoinChannel();
        }
       // mute the local users audio
        mRtcEngine.muteLocalAudioStream(false);
    }


    // start streaming video and audio
    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        setupVideoProfile();
        joinChannel();
    }

    // check the self permissions
    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        // return true when permissions granted
        return true;
    }

    @Override
    // check the video and audio  permissions
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);
        // check the requestCode
        switch (requestCode) {
            // case the audio permissions are granted
            // audio permission on the mainfest
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    // case there is no permission for audio
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    // end the app
                    finish();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                // case the video permissions are granted
                // audio permission on the mainfest
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // calling initAgoraEngineAndJoinChannel to start streaming
                    initAgoraEngineAndJoinChannel();
                } else {
                    // case there is no permission for audio
                    // end the app
                    showLongToast("No permission for " + Manifest.permission.CAMERA);
                    finish();
                }
                break;
            }
        }
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    // calling onDestroy when user want to end the connection
    protected void onDestroy() {
        super.onDestroy();

        // calling leaveChannel to end streaming
        leaveChannel();
        RtcEngine.destroy();
        // make the connection null
        mRtcEngine = null;
    }


  // calling initializeAgoraEngine to start streaming audio and video
    private void initializeAgoraEngine() {

        try {
            // create connection between the user and other users
            // create app id shared between users to connect to each other
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));

            throw new RuntimeException(" error\n" + Log.getStackTraceString(e));
        }
    }

    // set the user video profile
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }



    private void joinChannel() {
      // calling joinChannel which start the connection by using user id
        mRtcEngine.joinChannel(null, "demoChannel1", "Data", 0);
    }

    // create user interface for video streaimg
    private void setupRemoteVideo(int uid) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);

        if (container.getChildCount() >= 1) {
            return;
        }
       // create surfaceView and put the user id with it
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

        surfaceView.setTag(uid);

    }


    // calling leaveChannel when the user end the connection
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }


    private void onRemoteUserLeft() {

        // remove all views when the user left and end streaming audio and video
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);
        container.removeAllViews();


    }

    // when the user want to mute the video streaming
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);

        // create surface for video muting
        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        // set the Visibility for the video muted to vidible
        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }
}// end class MountedMobile 
