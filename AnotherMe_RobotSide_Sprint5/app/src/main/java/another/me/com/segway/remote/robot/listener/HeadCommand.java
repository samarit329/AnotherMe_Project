package another.me.com.segway.remote.robot.listener;

import android.os.Message;
import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.locomotion.head.Head;

import another.me.com.segway.remote.robot.service.ConnectivityService;
import another.me.com.segway.remote.robot.service.HeadService;
import another.me.com.segway.remote.robot.service.StreamVideoService;


public class HeadCommand extends MessageCommand {

    private static final String TAG = "HeadCommand";

    public HeadCommand(String[] message)

    {
        super(message);

    }

    @Override

    // implement method execute from the super class MessageCommand
    public void execute() {
        // set the values of yaw and pitch from the message
        float pitch = Float.valueOf(message[2]);
        float yaw = Float.valueOf(message[3]);

        Log.i(TAG, "yaw value is : " + yaw);
        Log.i(TAG, "pitch value is: " + pitch);

        // if the Pitch value exceed the supported range from SDK
        if (pitch > (3.15F) || pitch < (-(3.14F / 2))) {
            Log.e(TAG, "Received Pitch value is not supported");
        }

        // if the Yaw value exceed the supported range from SDK
        if (yaw > (3.15F * 0.8) || yaw < (-(3.15F * 0.8))) {
            Log.e(TAG, "Received Yaw value is not supported");
        }

        try {
            // set mode to tracking mode
            int mode = Head.MODE_SMOOTH_TACKING;

            // in case that message contains orientation then set mode to Lock mode
            if (message[1].equalsIgnoreCase("orientation")) {
                mode = Head.MODE_ORIENTATION_LOCK;
            }
            // send the mode , yaw , pitch values
            HeadService.getInstance().move(mode, yaw, pitch);


        } catch (Exception e) {
            Log.e(TAG, "An exception occured in head comman", e);
        }

    }
}// end class Head Command
