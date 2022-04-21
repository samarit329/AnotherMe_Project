package another.me.com.segway.remote.robot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;


import java.io.File;
import java.io.IOException;
import java.util.List;

import another.me.com.segway.remote.robot.service.BaseService;
import another.me.com.segway.remote.robot.service.ConnectivityService;
import another.me.com.segway.remote.robot.service.EmojiService;
import another.me.com.segway.remote.robot.service.HeadService;
import another.me.com.segway.remote.robot.service.StreamVideoService;
import another.me.com.segway.remote.robot.service.TextToSpeechService;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private TextView displayIP;
    Button button;
    Button viewr;



    private StreamVideoService streamVideoService;
    private ConnectivityService connectivityService;
    private HeadService headService;
    private BaseService baseService;
    private TextToSpeechService textToSpeechService;
    private EmojiService emojiService;

    private static final int MEDIA_RECORDER_REQUEST = 0;

    private Camera mCamera;
    public TextureView mPreview;
    private  ViewSwitcher switcher;
    private TextView Tixt1;
    private MediaRecorder mMediaRecorder;
    private File mOutputFile;

    private boolean isRecording = false;
   // private Button captureButton;

    private final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayIP = (TextView) findViewById(R.id.text1);

        //exit button
        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);

            }//end onClick
        });//end ClickListner

        mPreview = (TextureView) findViewById(R.id.surface_view);
        //captureButton = (Button) findViewById(R.id.button_capture);
        switcher =  (ViewSwitcher) findViewById(R.id.view_switcher);
        Tixt1 = (TextView) findViewById(R.id.text1);
        AssigIP();
        ServicesInitlaztion();


        onClickButton();

    }// end onCreate


    // Assign a text to the variable Text that contains the Loomo IP address
    private void AssigIP() {
        String ip = getDeviceIp();

        //If there is no WIFI connection
        if (ip.equalsIgnoreCase("0.0.0.0")) {
            Toast.makeText(getApplication(), "No Wi-Fi Connection", Toast.LENGTH_LONG).show();
        } else {
            //If there is WIFI connection
            displayIP.setText("Hello I am Loomo and here is my IP address: " + ip);
        }
    }






// Get the device (Loomo) IP address
    // use WifiManager for the WiFi connectivity
    private String getDeviceIp() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
        return ip;
    }


    // Create and initialize a new object of the class Connictivity Service and StreamViedo
    private void ServicesInitlaztion() {
        this.connectivityService = new ConnectivityService(this);
        this.streamVideoService = new StreamVideoService(getApplicationContext());
        this.headService = new HeadService(getApplicationContext());
        this.baseService = new BaseService(getApplicationContext());
        this.textToSpeechService = new TextToSpeechService(getApplicationContext());
        this.emojiService = new EmojiService(getApplicationContext(), this);
    }


    // destroy the service when it dose not needed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.connectivityService.disconnect();
        this.streamVideoService.disconnect();
        this.headService.disconnect();
        this.baseService.disconnect();
        this.textToSpeechService.disconnect();
        this.emojiService.disconnect();


    }// end onDestroy

    public void onCaptureClick(View view) {

        if (areCameraPermissionGranted()){
            startCapture();
        } else {
            requestCameraPermissions();
        }
    }

    public void startCapture(){

        if (isRecording) {
            // BEGIN_INCLUDE(stop_release_media_recorder)

            // stop recording and release camera
            try {
                mMediaRecorder.stop();  // stop the recording
            } catch (RuntimeException e) {
                // RuntimeException is thrown when stop() is called immediately after start().
                // In this case the output file is not properly constructed ans should be deleted.
                Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
                //noinspection ResultOfMethodCallIgnored
                mOutputFile.delete();
            }
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            //setCaptureButtonText("Record");
            isRecording = false;
            mPreview.setAlpha(0);
            releaseCamera();
            // END_INCLUDE(stop_release_media_recorder)

        } else {

            // BEGIN_INCLUDE(prepare_start_media_recorder)

            new MediaPrepareTask().execute(null, null, null);

            // END_INCLUDE(prepare_start_media_recorder)

        }
    }

    //private void setCaptureButtonText(String title) {
       // captureButton.setText(title);
   // }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean prepareVideoRecorder(){

        // BEGIN_INCLUDE (configure_preview)
        mCamera = VideoRecorder.getDefaultCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
        Camera.Size optimalSize = VideoRecorder.getOptimalVideoSize(mSupportedVideoSizes,
                mSupportedPreviewSizes, mPreview.getWidth(), mPreview.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        try {
            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }
        // END_INCLUDE (configure_preview)


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);

        // Step 4: Set output file
        mOutputFile = VideoRecorder.getOutputMediaFile(VideoRecorder.MEDIA_TYPE_VIDEO);
        if (mOutputFile == null) {
            return false;
        }
        mMediaRecorder.setOutputFile(mOutputFile.getPath());
        // END_INCLUDE (configure_media_recorder)

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private boolean areCameraPermissionGranted() {

        for (String permission : requiredPermissions){
            if (!(ActivityCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestCameraPermissions(){
        ActivityCompat.requestPermissions(
                this,
                requiredPermissions,
                MEDIA_RECORDER_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (MEDIA_RECORDER_REQUEST != requestCode) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        boolean areAllPermissionsGranted = true;
        for (int result : grantResults){
            if (result != PackageManager.PERMISSION_GRANTED){
                areAllPermissionsGranted = false;
                break;
            }
        }

        if (areAllPermissionsGranted){
            startCapture();
        } else {
            // User denied one or more of the permissions, without these we cannot record
            // Show a toast to inform the user.
            Toast.makeText(getApplicationContext(),
                    getString(R.string.need_camera_permissions),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Asynchronous task for preparing the {@link android.media.MediaRecorder} since it's a long blocking
     * operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
                isRecording = true;
                mPreview.setAlpha(1);


            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (!result) {
                MainActivity.this.finish();
            }
            // inform the user that recording has started
           // setCaptureButtonText("Stop");

        }
    }





    public void startrecord()

    {
        new MediaPrepareTask().execute(null, null, null);
    }





    public void stoprecord(){

          try {
              mMediaRecorder.stop();  // stop the recording
          } catch (RuntimeException e) {
              // RuntimeException is thrown when stop() is called immediately after start().
              // In this case the output file is not properly constructed ans should be deleted.
              Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
              //noinspection ResultOfMethodCallIgnored
              mOutputFile.delete();
          }
          releaseMediaRecorder(); // release the MediaRecorder object
          mCamera.lock();         // take camera access back from MediaRecorder

          // inform the user that recording has stopped

          isRecording = false;
          mPreview.setAlpha(0);
          releaseCamera();
          // END_INCLUDE(stop_release_media_recorder)
       // captureButton.setText("Record");

      }




    public void onClickButton(){

        viewr= (Button) findViewById(R.id.viewr);
        viewr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewRecord.class);
                startActivity(intent);
            }
        });


    }




}// end class
