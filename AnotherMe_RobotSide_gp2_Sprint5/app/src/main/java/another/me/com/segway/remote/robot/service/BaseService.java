package another.me.com.segway.remote.robot.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.PoseVLS;
import com.segway.robot.algo.VLSPoseListener;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.algo.minicontroller.ObstacleStateChangedListener;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.locomotion.sbv.StartVLSListener;

public class BaseService {

    private static final String TAG = "BaseService";

    private Base base = null;
    private Context context;
    private Handler timehandler;
    private Runnable lastStop = null;
    private RobotCheckpointListener checkpointListener = null;
    private RobotVLSListener vlsListener = null;

    // create variables for  base postions
    private float lastXPosition = 0f;
    private float lastYPosition = 0f;

    // create  instance of base
    public static BaseService instance;

    public static BaseService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LoomoBaseService instance not initialized yet");
        }
        return instance;
    }

    // create  instance of base
    public BaseService(Context context) {
        timehandler = new Handler();
        this.context = context;
        initBase();
        this.instance = this;
    }

    public void restartService() {
        initBase();
    }




   // this method call in raw command take two parameter and set it to both  linearVelocity,angularVelocity
    public void move(float linearVelocity, float angularVelocity) {
        // call method make sure it is in raw control move
        setRawControlMode();
        // set linearVelocity value
        base.setLinearVelocity(linearVelocity);
        // set angularVelocity value
        base.setAngularVelocity(angularVelocity);
        //chack last stop

        if (lastStop != null) {
            timehandler.removeCallbacks(lastStop);
            Log.d(TAG, "removed callback to stop");
        }
        // if last stop null set it and make both  linearVelocity,angularVelocity zeros
        lastStop = new Runnable() {
            @Override
            public void run() {
                base.setLinearVelocity(0);
                base.setAngularVelocity(0);
            }
        };


        timehandler.postDelayed(lastStop, 1000);
        Log.d(TAG, "added callback to stop");
    }
    // This method check if it in " Visual Localization System 'VLS' " stop it  then set it to  raw control mode

    private void setRawControlMode() {
        if (base.isVLSStarted()) {
            Log.d(TAG, "Stopping VLS");
            base.stopVLS();
        }

        if (base.getControlMode() != Base.CONTROL_MODE_RAW) {
            Log.d(TAG, "Setting control mode to: RAW");
            base.setControlMode(Base.CONTROL_MODE_RAW);
        }
    }



    private void initBase() {
        base = Base.getInstance();

        // bind base service
        base.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            // case of bind
            public void onBind() {
                Log.d(TAG, "Base bind successful");
                base.setControlMode(Base.CONTROL_MODE_NAVIGATION);

                base.setOnCheckPointArrivedListener(new CheckPointStateListener() {
                    @Override
                    // when recived a values of X and Y postion
                    public void onCheckPointArrived(CheckPoint checkPoint, final Pose2D realPose, boolean isLast) {
                        Log.i(TAG, "Position before moving: " + lastXPosition + " / " + lastYPosition);
                        lastXPosition = checkPoint.getX();
                        lastYPosition = checkPoint.getY();
                        Log.i(TAG, "Position after moving: " + lastXPosition + " / " + lastYPosition);
                    }

                    @Override

                    // when the X and Y values  are missing
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {
                        lastXPosition = checkPoint.getX();
                        lastYPosition = checkPoint.getY();
                        Log.i(TAG, "Missed checkpoint: " + lastXPosition + " " + lastYPosition);
                    }
                });

            }

            @Override
            // case of unbind
            public void onUnbind(String reason) {
                Log.d(TAG, "Base bind failed");
            }
        });
    }

    private ObstacleStateChangedListener obstacleStateChangedListener = new ObstacleStateChangedListener() {
        @Override
        public void onObstacleStateChanged(int ObstacleAppearance) {
            Log.i(TAG, "ObstacleStateChanged " + ObstacleAppearance);
        }
    };

    // return the chechpoint of the Loomo
    private class RobotCheckpointListener implements CheckPointStateListener {
        @Override
        public void onCheckPointArrived(CheckPoint checkPoint, final Pose2D realPose, boolean isLast) {
            Log.i(TAG, "Arrived to checkpoint: " + checkPoint);
        }

        @Override
        public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {
            Log.i(TAG, "Missed checkpoint: " + checkPoint);
        }
    }

    //  when open the VLS listener
    private class RobotVLSListener implements StartVLSListener {
        @Override
        public void onOpened() {
            Log.i(TAG, "VLSListener onOpenend");
            base.setNavigationDataSource(Base.NAVIGATION_SOURCE_TYPE_VLS);
        }

        @Override
        // case of error
        public void onError(String errorMessage) {
            Log.i(TAG, "VLSListener error: " + errorMessage);

        }
    }
// when VLS is posed
    private VLSPoseListener vlsPoseListener = new VLSPoseListener() {
        @Override
        public void onVLSPoseUpdate(long timestamp, float pose_x, float pose_y, float pose_theta, float v, float w) {
            Log.d(TAG, "onVLSPoseUpdate() called with: timestamp = [" + timestamp + "], pose_x = [" + pose_x + "], pose_y = [" + pose_y + "], pose_theta = [" + pose_theta + "], v = [" + v + "], w = [" + w + "]");
            Log.d(TAG, "Ultrasonic: " + base.getUltrasonicDistance());
        }
    };

    // calling disconnect()  to stop Base service
    public void disconnect() {
        this.base.unbindService();
    }
}

