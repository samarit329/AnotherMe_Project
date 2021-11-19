package another.me.com.segway.remote.phone.util;

import com.segway.robot.mobile.sdk.connectivity.StringMessage;

//  Storing command messages class

public class CommandStringFactory {

    public static StringMessage getStringMessage(String[] command) {
        StringBuilder fullString = new StringBuilder();

        for (String elem : command) {
            fullString.append(elem);
            fullString.append(";");
        }
        // remove trailing ;
        fullString.deleteCharAt(fullString.length() -1);
        return new StringMessage(fullString.toString());
    }

}
