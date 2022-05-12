package another.me.com.segway.remote.phone.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class BodyControl extends JoyStickControllerFragment implements ByteMessageReceiver  {


    private static final String TAG = "VisionFragment";

    private ImageView imageView;
    private JoystickView joySpeed;
    private JoystickView joyDirection;
    Button stopB;
    Button startB;
    Button recordvidB;
    Button stopvidB;

    Button call;

    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View layout = inflater.inflate(R.layout.body_control, container, false);
        imageView = layout.findViewById(R.id.image_stream_b);


        joySpeed = layout.findViewById(R.id.stream_joy_speed);
        joyDirection = layout.findViewById(R.id.stream_joy_direction);

        joySpeed.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_SPEED));
        joyDirection.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_DIRECTION));

        joySpeed.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_SPEED));
        joyDirection.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_DIRECTION));



        // Stop stream button
        stopB=layout.findViewById(R.id.stopB);
        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopStreamB();
                startB.setVisibility(View.VISIBLE);
                stopB.setVisibility(View.INVISIBLE);
            }
        });//end onClickListener


        //Start stream button
        startB=layout.findViewById(R.id.startB);
        startB.setVisibility(View.INVISIBLE);
        stopB.setVisibility(View.VISIBLE);
        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startStreamB();
                startB.setVisibility(View.INVISIBLE);
                stopB.setVisibility(View.VISIBLE);
            }
        });//end onClickListener


        recordvidB=layout.findViewById(R.id.startRecordB);
        recordvidB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startRrcord();
                //recordvid.setText("Stop");
            }
        });//end Listener
        //end onClickListener



        stopvidB=layout.findViewById(R.id.stopRecordB);
        //stopvidB.setAlpha(0);
        stopvidB.setVisibility(View.INVISIBLE);
        //recordvidB.setAlpha(1);
        recordvidB.setVisibility(View.VISIBLE);
        stopvidB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopRecord();
                //recordvid.setText("Stop");
            }
        });//end Listener
        //end onClickListener




        call = layout.findViewById(R.id.callB);
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
    public void stopStreamB(){

        Log.d(TAG, "sending vision stop");
        getLoomoService().unregisterByteMessageReceiver(this);
        String[] message = {"vision", "end"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }

    // start stream mehod for the start button
    public void startStreamB() {
        Log.d(TAG, "sending vision start");
        String[] message = {"vision", "start"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
        getLoomoService().registerByteMessageReceiver(this);
    }



    public void startRrcord() {
        //stopvidB.setAlpha(1);
        stopvidB.setVisibility(View.VISIBLE);
        //recordvidB.setAlpha(0);
        recordvidB.setVisibility(View.INVISIBLE);
        Log.d(TAG, "sending record start");
        String[] message = {"recordStart"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    public void stopRecord() {
        //stopvidB.setAlpha(0);
        stopvidB.setVisibility(View.INVISIBLE);
        //recordvidB.setAlpha(1);
        recordvidB.setVisibility(View.VISIBLE);
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
    }//end method
}// end class
