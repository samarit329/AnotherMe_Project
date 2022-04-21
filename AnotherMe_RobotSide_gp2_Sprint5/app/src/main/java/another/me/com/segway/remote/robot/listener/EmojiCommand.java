package another.me.com.segway.remote.robot.listener;


import another.me.com.segway.remote.robot.service.EmojiService;

public class EmojiCommand extends MessageCommand {

    public EmojiCommand(String[] message) {

        super(message);
    }

    @Override
    public void execute() {
        EmojiService.getInstance().doEmoji(message[1]);
    }
}
