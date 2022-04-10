package another.me.com.segway.remote.robot.service;

import android.util.Log;
import android.widget.Toast;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.baseconnectivity.MessageRouter;
import com.segway.robot.sdk.connectivity.RobotMessageRouter;

import another.me.com.segway.remote.robot.MainActivity;
import another.me.com.segway.remote.robot.listener.MessageListener;



public class ConnectivityService {

    private static final String TAG = "ConnectivityService";

    public static ConnectivityService instance;
    private final MainActivity mainActivity;



    //initalize RobotMessageRouter to register/unregister and bind to/unbind from the connection service in robot
    private RobotMessageRouter mMessageRouter;
    private MessageConnection mMessageConnection;

    public static ConnectivityService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Connection service  not initialized yet");
        }
        return instance;
    }

    public ConnectivityService(MainActivity activity) {
        this.mainActivity = activity;
        initialization();
        instance = this;
    }
// calling initialization to initialize connectivity  service
    public void restartService() {
        initialization();
    }


    private void initialization() {

        //get RobotMessageRouter
        this.mMessageRouter = RobotMessageRouter.getInstance();
        //bind to connectivity service in robot
        this.mMessageRouter.bindService(this.mainActivity, bindStateListener);

    }

    private ServiceBinder.BindStateListener bindStateListener = new ServiceBinder.BindStateListener() {
        @Override
        //Bind to the connection service.
        public void onBind() {
            Log.d(TAG, "onBind called");
            try {

       //register MessageConnectionListener in the RobotMessageRouter
                mMessageRouter.register(messageConnectionListener);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//end onBind

        //Unbind from the connection service.
        @Override
        public void onUnbind(String reason) {
            Log.d(TAG, "onUnBind called: " + reason);
        }
    };


    private MessageRouter.MessageConnectionListener messageConnectionListener = new MessageRouter.MessageConnectionListener() {
        @Override
        public void onConnectionCreated(final MessageConnection connection) {
            Log.d(TAG, "onConnectionCreated: " + connection.getName());
            mMessageConnection = connection;
            try {
                mMessageConnection.setListeners(connectionStateListener, new MessageListener(mainActivity));
            } catch (Exception e) {
                Log.e(TAG, " listener is not initialize", e);
            }
        }
    };

    private MessageConnection.ConnectionStateListener connectionStateListener = new MessageConnection.ConnectionStateListener() {
        @Override
        public void onOpened() {
            Log.d(TAG, "onOpened called: " + mMessageConnection.getName());
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, "connected to: " + mMessageConnection.getName(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public void onClosed(String error) {
            Log.e(TAG, "onClosed called: " + error + ";name=" + mMessageConnection.getName());
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(mainActivity, "disconnected to: " + mMessageConnection.getName(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

// Get the connection message
    public MessageConnection getMessageConnection() {
        return mMessageConnection;
    }


    public void disconnect() {
        //Unbind from the connection service.
        this.mMessageRouter.unbindService();
    }
}// end class ConnectivityService
