package another.me.com.segway.remote.robot.service;

import android.content.Context;
import android.util.Log;
import android.widget.ViewSwitcher;

import com.segway.robot.sdk.emoji.BaseControlHandler;
import com.segway.robot.sdk.emoji.Emoji;
import com.segway.robot.sdk.emoji.EmojiPlayListener;
import com.segway.robot.sdk.emoji.EmojiView;
import com.segway.robot.sdk.emoji.HeadControlHandler;
import com.segway.robot.sdk.emoji.exception.EmojiException;
import com.segway.robot.sdk.emoji.player.RobotAnimator;
import com.segway.robot.sdk.emoji.player.RobotAnimatorFactory;

import another.me.com.segway.remote.robot.MainActivity;
import another.me.com.segway.remote.robot.R;

public class EmojiService {

    private static final String TAG = "EmojiService";

    public static EmojiService instance;
    private final Context context;
    private final MainActivity activity;
    private final ViewSwitcher viewSwitcher;

    private Emoji emoji;

    public static EmojiService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("EmojiService is not initialized ");
        }
        return instance;
    }

    public EmojiService(Context context, MainActivity activity) {

        this.context = context;
        this.activity = activity;

        // view to switch activity to emoji screen
        this.viewSwitcher = (ViewSwitcher) activity.findViewById(R.id.view_switcher);
        //initialize service
        init();
        // set instance
        instance = this;
    }
    // calling initialization to initialize emoji service
    public void restartService()
    {
        init();
    }

    private void init() {
        // get instanse from class emoji
        this.emoji = Emoji.getInstance();
        // initialize emoji  service
        this.emoji.init(this.context);

        this.emoji.init(activity.getApplicationContext());
        // init head control handler
        this.emoji.setHeadControlHandler(headControlHandler);
        // init base control handler
        this.emoji.setBaseControlHandler(baseControlHandler);
    }

    public void doEmoji(String emoji) {
        // lets assume this is always the right view when we use emoji functions
        this.emoji.setEmojiView((EmojiView) this.viewSwitcher.getCurrentView());
        // init emoji id defult will be  -1 for every emoji there is unique id .
        int emojiId = -1;
        // the id chosen to meet sdk emojis id
        switch (emoji) {
            // for look aroud id 1
            case "look_around":
                emojiId = 1;
                break;
            // for look comfort id 2
            case "look_comfort":
                emojiId = 2;
                break;
            // for look curious id 3
            case "look_curious":
                emojiId = 3;
                break;
            // for look on id 4
            case "look_no":
                emojiId = 4;
                break;
            // for look up id 31
            case "look_up":
                emojiId = 31;
                break;
            // for look down id 32
            case "look_down":
                emojiId = 32;
                break;
            // look left Id 33
            case "look_left":
                emojiId = 33;
                break;
            // for look right Id 34
            case "look_right":
                emojiId = 34;
                break;
            // for look left Id 35
            case "turn_left":
                emojiId = 35;
                break;
            // for turn right Id 36
            case "turn_right":
                emojiId = 36;
                break;
            // for turn around  Id 37
            case "turn_around":
                emojiId = 37;
                break;
            // for turn full Id 38
            case "turn_full":
                emojiId = 38;
                break;
        }

        try {
            if (emojiId != -1) { // check the id if has been changed
                // send the id to start the animation in robot screen
                this.emoji.startAnimation(RobotAnimatorFactory.getReadyRobotAnimator(emojiId), emojiPlayListener);
            } else {
                // in case emoji id does not change "-1 "  it is mean the id wrong so display invalid emoji id
                Log.e(TAG, "Received animation String is not valid: " + emoji);
            }
            // throw exception
        } catch (EmojiException e) {
            Log.d(TAG, "EmojiException", e);
        }
    }


    //  make an interface to let the Emoji control to  set the robot head to Emoji mode
    // to enable using Emojis from SDK
    private HeadControlHandler headControlHandler = new HeadControlHandler() {
        @Override
        public int getMode() {
            return 0;
        }

        @Override
        public void setMode(int mode) {

        }

        @Override
        public void setWorldPitch(float angle) {

        }

        @Override
        public void setWorldYaw(float angle) {

        }

        @Override
        public float getWorldPitch() {
            return 0;
        }

        @Override
        //yaw
        public float getWorldYaw() {
            return 0;
        }
    };


    // convert the robot base to a base control handler
// to enable using Emojis from SDK
// implements emoji methods from SDK
    private BaseControlHandler baseControlHandler = new BaseControlHandler() {
        @Override
        public void setLinearVelocity(float velocity) {

        }

        @Override
        public void setAngularVelocity(float velocity) {

        }

        @Override
        public void stop() {

        }

        @Override
        public Ticks getTicks() {
            return null;
        }
    };



    // implements emoji methods from SDK
    private EmojiPlayListener emojiPlayListener = new EmojiPlayListener() {
        @Override
        public void onAnimationStart(RobotAnimator animator) {
        }

        @Override
        public void onAnimationEnd(RobotAnimator animator) {
        }

        @Override
        public void onAnimationCancel(RobotAnimator animator) {
        }
    };
    // calling disconnect() in class LoomoService to stop Emoji service
    public void disconnect() {
    }

}//end class EmojiService