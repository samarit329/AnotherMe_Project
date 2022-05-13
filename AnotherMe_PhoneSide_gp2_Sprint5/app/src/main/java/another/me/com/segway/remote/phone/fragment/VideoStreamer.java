package another.me.com.segway.remote.phone.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import another.me.com.segway.remote.phone.MainActivity;
import another.me.com.segway.remote.phone.R;
import another.me.com.segway.remote.phone.SecondActivity;
import another.me.com.segway.remote.phone.fragment.base.RemoteFragment;
import another.me.com.segway.remote.phone.service.ByteMessageReceiver;
import another.me.com.segway.remote.phone.util.CommandStringFactory;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import static android.content.Context.CONNECTIVITY_SERVICE;


// Video streaming functionality class

public class VideoStreamer extends RemoteFragment implements ByteMessageReceiver  {

    private static final String TAG = "VisionFragment";
    private ImageView imageView;
    Button stop;
    Button start;
    Button recordvid;
    Button stopvid;
    Button call;

//SecondActivity vcall;
//MainActivity  mainA ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            View layout = inflater.inflate(R.layout.fragment_videostreamer, container, false);

            imageView = layout.findViewById(R.id.image_stream);






            stop = layout.findViewById(R.id.stopS);

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopStream();
                }
            });//end Listener

            start = layout.findViewById(R.id.startS);
           // start.setAlpha(0);
            start.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startStream();
                }
            });//end Listener


            recordvid = layout.findViewById(R.id.startRecordS);
            recordvid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startRrcord();
                    //recordvid.setText("Stop");
                }
            });//end Listener
            //end onClickListener


            stopvid = layout.findViewById(R.id.stopRecordS);
            stopvid.setVisibility(View.INVISIBLE);
            recordvid.setVisibility(View.VISIBLE);
            //stopvid.setAlpha(0);
            stopvid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopRecord();
                    //recordvid.setText("Stop");
                }
            });//end Listener


            call = layout.findViewById(R.id.callS);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNewActivity();
                }
            });


            //isNetworkAvailable();


            // Start streaming
            Log.d(TAG, "sending vision start"); //DEBUG log message.
            String[] message = {"vision", "start"};// create start video stream message to send it to the robot
            getLoomoService().send(CommandStringFactory.getStringMessage(message));//Send the message to the robot
            getLoomoService().registerByteMessageReceiver(this);//register to the incoming byte message(video)


        return layout;
    } // end onCreateView




    public void openNewActivity(){
        Intent intent = new Intent(getContext(), SecondActivity.class);
        startActivity(intent);
    }



    // Stop streaming
    @Override
    public void onPause() {
       // Toast.makeText(getContext(), " There is NO wifi connection video stream", Toast.LENGTH_LONG).show();
        super.onPause();
       // isNetworkAvailable();
        Log.d(TAG, "sending vision stop");
        getLoomoService().unregisterByteMessageReceiver(this);

        String[] message = {"vision", "stop"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


   // stop stream
    public void stopStream(){

        stop.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
        Log.d(TAG, "sending vision stop");//DEBUG log message.
        getLoomoService().unregisterByteMessageReceiver(this);//unRegister to the incoming byte message(video)
        String[] message = {"vision", "end"};// create stop video stream message to send it to the robot
        getLoomoService().send(CommandStringFactory.getStringMessage(message));//Send the message to the robot

    }//end stop stream
    
    
     // start stream
    public void startStream() {
        stop.setVisibility(View.VISIBLE);
        start.setVisibility(View.INVISIBLE);
        Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);
    }


    //start record 
    public void startRrcord() {
        stopvid.setVisibility(View.VISIBLE);
        recordvid.setVisibility(View.INVISIBLE);
        Log.d(TAG, "sending record start");
        String[] message = {"recordStart"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    public void stopRecord() {
        stopvid.setVisibility(View.INVISIBLE);
        recordvid.setVisibility(View.VISIBLE);
        Log.d(TAG, "sending record stop");
        String[] message = {"recordStop"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }

    // Handle the received image and display it
  @Override
    public void handleByteMessage(final byte[] message) {
      getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
              Bitmap bitmap = BitmapFactory.decodeByteArray(message, 0, message.length);
              imageView.setImageBitmap(bitmap);

          }
      });
  }

    




}// end videoStreamer

