package another.me.com.segway.remote.phone.fragment.base;

import another.me.com.segway.remote.phone.service.ConnectionService;



public interface RemoteFragmentInterface {
    ConnectionService getLoomoService();

    String getTitle();

    int getFragmendId();

    boolean isFragmentId(int id);

    void setTitle(String title);
}
