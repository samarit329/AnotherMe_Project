package another.me.com.segway.remote.phone.fragment;


import android.content.Intent;
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
import another.me.com.segway.remote.phone.SecondActivity;
import another.me.com.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import another.me.com.segway.remote.phone.service.ByteMessageReceiver;
import another.me.com.segway.remote.phone.util.CommandStringFactory;
import another.me.com.segway.remote.phone.util.MovementListenerFactory;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class HeadControl extends JoyStickControllerFragment implements ByteMessageReceiver{

    private static final String TAG = "VisionFragment";

    private ImageView imageView;
    private JoystickView joyHeadPitch;
    private JoystickView joyHeadYaw;
    Button stopH;
    Button startH;
    Button recordvidH;
    Button stopvidH;
    Button call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View layout = inflater.inflate(R.layout.head_control, container, false);

        imageView = layout.findViewById(R.id.image_stream_h);


        joyHeadPitch = layout.findViewById(R.id.joystick_head_pitch);
        joyHeadYaw = layout.findViewById(R.id.joystick_head_yaw);


        joyHeadPitch.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_PITCH));
        joyHeadYaw.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_YAW));


        joyHeadPitch.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_PITCH));
        joyHeadYaw.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_YAW));



        // Stop stream button
        stopH=layout.findViewById(R.id.stopH);
        stopH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopStreamH();
            }
        });//end onClickListener


        //Start stream button
        startH=layout.findViewById(R.id.startH);
        startH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startStreamH();
            }
        });//end onClickListener


        recordvidH=layout.findViewById(R.id.startRecordH);
        recordvidH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startRrcord();
                //recordvid.setText("Stop");
            }
        });//end Listener
        //end onClickListener



        stopvidH=layout.findViewById(R.id.stopRecordH);
        stopvidH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopRecord();
                //recordvid.setText("Stop");
            }
        });//end Listener
        //end onClickListener




        call = layout.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });



        //send start stream video when enter the page
       Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);

        return layout;
    }




    public void openNewActivity(){
        Intent intent = new Intent(getContext(), SecondActivity.class);
        startActivity(intent);
    }



    //stop strem
    @Override
   public void onPause() {
       super.onPause();
       Log.d(TAG, "sending vision stop");
      getLoomoService().unregisterByteMessageReceiver(this);
      String[] messageh = {"vision", "stop"};
      getLoomoService().send(CommandStringFactory.getStringMessage(messageh));
    }


    // stop stream method for the stop button
    public void stopStreamH(){

        Log.d(TAG, "sending vision stop");
        getLoomoService().unregisterByteMessageReceiver(this);
        String[] message = {"vision", "end"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    // start stream method for the start button
    public void startStreamH() {
        Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);
    }

    public void startRrcord() {
        stopvidH.setAlpha(1);
        recordvidH.setAlpha(0);
        Log.d(TAG, "sending record start");
        String[] message = {"recordStart"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    public void stopRecord() {
        stopvidH.setAlpha(0);
        recordvidH.setAlpha(1);
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


}//end class

