package another.me.com.segway.remote.robot.listener;



public abstract class MessageCommand {

    protected final String[] message;

    public MessageCommand(String[] message)

    {
        this.message = message;
    }

    // Create an abstract method called (execute)
    public abstract void execute();
}
