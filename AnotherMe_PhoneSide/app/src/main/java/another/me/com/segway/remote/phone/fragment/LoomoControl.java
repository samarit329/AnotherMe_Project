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
import another.me.com.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import another.me.com.segway.remote.phone.service.ByteMessageReceiver;
import another.me.com.segway.remote.phone.util.CommandStringFactory;
import another.me.com.segway.remote.phone.util.MovementListenerFactory;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class LoomoControl extends JoyStickControllerFragment implements ByteMessageReceiver{

    private static final String TAG = "VisionFragment";

    private ImageView imageView;

    private JoystickView joySpeed;
    private JoystickView joyDirection;
    private JoystickView joyHeadPitch;
    private JoystickView joyHeadYaw;

    Button stopF;
    Button startF;
    Button recordvidF;
    Button stopvidF;
    Button MoveToUI;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {



        View layout = inflater.inflate(R.layout.loomo_control, container, false);
        imageView = layout.findViewById(R.id.image_stream_l);

        joySpeed = layout.findViewById(R.id.stream_joy_speed_l);
        joyDirection = layout.findViewById(R.id.stream_joy_direction_l);

        joyHeadPitch = layout.findViewById(R.id.stream_joy_head_pitch_l);
        joyHeadYaw = layout.findViewById(R.id.stream_joy_head_yaw_l);




        joySpeed.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_SPEED));

        joyDirection.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_DIRECTION));

        joyHeadPitch.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_PITCH));

        joyHeadYaw.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_YAW));

        joySpeed.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_SPEED));
        joyDirection.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_DIRECTION));
        joyHeadPitch.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_PITCH));
        joyHeadYaw.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_YAW));




        // Stop stream button
        stopF=layout.findViewById(R.id.stopF);
        stopF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopStreamF();
            }
        });//end Listener
       //end onClickListener

        //Start stream button
        startF=layout.findViewById(R.id.startF);
        startF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
            startStreamF();

            }
        });//end Listener
        //end onClickListener


        recordvidF=layout.findViewById(R.id.startRecordF);
        recordvidF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startRrcord();
                //recordvid.setText("Stop");
            }
        });//end Listener
        //end onClickListener



        stopvidF=layout.findViewById(R.id.stopRecordF);
        stopvidF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopRecord();
            }
        });//end Listener
        //end onClickListener


        MoveToUI=layout.findViewById(R.id.Move_UI);
        MoveToUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                getFragmentManager().beginTransaction().replace(R.id.content_frame, new UserInteraction()).commit();
                setTitle("User Interaction");
            }
        });//end Listener
        //end onClickListener


        //send start stream video when enter the page
        Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);

        return layout;
    }

    @Override
    //stop stream
    public void onPause() {
        super.onPause();
        Log.d(TAG, "sending vision stop");
        getLoomoService().unregisterByteMessageReceiver(this);
        String[] message = {"vision", "stop"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    // stop stream method for the stop button
    public void stopStreamF(){

        Log.d(TAG, "sending vision stop");
        getLoomoService().unregisterByteMessageReceiver(this);
        String[] message = {"vision", "end"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }
    // start stream method for the start button
    public void startStreamF() {
        Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);
    }



    public void startRrcord() {
        Log.d(TAG, "sending record start");
        String[] message = {"recordStart"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    public void stopRecord() {
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

}
