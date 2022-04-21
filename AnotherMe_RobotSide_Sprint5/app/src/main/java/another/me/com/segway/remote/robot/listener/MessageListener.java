package another.me.com.segway.remote.robot.listener;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.segway.robot.sdk.baseconnectivity.Message;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;

import java.util.Arrays;

import another.me.com.segway.remote.robot.MainActivity;
import another.me.com.segway.remote.robot.R;

public class MessageListener implements MessageConnection.MessageListener {

    private static String TAG = "MessageListener";
    private final MainActivity activity;

    private Toast messageToast = null;
    Button captureButton ;

    public MessageListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    // if an error occured during message sent
    public void onMessageSentError(Message message, String error) {
        Log.d(TAG, "onMessageSentError: " + error + " during message: " + message);
    }

    @Override
    // if message was sent successfully
    public void onMessageSent(Message message) {
        Log.d(TAG, "onMessageSent: " + message + " was sent successfully!");
    }

    // split message to know what type of service is needed
    private String[] splitMessage(Message message) {
        return message.getContent().toString().split(";");
    }

    @Override
    public void onMessageReceived(final Message message) {
        Log.d(TAG, "onMessageReceived: " + message);

        long startTime = System.currentTimeMillis();
        // create command from class MessageCommand
        MessageCommand command = null;
        String[] splitMessage = splitMessage(message);
        String prefix = splitMessage[0];

        Log.i(TAG, prefix + "Received");
        try {
            switch (prefix) {

                //if the received service of type vision
                case "vision":
                    command = new StreamVideoCommand(splitMessage);
                    break;
                //if the received service of type head
                case "head":
                    command = new HeadCommand(splitMessage);
                    break;
                case "move":
                    command = new BaseCommand(splitMessage);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                case "speak":
                    command = new TextToSpeechCommand(splitMessage);
                    break;
                case "settings":
                    command = new SettingsCommand(splitMessage, activity);
                    break;
                case "emoji":
                    command = new EmojiCommand(splitMessage);
                    break;
                case "recordStart":
                    //activity.onCaptureClick(activity.mPreview);
                    activity.startrecord();
                    break;
                case "recordStop":
                    //activity.onCaptureClick(activity.mPreview);
                    activity.stoprecord();

                    break;
                default:
                    Log.w(TAG, "Unknown message " + Arrays.toString(splitMessage));
            }


            if (command != null) {
                command.execute();
                Log.i(TAG, command.getClass().toString() + "Executed");
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (messageToast != null) {
                        messageToast.cancel();
                    }
                    messageToast = Toast.makeText(activity, "Got message: " + message.getContent().toString(), Toast.LENGTH_SHORT);
                    messageToast.show();
                }
            });


        } catch (Exception e) {
            Log.w(TAG, "An exeption occured", e);

        }

    }

}