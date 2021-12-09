package another.me.com.segway.remote.phone;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import another.me.com.segway.remote.phone.service.ConnectionCallback;
import another.me.com.segway.remote.phone.service.ConnectionService;

public class MainActivity extends AppCompatActivity implements ConnectionCallback {

    private static final String TAG = MainActivity.class.getName();

    private ConnectionService connectionService;
    private EditText ipAddress;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ipAddress = (EditText) findViewById(R.id.ip_input);

    }// end on create


    @Override
    protected void onStart() {

        //bind to ConnectionService
        super.onStart();
        // create Intent from ConnectionService class to be use in the bindService method
        Intent intent = new Intent(this, ConnectionService.class);
        // use flag Context.BIND_AUTO_CREATE to directly bind the service and start the service
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        // When call the finish() method
        super.onDestroy();
        connectionService.unregisterCallback(this);
        unbindService(serviceConnection);
    }


    // Defines callbacks for service binding, that passed to bindService()
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //Called when a connection to the Service has been established, with the IBinder of the communication channel to the Service.
            Log.i(TAG, "Connected to service. Redirecting to Navigation Activity");
            connectionService = ((ConnectionService.LocalBinder) iBinder).getService();
            connectionService.registerCallback(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Called when a connection to the Service has been lost.
            Log.i(TAG, "Disconnected from service");
            connectionService.unregisterCallback(MainActivity.this);
            connectionService = null;
        }
    };

    @Override
    public void onConnected() {
        //implement onConnected(), it called when the service is connected.

        //Define the intent to start Navigation Activity class
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDisconnected() {
        //implement onDisconnected(), it called when the service between the robot and mobile application is disconnected.
        try {
            Toast.makeText(connectionService, "Disconnected from Loomo", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException ignored) {

        }
    }



    public void connectToRobot(View view) {

        if (serviceConnection == null) {
            //if there is no service connection
            Toast.makeText(connectionService, "Service is null", Toast.LENGTH_SHORT).show();
        } else {
            // if there is service connection call connectToRobot method in ConnectionService class and send the IP address that the user enter as parameter
            connectionService.setIpConnection(ipAddress.getText().toString().trim());
        }
    }


    public void skipToController(View view) {
        //This method activate when you click skip button, it just show the application interface without bind to service.
        connectionService.setConnectionSkipped(true);
        //Define the intent to start Navigation Activity class
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }


}//end class