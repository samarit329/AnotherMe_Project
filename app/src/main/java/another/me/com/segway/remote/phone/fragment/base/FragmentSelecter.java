package another.me.com.segway.remote.phone.fragment.base;

import android.content.Context;

import another.me.com.segway.remote.phone.MainActivity;
import another.me.com.segway.remote.phone.R;
//import another.me.com.segway.remote.phone.VideoChatViewActivity;
import another.me.com.segway.remote.phone.fragment.BodyControl;
import another.me.com.segway.remote.phone.fragment.HeadControl;
import another.me.com.segway.remote.phone.fragment.HomePage;
import another.me.com.segway.remote.phone.fragment.LoomoControl;
import another.me.com.segway.remote.phone.fragment.UserInteraction;
import another.me.com.segway.remote.phone.fragment.EmojiController;
import another.me.com.segway.remote.phone.fragment.VideoStreamer;
//import another.me.com.segway.remote.phone.fragment.TextToSpeech;

// This class for know what the user page selection by take the id from the xml and make new fragment form the selection class.

/*For example, if the user select video stream from the drop down menu this class will take the id (vision)
and create fragment from the selected class (VideoStreamer())*/


public class FragmentSelecter {

    public static RemoteFragmentInterface getFragment(Context context, int id) {
        RemoteFragmentInterface fragment;
        switch (id) {
            case R.id.Home:
                fragment = new HomePage();
                break;

            case R.id.video_stream:
                fragment = new VideoStreamer();
                break;

            case R.id.head_control:
                fragment = new HeadControl();
                break;

            case R.id.body_control:
                fragment = new BodyControl();
                break;

            case R.id.loomo_control:
                fragment = new LoomoControl();
                break;

            case R.id.backConnect:

                fragment= (RemoteFragmentInterface) new MainActivity();
                break;


            case R.id.user_interaction:
                fragment = new UserInteraction();
                break;





            default:
                throw new IllegalArgumentException("Unknown Fragment ID");
        }
        fragment.setTitle(getTitleForFragmentId(context, id));
        return fragment;
    }

    private static String getTitleForFragmentId(Context context, int id) {
        String title;
        switch (id) {
            case R.id.Home:
                title = context.getResources().getString(R.string.navigation_fragment_title_raw);
                break;

            case R.id.video_stream:
                title = context.getResources().getString(R.string.navigation_fragment_title_vision);
                break;

            case R.id.head_control:
                title = context.getResources().getString(R.string.navigation_fragment_title_head);
                break;

            case R.id.body_control:
                title = "Body Control";
                break;


            case R.id.loomo_control:
                title = "Loomo Control";
                break;

            case R.id.backConnect:
                title = "Back To Connect";
                break;


            case R.id.user_interaction:
                title = "User Interaction";
                break;





            default:
                throw new IllegalArgumentException("Unknown Fragment ID");
        }
        return title;
    }
}
