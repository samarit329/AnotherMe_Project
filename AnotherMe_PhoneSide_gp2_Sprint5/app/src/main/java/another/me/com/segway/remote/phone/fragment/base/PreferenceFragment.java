package another.me.com.segway.remote.phone.fragment.base;

import another.me.com.segway.remote.phone.service.ConnectionService;

public abstract class PreferenceFragment extends android.preference.PreferenceFragment implements RemoteFragmentInterface {


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
