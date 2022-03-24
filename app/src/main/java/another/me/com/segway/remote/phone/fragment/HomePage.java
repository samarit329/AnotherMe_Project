package another.me.com.segway.remote.phone.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import another.me.com.segway.remote.phone.R;
//import another.me.com.segway.remote.phone.VideoChatViewActivity;
import another.me.com.segway.remote.phone.fragment.base.RemoteFragmentInterface;
import another.me.com.segway.remote.phone.service.ConnectionService;

// home page class (second main UI)

public class HomePage extends Fragment implements RemoteFragmentInterface {
    private static final String TAG = "RawControlFragment";

    Button moveCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_homepage, container, false);

/*
        moveCall=layout.findViewById(R.id.call);
        moveCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){


                Intent intent= new Intent(this, VideoChatViewActivity.class);
                startActivity(intent);


            }
        });//end Listener*/
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
