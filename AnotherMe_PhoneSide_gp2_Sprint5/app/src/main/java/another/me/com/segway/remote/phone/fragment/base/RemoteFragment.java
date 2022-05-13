package another.me.com.segway.remote.phone.fragment.base;

import android.app.Fragment;

import another.me.com.segway.remote.phone.service.ConnectionService;


// This class for implement the methods in the interface, this class will use by NavigationActivity class.

public abstract class RemoteFragment extends Fragment implements RemoteFragmentInterface {

    protected int fragmentId;
    protected String fragmentTitle;

    public ConnectionService getLoomoService() {
        return ConnectionService.getInstance();
    }

    public String getTitle() {
        return fragmentTitle;
    }

    public void setTitle(String title) {
        fragmentTitle = title;
    }

    public int getFragmendId() {
        return fragmentId;
    }

    public boolean isFragmentId(int id) {
        return id == fragmentId;
    }
}
