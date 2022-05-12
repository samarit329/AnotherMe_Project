package another.me.com.segway.remote.robot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    // initialize the needed variables
    String httpLiveUrl;
    VideoView videoView;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideUI();

        // Create a progressbar
        pDialog = new ProgressDialog(VideoPlayerActivity.this);
        // Set progressbar title
        pDialog.setTitle(getString(R.string.progress_dialog_title));
        // Set progressbar message
        pDialog.setMessage(getString(R.string.progress_dialog_message));
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        setContentView(R.layout.activity_video_player);

        // request the video url
        Intent intent = getIntent();
        httpLiveUrl = intent.getStringExtra("EXTRA_URL");
        videoView = (VideoView) findViewById(R.id.VideoView);
        // set the video url
        videoView.setVideoURI(Uri.parse(httpLiveUrl));
        // call MediaController which is from Vitamio laibraries and set it to video view
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        //start video view
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
                hideUI();
            }
        });
        // when an error occures
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            // Close the progress bar and play the video
            public boolean onError(MediaPlayer mp, int x, int y) {
                pDialog.dismiss();
                return false;
            }
        });

        // when change the foucos listener to another video view
        videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                }
            }
        });


       // create action button to enable going back from video play to videos lists
        FloatingActionButton BackH = (FloatingActionButton) findViewById(R.id.BackH);
        BackH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call finish to end the current page and return back
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu by  adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_video_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  The action bar will automatically handle clicks on the Home/Up button
        // need to specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    // hide the video interface  with all of its details when required
    public void hideUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                    new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                // set immersive mode sticky
                            }
                        }
                    });
        }
    }
}// end class VideoPlayerActivity
