package another.me.com.segway.remote.robot.listener;

import another.me.com.segway.remote.robot.service.TextToSpeechService;

public class TextToSpeechCommand extends MessageCommand {

    private static final String TAG = "TextToSpeechCommand";

    public TextToSpeechCommand(String[] message) {
        super(message);
    }

    @Override
    // implement method execute from the super class MessageComman
    public void execute() {
        // pass the received message to speak method in TextToSpeechService class
        TextToSpeechService.getInstance().speak(message[1]);
    }
}//end class TextToSpeechCommand