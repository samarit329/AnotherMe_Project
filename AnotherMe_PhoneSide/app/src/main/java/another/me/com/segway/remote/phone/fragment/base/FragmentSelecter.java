package another.me.com.segway.remote.phone.fragment.base;

import android.content.Context;

import another.me.com.segway.remote.phone.R;
import another.me.com.segway.remote.phone.fragment.HomePage;
import another.me.com.segway.remote.phone.fragment.VideoStreamer;

// This class for know what the user page selection by take the id from the xml and make new fragment form the selection class.
//For example , if the user select video stream from the drop down menu this class will take the id for that selection
// and make new object from VideoStreamer() class

public class FragmentSelecter {

    public static RemoteFragmentInterface getFragment(Context context, int id) {
        RemoteFragmentInterface fragment;
        switch (id) {
            case R.id.home_page:
                fragment = new HomePage();
                break;

            case R.id.video_stream:
                fragment = new VideoStreamer();
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
            case R.id.home_page:
                title = context.getResources().getString(R.string.navigation_fragment_title_raw);
                break;

            case R.id.video_stream:
                title = context.getResources().getString(R.string.navigation_fragment_title_vision);
                break;

            default:
                throw new IllegalArgumentException("Unknown Fragment ID");
        }
        return title;
    }
}
