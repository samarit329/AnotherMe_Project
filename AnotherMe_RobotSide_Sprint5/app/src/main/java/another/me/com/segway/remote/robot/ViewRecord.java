package another.me.com.segway.remote.robot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ViewRecord extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VideosFragment.OnFragmentInteractionListener{


    // initialize the required variables
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private int mCurrentSelectedPosition;

    private CharSequence mTitle;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    public static Snackbar permissionsSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // connect to the related interface
        setContentView(R.layout.activity_view_record);

        // check the instance postion
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        } else {
            mCurrentSelectedPosition = R.id.nav_videos;
        }

        // create the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create button to enable back from list of videos to ip address page
        FloatingActionButton BackH = (FloatingActionButton) findViewById(R.id.BackH);
        BackH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call finish to back to the ip address
               finish();
            }
        });

        // create drawer layout which include all the content of ViewRecord interface
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // crate the navigation bar of the ViewRecord page
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // if the selected item videos the diaplay the list of videos
        if(mCurrentSelectedPosition==R.id.nav_videos){
            navigationView.setCheckedItem(R.id.nav_videos);
        }

        loadFragment(mCurrentSelectedPosition);
    }

    @Override
    // request permissions for the videos
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // videos-related task
                    if (mCurrentSelectedPosition == R.id.nav_videos) {
                        loadFragment(mCurrentSelectedPosition);
                    }
                } else {
                    // permission denied
                    // functionality that depends on this permission.
                    if (mCurrentSelectedPosition == R.id.nav_videos) {
                        loadFragment(mCurrentSelectedPosition);
                    }
                }
                return;
            }

        }
    }// end method

    @Override
    // function back which related to the interface using drawer layout
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    // save the current instance and its postion
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    // check the selected item from navigation bar
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mCurrentSelectedPosition = id;
        loadFragment(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // change the title of the page based on the selected item
    public void onSectionAttached(int number) {
        String titles[] = new String[3];
        // if the selected item is videos then display videos
        titles[0] = getString(R.string.nav_local_videos);

        mTitle = titles[number];
    }

    // get the video id then call playVideo to start it
    public void onVideosFragmentInteraction(String id) {
        playVideo(id);
    }



    public void restoreActionBar() {
        ViewRecord.this.setTitle(mTitle);
//        toolbar.setTitle(mTitle);
    }

    // receive the id of the video
    // move the id to class VideoPlayerActivity to open it
    private void playVideo(String id) {
        Intent intent = new Intent(ViewRecord.this,
                VideoPlayerActivity.class);
        intent.putExtra("EXTRA_URL", id);
        startActivity(intent);
    }


    // check the recevied id , if it is for videos then call VideoFragment

    private void loadFragment(int fragmentID) {
        if(ViewRecord.permissionsSnackbar != null){
            ViewRecord.permissionsSnackbar.dismiss();
        }
        Fragment fragment = null;
        switch (fragmentID) {
            case R.id.nav_videos:
                fragment = VideosFragment.newInstance();
                switchFragment(fragment);
                // set the title to videos
                mTitle = getString(R.string.nav_local_videos);
                restoreActionBar();
                break;

            default:
                break;
        }
    }

    // switch fragment based on the request
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }




}// end class ViewRecord