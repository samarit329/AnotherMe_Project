package another.me.com.segway.remote.robot.listener;

import android.util.Log;
import android.widget.ViewSwitcher;

import com.segway.robot.sdk.emoji.EmojiView;

import another.me.com.segway.remote.robot.MainActivity;
import another.me.com.segway.remote.robot.R;

public class SettingsCommand extends MessageCommand {

    private static final String TAG = "SettingsCommand";

    //private static final String KEY_VOICE = "voice_recognition";
    private static final String KEY_EMOJI = "emoji";

    private final MainActivity activity;
    private ViewSwitcher viewSwitcher;

    public SettingsCommand(String[] message, MainActivity activity) {
        super(message);
        this.activity = activity;
        this.viewSwitcher = (ViewSwitcher) activity.findViewById(R.id.view_switcher);
    }

    @Override
    public void execute() {
        boolean value = Boolean.valueOf(message[2]);
        switch (message[1]) {

            case KEY_EMOJI:
                this.toggleEmoji(value);
                break;
        }

    }




    private void toggleEmoji(final boolean enableEmoji) {
        Log.d(TAG, "toggleEmoji called with " + enableEmoji);
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isOnEmoji = viewSwitcher.getCurrentView().getClass() == EmojiView.class;

                Log.d(TAG, "isOnEmoji: " + isOnEmoji);
                Log.d(TAG, "viewClass: " + viewSwitcher.getCurrentView().getClass());

                if (!isOnEmoji && enableEmoji) {
                    viewSwitcher.showNext();
                }

                if (isOnEmoji && !enableEmoji) {
                    viewSwitcher.showNext();
                }
            }
        });
    }

}

