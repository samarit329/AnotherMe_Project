package another.me.com.segway.remote.phone.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.segway.robot.mobile.sdk.connectivity.MobileMessageRouter;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.Message;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.baseconnectivity.MessageRouter;

import java.util.ArrayList;

import another.me.com.segway.remote.phone.util.CommandStringFactory;

public class ConnectionService extends Service {

    private static final String TAG = ConnectionService.class.getName();

    private final IBinder binder = new LocalBinder();

    // to use methods for the mobile applications register/unregister and bind to/unbind from the connection service in robot.
    private MobileMessageRouter mobileMessageRouter;

    //to use methods that set the message listener and send messages to the specific robot applications.
    private MessageConnection messageConnection;


    private ArrayList<ConnectionCallback> callbackList = new ArrayList<>();
    private ArrayList<ByteMessageReceiver> byteMessageReceivers = new ArrayList<>();

    private static ConnectionService instance;

    private boolean connectionSkipped = false;// to change to true when call setConnectionSkipped method

    public boolean isConnectionSkipped() {

        return connectionSkipped;
    }

    public void setConnectionSkipped(boolean connectionSkipped) {
        // the the passing parameter (connectionSkipped)equal true
        this.connectionSkipped = connectionSkipped;
    }

    public static void setInstance(ConnectionService connectionService) {

        instance = connectionService;
    }

    public static ConnectionService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Connection Service should be already instantiated");
        }

        return instance;
    }

    //Send messages to the specific robot applications(Another Me in the robot side)
    public void send(Message message) {
        if (connectionSkipped) {
            Log.d(TAG, "Connection was skipped");
        } else {
            try {

                //Send messages to the specific robot applications.
                Log.d(TAG, "Sending command with content: " + message.getContent().toString());
                messageConnection.sendMessage(message);

            } catch (Exception e) {
                Log.e(TAG, "message send failed for message: " + message.toString(), e);
            }
        }
    }


    public void registerByteMessageReceiver(ByteMessageReceiver byteMessageReceiver) {
        byteMessageReceivers.add(byteMessageReceiver);
    }

    public void unregisterByteMessageReceiver(ByteMessageReceiver byteMessageReceiver) {
        byteMessageReceivers.remove(byteMessageReceiver);
    }

    public class LocalBinder extends Binder {
        // to bind the Service to the listener
        public ConnectionService getService() {
            return ConnectionService.this;
        }
    }

    public void registerCallback(ConnectionCallback callbackInstance) {
        callbackList.add(callbackInstance);
    }

    public void unregisterCallback(ConnectionCallback callbackInstance) {
        callbackList.remove(callbackInstance);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "ConnectionService onBind called");
        if (mobileMessageRouter == null) {
            mobileMessageRouter = MobileMessageRouter.getInstance();
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "ConnectionService onUnbind called");
        if (callbackList.size() == 0) {
            disconnectFromLoomo();
        }
        return super.onUnbind(intent);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // This method for connect the mobile to the robot by getting the ip address, when the main activity calls this method and pass the ip address.
    public void setIpConnection(String ip) {

        Log.i(TAG, "Connecting to IP: " + ip);//Send an INFO log message.
        Log.d(TAG, "Connect To Robot method ");//Send a DEBUG log message.

        try {
            // Set the connection IP. If the input argument IP is illegal, then throw Exception.
            Log.d(TAG, "set IP address connection");
            mobileMessageRouter.setConnectionIp(ip);

            //bind the connection service in robot.
            Log.d(TAG, "Binding Loomo connection service");
            // (bindService for bind the robot to connection service)(BindStateLister to listen to the bind state of the connection service)
            mobileMessageRouter.bindService(this, bindStateListener);

        } catch (Exception e) {
            Log.e(TAG, "Exception during mobile and robot connection", e);//Send a ERROR log message and log the exception.

        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ServiceBinder.BindStateListener bindStateListener = new ServiceBinder.BindStateListener() {
        @Override
        public void onBind() {
            // Bind to the connection service
            Log.d(TAG, " onBind called");//Send a DEBUG log message.

            try {

                mobileMessageRouter.register(messageConnectionListener);//Register a MessageConnectionListener in the message handler

            } catch (Exception e) {
                Log.e(TAG, "Exception in register the connection Listener");//Send a ERROR log message and log the exception.
            }
        }

        @Override
        public void onUnbind(String reason) {
            // unbind the connection service
            Log.d(TAG, " onUnBind called: " + reason);
        }
    };
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private MessageRouter.MessageConnectionListener messageConnectionListener = new MessageRouter.MessageConnectionListener() {
        @Override

        public void onConnectionCreated(final MessageConnection connection) {

            Log.d(TAG, " onConnectionCreated called");

            Log.i(TAG, " connection created for " + connection.getName());//getName() give the package name of the specific robot application that is connected to

            messageConnection = connection;
            try {
                Log.d(TAG, "Set the message connection listeners");
                // Set the ConnectionStateListener for the connection state and MessageListener for the message state.
                messageConnection.setListeners(connectionStateListener, messageListener);
            } catch (Exception e) {
                Log.e(TAG, "Exception in  message connection listeners");
            }
        }
    };
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private MessageConnection.ConnectionStateListener connectionStateListener = new MessageConnection.ConnectionStateListener() {
        @Override

        public void onOpened() {

            //The connection now is open between mobile and robot
            Log.d(TAG, "onOpened called: " + messageConnection.getName());//getName() give the package name of the specific robot application that is connected to

            // use the for-each loop to iterate through elements of callbackList array
            for (ConnectionCallback callback : callbackList) {
                callback.onConnected();// the callback item call the method onConnected() in StateOfConnection class
            }
        }

        @Override
        public void onClosed(String error) {

            //The connection is close between mobile and robot because of error
            Log.d(TAG, " onClosed called: " + error + ";name=" + messageConnection.getName());

            // use the for-each loop to iterate through elements of callbackList array
            for (ConnectionCallback callback : callbackList) {
                callback.onDisconnected();// the callback item call the method onDisconnected() in StateOfConnection class
            }
        }
    };
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private MessageConnection.MessageListener messageListener = new MessageConnection.MessageListener() {
        @Override
        public void onMessageSentError(Message message, String error) {
            //when message sent error
            Log.d(TAG, " onMessageSentError: " + error + " through message: " + message);
        }

        @Override
        public void onMessageSent(Message message) {
            //when message sent
            Log.d(TAG, " onMessageSent: " + message + " it sent successfully");
        }

        @Override
        public void onMessageReceived(Message message) {
            // when message received
            Log.d(TAG, "onMessageReceived: " + message);

            if (message.getContent() instanceof byte[]) { // if the message received is BufferMessage

                Log.d(TAG, "Byte message received send frame to receivers");
                // use the for-each loop to iterate through elements of byteMessageReceivers array
                for (ByteMessageReceiver receiver : byteMessageReceivers) {
                    receiver.handleByteMessage((byte[]) message.getContent());// the receiver item call the method handleByteMessage() in ByteMessageReceiver class
                }
            } else {
                Log.i(TAG, "Received string message: " + message.getContent());//Send an INFO log message
            }
        }
    };
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void disconnectFromLoomo() {


        Log.d(TAG, "Connection Service disconnect From Loomo called");
        try {
            mobileMessageRouter.unregister();
        } catch (IllegalStateException e) {
            Log.d(TAG, "MessageConnectionListener is not yet registered. Skipping unregistration.");
        }
        try {
            mobileMessageRouter.unbindService();
        } catch (IllegalStateException e) {
            Log.d(TAG, "MobileMessageRouter is not bount. Skipping unbind.");
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Text to speech function, send sound

    public void sendSound(String speak) {
        try {
            Log.i(TAG, "Trying to say: " + speak);
            String[] message = {"speak", speak};
            messageConnection.sendMessage(CommandStringFactory.getStringMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}