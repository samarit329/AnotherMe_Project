package another.me.com.segway.remote.phone.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import another.me.com.segway.remote.phone.R;
import another.me.com.segway.remote.phone.fragment.base.RemoteFragment;
import another.me.com.segway.remote.phone.util.CommandStringFactory;
import another.me.com.segway.remote.phone.service.ConnectionService;

// text to speech and sending Emojis to  the  robot

public class UserInteraction extends RemoteFragment implements Button.OnClickListener{

    private static final String TAG = "EmojiFragment";
    private EditText speechInput;
    Button MoveToFull;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View emojiFragment = inflater.inflate(R.layout.user_interaction, container, false);


        Button soundTestButton = emojiFragment.findViewById(R.id.sound_test);
        soundTestButton.setOnClickListener(mButtonClickListener);
        speechInput = emojiFragment.findViewById(R.id.speech_input);



        // add click listener to all buttons on fragment
        ArrayList<View> buttons1 = emojiFragment.findViewById(R.id.emoji_layout_1).getTouchables();
        ArrayList<View> buttons2 = emojiFragment.findViewById(R.id.emoji_layout_2).getTouchables();

        for (View v : buttons1) {
            v.setOnClickListener(this);
        }
        // listener for Emojis in row 2
        for (View v : buttons2) {
            v.setOnClickListener(this);
        }

        MoveToFull=emojiFragment.findViewById(R.id.movetofull);
        MoveToFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                getFragmentManager().beginTransaction().replace(R.id.content_frame, new LoomoControl()).commit();
                setTitle("Loomo Control");
            }
        });//end Listener

        return emojiFragment;

    }

    // send  Emoji command to robot
    @Override
    public void onClick(View v) {
        // create the first message that indicate the emoje will be sent to switch the view to the emoji view.
        // KEY_EMOJI is string variable with value "emoji" from EmojiController (class for emoji setting)
        String[] message = {"settings", EmojiController.KEY_EMOJI, "true"};
        //Send the first message (active emoji view)
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message));
        //create the second message that send emoji id to the robot
        String[] message1 = {"emoji", v.getTag().toString()};
        // send the second message (emoji id)
        getLoomoService().send(CommandStringFactory.getStringMessage(message1));


    }

// send sound to robot to speak
    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {

        public void onClick(View view) {
            String speak = speechInput.getText().toString().trim();// store the text message
            Log.i(TAG, "Trying to say: " + speak);
            getLoomoService().sendSound(speak);// send the sound to robot
        }
    };







}// end class UserInteraction

