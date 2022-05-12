package another.me.com.segway.remote.robot.service;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.voice.Speaker;

public class TextToSpeechService implements TextToSpeech.OnInitListener {

    // initialise var of type text to speech
    private TextToSpeech textToS;
    // initialise media player for file playing showcase
    MediaPlayer player = new MediaPlayer();

    @Override
    public void onInit(int status) {
    }


    private static final String TAG = "TextToSpeechService";
    // initialise context and speaker to enable use the Loomo SDK speaker function
    private Speaker speaker;
    private Context context;


    public static TextToSpeechService instance;

    //check if Text To Speech initalize or not
    public static TextToSpeechService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextToSpeechService instance not initialized");
        }
        // if it was initalize then return text to speech instance
        return instance;
    }

    public TextToSpeechService(Context context) {
        this.context = context;
        init();
        this.instance = this;

        textToS = new TextToSpeech(context, this);
    }
    // calling restartService() in class Receiver to start TextToSpeechService
    public void restartService() {
        init();
    }


    public void speak(String sentence) {
        textToS.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
    }

// initialize the Text to speech service (bind and unbind)
    private void init() {
        //bind to the service
        speaker = Speaker.getInstance();
        speaker.bindService(context, new ServiceBinder.BindStateListener() {

            // bind text to speech service
            @Override
            public void onBind() {
                Log.d(TAG, "Speaker service is  onBind");
            }


            // Unbind text to speech service
            @Override
            public void onUnbind(String reason) {
                Log.d(TAG, "Speaker service is  onUnbind");
            }
        });

    }// end int method

    // calling disconnect() in class LoomoService to stop text to speech service

    public void disconnect() {
        this.speaker.unbindService();
    }


}// end calss TextToSpeechService
