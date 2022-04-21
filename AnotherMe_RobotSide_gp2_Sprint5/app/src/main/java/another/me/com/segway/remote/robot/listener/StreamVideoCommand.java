package another.me.com.segway.remote.robot.listener;




import android.util.Log;

import com.segway.robot.sdk.baseconnectivity.MessageConnection;

import another.me.com.segway.remote.robot.service.ConnectivityService;
//import another.me.com.segway.remote.robot.service.HeadService;
import another.me.com.segway.remote.robot.service.StreamVideoService;


public class StreamVideoCommand extends MessageCommand {

    private MessageConnection mMessageConnection;

    private static String TAG = "StreamVideoCommand";

    public StreamVideoCommand(String[] message) {
        super(message);
        //Getting the recevied message
        this.mMessageConnection = ConnectivityService.getInstance().getMessageConnection();
    }

    @Override
    public void execute() {
        // if the received  message request to start vedio streaming
        if (message[1].equals("start")) {
            Log.d(TAG, "strat Video Streaming ");
            StreamVideoService.getInstance().startTransferringImageStream(mMessageConnection);

            // if the received  message request to stop vedio streaming
        } else  if (message[1].equals("end")) {
          Log.d(TAG, "stop Video Streaming");
          StreamVideoService.getInstance().stopTransferringImageStream();

       }

    }// end execute
}// end class