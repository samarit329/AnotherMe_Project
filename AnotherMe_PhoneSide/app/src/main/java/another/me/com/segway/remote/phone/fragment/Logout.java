package another.me.com.segway.remote.phone.fragment;

import another.me.com.segway.remote.phone.fragment.base.RemoteFragment;
import another.me.com.segway.remote.phone.service.ByteMessageReceiver;
import another.me.com.segway.remote.phone.util.CommandStringFactory;

public class Logout extends RemoteFragment {


    public void log() {

        // creat log message to send it to the robot so terminate the application
        String[] message = {"exit", "exit"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));//send message to the robot

    }


}