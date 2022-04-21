package another.me.com.segway.remote.robot.listener;

import android.util.Log;

import another.me.com.segway.remote.robot.service.BaseService;

public class BaseCommand extends MessageCommand {

    private static final String TAG = "RawCommand";

    //Getting the recevied message
    public BaseCommand(String[] message) {
        super(message);
    }

    @Override
    public void execute() {

        Log.i(TAG, "move speed: " + message[1]);
        Log.i(TAG, "move angle: " + message[2]);

        // convert to float so loomo understand it

        float newSpeed = Float.valueOf(message[1]);
        float newAngle = Float.valueOf(message[2]);
        // send angle and speed to move function
        BaseService.getInstance().move(newSpeed,newAngle);
    }
}