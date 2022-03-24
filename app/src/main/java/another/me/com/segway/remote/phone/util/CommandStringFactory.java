package another.me.com.segway.remote.phone.util;

import com.segway.robot.mobile.sdk.connectivity.StringMessage;

//  Storing and return command messages class

public class CommandStringFactory {


    public static StringMessage getStringMessage(String[] command) {

        //Creates an empty string builder with a capacity of 16 (16 empty elements).
        StringBuilder fullString = new StringBuilder();

        for (String elem : command) {
            fullString.append(elem);
            fullString.append(";");
        }

        // remove trailing ;
        fullString.deleteCharAt(fullString.length() -1);
        // return the sting command (message) to the calling method so that it can send the message to the robot to do the action.
        return new StringMessage(fullString.toString());
    }

}
