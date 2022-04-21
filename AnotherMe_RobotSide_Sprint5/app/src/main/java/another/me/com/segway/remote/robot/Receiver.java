package another.me.com.segway.remote.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import another.me.com.segway.remote.robot.service.BaseService;
import another.me.com.segway.remote.robot.service.ConnectivityService;
import another.me.com.segway.remote.robot.service.EmojiService;
import another.me.com.segway.remote.robot.service.HeadService;
import another.me.com.segway.remote.robot.service.StreamVideoService;
import another.me.com.segway.remote.robot.service.TextToSpeechService;

public class Receiver extends BroadcastReceiver {

    private static final String TAG = "Receiver";



    // Loomo transforms into a self-balancing vehicle
    private static final String TO_SBV = "com.segway.robot.action.TO_SBV";
    // Loomo transforms into a robot
    private static final String TO_ROBOT = "com.segway.robot.action.TO_ROBOT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received intent:" + intent.getAction());

        switch (intent.getAction()) {
            // stop all services when Loomo in self-balancing mode
            case TO_SBV:
                stopServices();
                break;
            // start all services when Loomo in robot mode
            case TO_ROBOT:
                startServices();
                break;
        }
    }


    // start all Loomo robot services
    private void startServices() {
        Log.i(TAG, " Start Services ");
        ConnectivityService.getInstance().restartService();
        StreamVideoService.getInstance().restartService();
        HeadService.getInstance().restartService();
        BaseService.getInstance().restartService();
        TextToSpeechService.getInstance().restartService();
        EmojiService.getInstance().restartService();

    }
    // stop all Loomo services when loomo in SBV mode
    private void stopServices() {
        Log.i(TAG, "Stop Services");
        ConnectivityService.getInstance().disconnect();
        StreamVideoService.getInstance().disconnect();
        HeadService.getInstance().disconnect();
        BaseService.getInstance().disconnect();
        TextToSpeechService.getInstance().disconnect();
        EmojiService.getInstance().disconnect();
    }
}// end class
