package another.me.com.segway.remote.phone.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import another.me.com.segway.remote.phone.R;
import another.me.com.segway.remote.phone.fragment.base.RemoteFragment;
import another.me.com.segway.remote.phone.service.ByteMessageReceiver;
import another.me.com.segway.remote.phone.service.ConnectionService;
import another.me.com.segway.remote.phone.util.CommandStringFactory;

// Video streaming functionality class

public class VideoStreamer extends RemoteFragment implements ByteMessageReceiver  {

    private static final String TAG = "VisionFragment";
    private ImageView imageView;
    Button stop;
    Button start;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View layout = inflater.inflate(R.layout.fragment_videostreamer, container, false);

        imageView = layout.findViewById(R.id.image_stream);

        stop=layout.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopStream();
            }
        });//end Listener


        start=layout.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startStream();
            }
        });//end Listener

    // Start streaming
       Log.d(TAG, "sending vision start"); //DEBUG log message.
        String[] message = {"vision", "start"};// create start video stream message to send it to the robot
        getLoomoService().send(CommandStringFactory.getStringMessage(message));//Send the message to the robot
        getLoomoService().registerByteMessageReceiver(this);//register to the incoming byte message(video)

        return layout;
    } // end onCreateView


    // Stop streaming
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "sending vision stop");
        getLoomoService().unregisterByteMessageReceiver(this);
        String[] message = {"vision", "stop"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


   // stop stream
    public void stopStream(){

        Log.d(TAG, "sending vision stop");//DEBUG log message.
        getLoomoService().unregisterByteMessageReceiver(this);//unRegister to the incoming byte message(video)
        String[] message = {"vision", "end"};// create stop video stream message to send it to the robot
        getLoomoService().send(CommandStringFactory.getStringMessage(message));//Send the message to the robot

    }//end stop stream
    
    
     // start stream
    public void startStream() {
        Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);
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

