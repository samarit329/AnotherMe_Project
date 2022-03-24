package another.me.com.segway.remote.robot.listener;


import another.me.com.segway.remote.robot.service.EmojiService;

public class EmojiCommand extends MessageCommand {

    public EmojiCommand(String[] message) {

        super(message);
    }

    @Override
    // get the emoji id then display it
    public void execute() {
        EmojiService.getInstance().doEmoji(message[1]);
    }
}
