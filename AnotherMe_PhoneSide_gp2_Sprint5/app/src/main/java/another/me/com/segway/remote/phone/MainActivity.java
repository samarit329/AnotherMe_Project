package another.me.com.segway.remote.phone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import another.me.com.segway.remote.phone.service.ConnectionCallback;
import another.me.com.segway.remote.phone.service.ConnectionService;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class MainActivity extends AppCompatActivity implements ConnectionCallback {

    private static final String TAG = MainActivity.class.getName();

    private ConnectionService connectionService;
    private EditText ipAddress;


    private static final String LOG_TAG = another.me.com.segway.remote.phone.MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    public RtcEngine mRtcEngine;






    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ipAddress = (EditText) findViewById(R.id.ip_input);
        isNetworkAvailable();
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
            //Toast.makeText(getApplicationContext(), " There is NO WIFI connection ", Toast.LENGTH_LONG).show();

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
            Toast.makeText(getBaseContext(), " There is NO wifi connection main", Toast.LENGTH_LONG).show();
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



    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    public  void isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplication()
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
        } else {

            Toast.makeText(getApplication(), " There is NO WIFI connection ", Toast.LENGTH_LONG).show();
        }
    }


}//end class