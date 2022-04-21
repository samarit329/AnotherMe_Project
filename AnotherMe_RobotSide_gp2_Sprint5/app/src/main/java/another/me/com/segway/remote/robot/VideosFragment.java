package another.me.com.segway.remote.robot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;



public class VideosFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;



    private AbsListView mListView;
    private ListAdapter mAdapter;


    //  create an instance of video fragment
    public static VideosFragment newInstance()
    {
        return new VideosFragment();
    }
    public VideosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override

    // create the view of videos list
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View videosFragmentView = inflater.inflate(R.layout.fragment_videos, container, false);
        mListView = videosFragmentView.findViewById(android.R.id.list);
        mListView.setEmptyView(videosFragmentView.findViewById(android.R.id.empty));
        requestPermissions(getActivity(), getActivity().findViewById(R.id.fragment_container));
        // Set the adapter
        (mListView).setAdapter(mAdapter);
        // Set OnItemClickListener when an item clicks
        mListView.setOnItemClickListener(this);
        return videosFragmentView;
    }

    @Override

    // check if the fragment interaction listener have been implemented or not
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    // on destroy set the listener to null
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    // when click an item call onItemClick
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface
            // which is (the activity, if the fragment is attached to one) that an item has been selected.
            Cursor cursor = ((VideosAdapter) mAdapter).getCursor();
            cursor.moveToPosition(position);
            mListener.onVideosFragmentInteraction(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)));
        }
    }

    //create the default content for Fragment which is a TextView that is shown when the list is empty
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    // check the permissions
    private void requestPermissions(final Activity thisActivity, View videosFragmentView) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //  show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user
                // After the use sees the explanation, try again to request the permission.
                ViewRecord.permissionsSnackbar = Snackbar.make(videosFragmentView, "Storage access permission is required to scan and play media files on this device.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(thisActivity,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        ViewRecord.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                ViewRecord.permissionsSnackbar.show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        ViewRecord.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // call medisstore from vitamio to reach the extrnal storage and store videos
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Video.VideoColumns.DISPLAY_NAME, MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns._ID};
            Cursor c = getActivity().getContentResolver().query(uri, projection, null, null, null);
            mAdapter = new VideosAdapter(getActivity(), c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onVideosFragmentInteraction(String id);

        public void onSectionAttached(int number);
    }
}// end class VideoFragment
