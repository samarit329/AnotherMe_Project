package another.me.com.segway.remote.robot.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.connectivity.BufferMessage;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.Frame;
import com.segway.robot.sdk.vision.stream.StreamType;

import java.io.ByteArrayOutputStream;

public class HeadService {

    private static final String TAG = "LoomoHeadService";

    private Head head = null;
    private Context context;

    private Handler timehandler;

    public static HeadService instance;

    // get head class instance
    public static HeadService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Head service is not initialized ");
        }
        return instance;
    }





    public HeadService(Context context) {
        timehandler = new Handler();
        this.context = context;
        initBase();
        this.instance = this;
    }

    public void restartService()
    {
        initBase();
    }

    public void move(int mode, float yawValue, float pitchValue) {
        Log.d(TAG, " world head position: " + this.head.getWorldYaw().getAngle() + ";" + this.head.getWorldPitch().getAngle());

        // check the type of received mode Tracking or Lock mode
        this.head.setMode(mode);

        //if the mode is Tracking mode then means control the head's position
        if (mode == Head.MODE_SMOOTH_TACKING) {
            Log.d(TAG, "change head position to: " + yawValue + " , " + pitchValue);

            // set the received values for Yaw and  Pitch
            this.head.setWorldYaw(yawValue);
            this.head.setWorldPitch(pitchValue);

            //if the mode is Lock mode then means control the head's velocity
        } else if (mode == Head.MODE_ORIENTATION_LOCK) {
            Log.d(TAG, "Accelerating head with: " + yawValue + " , " + pitchValue);

            // set the received values for Yaw and  Pitch
            this.head.setYawAngularVelocity(yawValue);
            this.head.setPitchAngularVelocity(pitchValue);
            // if no of these two modes received then nothing will change
        } else {
            Log.e(TAG, "Unknown mode received: " + mode);
        }
    }


    //initialize the Head instance and bind the head service
    private void initBase() {
        head = Head.getInstance();
        head.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            // on bind head servive
            public void onBind() {
                Log.d(TAG, "Head bind successfully ");
            }

            // on unbind head service
            @Override
            public void onUnbind(String reason) {

                Log.d(TAG, "Head is unbind ");
            }
        });


    }














// in case of disconnect the head service call unbindService
    public void disconnect() {

        this.head.unbindService();
    }
}// end class HeadService
