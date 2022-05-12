package another.me.com.segway.remote.phone;

import android.app.Fragment;
import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import another.me.com.segway.remote.phone.fragment.EmojiController;
import another.me.com.segway.remote.phone.fragment.HeadControl;
import another.me.com.segway.remote.phone.fragment.HomePage;
import another.me.com.segway.remote.phone.fragment.Logout;
import another.me.com.segway.remote.phone.fragment.VideoStreamer;
import another.me.com.segway.remote.phone.fragment.base.FragmentSelecter;
import another.me.com.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import another.me.com.segway.remote.phone.fragment.base.RemoteFragmentInterface;
import another.me.com.segway.remote.phone.service.ConnectionCallback;
import another.me.com.segway.remote.phone.service.ConnectionService;
import another.me.com.segway.remote.phone.util.CommandStringFactory;

import static java.security.AccessController.getContext;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallback {

Context context;
    private static final String TAG = NavigationActivity.class.getName();

    private Toolbar toolbar = null;
    Logout out = new Logout();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
         isNetworkAvailable();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.navigation_fragment_title_raw);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // set initial fragment if none is set
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
        }


    }

    public  void isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {

        } else {

            Toast.makeText(getApplicationContext(), " There is NO wifi connection ", Toast.LENGTH_LONG).show();

        }
    }


    // handle callbacks
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // when click logout close the app
        if (id == R.id.logout) {
           out.log();
        }

        // set view when choose fragment from navigation menu
        RemoteFragmentInterface fragment = FragmentSelecter.getFragment(this, id);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, (Fragment) fragment).commit();
        toolbar.setTitle(fragment.getTitle());

        //Force hiding Emojis  screen
        String[] message1 = {"settings", EmojiController.KEY_EMOJI, "false"};
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message1));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        // connection to the Service has been established
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "Connected to service. Redirecting to NavigationActivity");

            ConnectionService.setInstance(((ConnectionService.LocalBinder) iBinder).getService());
            ConnectionService.getInstance().registerCallback(NavigationActivity.this);
        }
        // connection to the Service has been lost
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "Disconnected from service");

            ConnectionService.getInstance().unregisterCallback(NavigationActivity.this);
            ConnectionService.setInstance(null);
        }
    };

// disconnecting from loomo
    private void disconnectFromLoomo() {

        isNetworkAvailable();
        ConnectionService.getInstance().disconnectFromLoomo();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

        Log.d(TAG, "onDisconnected called");
        finish();
        //System.exit(0);

        }

    // Clean up the program
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectFromLoomo();
        ConnectionService.getInstance().unregisterCallback(NavigationActivity.this);
        unbindService(serviceConnection);
    }



}//end class
