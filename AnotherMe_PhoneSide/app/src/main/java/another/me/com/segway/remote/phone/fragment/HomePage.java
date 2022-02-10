package another.me.com.segway.remote.phone.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import another.me.com.segway.remote.phone.R;
import another.me.com.segway.remote.phone.fragment.base.RemoteFragmentInterface;
import another.me.com.segway.remote.phone.service.ConnectionService;
import another.me.com.segway.remote.phone.util.CommandStringFactory;

// home page class (second main UI)

public class HomePage extends Fragment implements RemoteFragmentInterface {
    private static final String TAG = "RawControlFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View homePage = inflater.inflate(R.layout.fragment_homepage, container, false);


        return homePage;
    }
// Setters amd Getters fot title and Id

    @Override
    public ConnectionService getLoomoService() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getFragmendId() {
        return 0;
    }

    @Override
    public boolean isFragmentId(int id) {
        return false;
    }

    @Override
    public void setTitle(String title) {

    }
}
