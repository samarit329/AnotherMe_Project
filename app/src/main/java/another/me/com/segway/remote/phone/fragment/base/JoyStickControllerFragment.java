package another.me.com.segway.remote.phone.fragment.base;


import android.util.Log;

import java.util.Arrays;

import another.me.com.segway.remote.phone.util.CommandStringFactory;


public class JoyStickControllerFragment extends RemoteFragment {


    public static final String TAG = "JoyStickController";

    private static final int DELTA_COMMAND_SEQUENCE = 100;

    public static final float HALF_OF_PI = 3.14F / 2F;

    public long lastCommandSend = 0;

    public float speedAngle = 0F;
    public float speedStrength = 0F;

    public float directionAngle = 0F;
    public float directionStrength = 0F;

    public float pitchAngle = 0F;
    public float pitchStrength = 0F;

    public float yawAngle = 0F;
    public float yawStrength = 0F;

    public boolean smoothMode = true;


////////////////////////////////////////////////////     HEAD       ///////////////////////////////////////////////////////////////////////////


    // this method for send base move command to loomo robot
    public void sendMoveCommand(boolean force) {
        // compare the current time minus last command send with time delta to avoid flooding Loomo with commands
        if (force || (System.currentTimeMillis() - lastCommandSend > DELTA_COMMAND_SEQUENCE)) {
            lastCommandSend = System.currentTimeMillis(); // set current time in milliseconds to lastCommandSend var

            // Now get the speed and direction to send it to the  Loomo robot

            float speed = getNormalizedSpeed(); // get the speed from getNormalizedSpeed() method
            float direction = getNormalizedDirection();// get the direction from the getNormalizedDirection()

            // after get the speed and direction now create the message that will send to loomo robot

            String[] commandToSend = getMoveCommand(speed, direction);// that will return string array contain of move command ,speed and the direction
            Log.d(TAG, "send move: " + Arrays.toString(commandToSend));
            getLoomoService().send(CommandStringFactory.getStringMessage(commandToSend));// send move command to the loomo robot
        }
    }


    private String[] getMoveCommand(float speed, float direction) {
        // This method to create message of string array then return it to sendMoveCommand() method to send it to loomo robot
        return new String[]{"move", String.valueOf(speed), String.valueOf(direction)};
    }

    // this method used by sendMoveCommand() method to get the speed to send it to Loomo Robot
    public float getNormalizedSpeed() {

        // this method will return speed value between -3 and 3 ,based on stored member variables speedStrength and speedAngle
        // 3(forward max speed) and -3(backwards max speed)
        float speed = speedStrength;
        if (speedAngle > 180) {
            speed = speed * -1F;
        }
        speed = speed / 100F;

        return speed;
    }

    public float getNormalizedDirection() {
        float direction = (directionAngle);


        // if the directionAngle bigger than 0 and less than or equal 90 the direction will be to the right
        if (directionAngle > 0 && directionAngle <= 90) {
            float delta = directionAngle / 90F;
            direction = -1.0f + delta;
        }

        // if the directionAngle bigger than 90 and less than or equal 180 the direction will be to the left
        else if (directionAngle > 90 && directionAngle <= 180) {
            float delta = (directionAngle - 90F) / 90F;
            direction = 0.0f + delta;
        }



        //if the directionAngle bigger than 180 and less than or equal 270 the direction will be to the right
        else if (directionAngle > 180 && directionAngle <= 270) {
            float delta = (directionAngle - 180F) / 90F;
            direction = 1.0f - delta;
        }

        // if the directionAngle bigger than 270 and less than or equal 360 the direction will be to the left
        else if (directionAngle > 270 && directionAngle <= 360) {
            float delta = (directionAngle - 270F) / 90F;
            direction = 0f - delta;
        }

        return direction;
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////   BODY    ////////////////////////////////////////////////
    // this method for send head move command to loomo robot
    public void sendHeadCommand(boolean force) {
        // compare the current time minus last command send with time delta to avoid flooding Loomo with commands
        if (force || (System.currentTimeMillis() - lastCommandSend > DELTA_COMMAND_SEQUENCE)) {
            lastCommandSend = System.currentTimeMillis();// set current time in milliseconds to lastCommandSend var

            // that will return string array contain of head command, pithchValue, and yawValue
            String[] commandToSend = getHeadCommand(this.smoothMode, getPitchValue(), getYawValue());

            Log.d(TAG, "send head: " + Arrays.toString(commandToSend));
            getLoomoService().send(CommandStringFactory.getStringMessage(commandToSend)); // send head move command to the loomo robot
        }
    }


    private String[] getHeadCommand(boolean smoothMode, float pitchValue, float yawValue) {

        String command = "smooth"; //In state of smooth the head can be controlled by setting the angle using the base as the reference frame.
        if (!smoothMode) {
            command = "orientation";//In state of orientation you can control the head orientation by setting the head rotation velocity.
        }
        // create message of string array then return it to sendHeadCommand() method to send it to loomo robot
        return new String[]{"head", command, String.valueOf(pitchValue), String.valueOf(yawValue)};
    }

    public void sendHeadStopOrientation() {
        // this method used by getJoyStickReleaseListener() method in MovementListenerFactory class when case to stop the Orientation mode

        // create the message that will send to loomo robot
        String[] command = getHeadCommand(false, 0F, 0F);
        getLoomoService().send(CommandStringFactory.getStringMessage(command)); // send head move command to the loomo robot
    }


    private float getPitchValue() {
        // This method to return pitch value to sendHeadCommand() method

        Log.d(TAG, "pitch Strength: " + pitchStrength);
        Log.d(TAG, "pitch Angle: " + pitchAngle);


        float pitchValue = (pitchStrength * 3.14F) / 100F;
        if (smoothMode) {
            // totally  down = -90 (- pi/2)
            if (pitchAngle > 180) {
                pitchValue = (pitchValue / 2) * -1;
            }
        } else {
            // totally up = 180 (pi)
            if (pitchAngle > 180) {
                pitchValue = pitchValue * -1;
            }
        }
        //return pitch value to send, based on joysticks position
        return pitchValue;

    }

    private float getYawValue() {
        if (smoothMode) {
            return getYawSmoothMode();
        } else {
            return getYawOrientationMode();
        }
    }


    private float getYawOrientationMode() {
        Log.d(TAG, "yawAngle: " + yawAngle);
        Log.d(TAG, "yawStrength: " + yawStrength);

        float yawValue = (yawStrength * 3.14F) / 100F;


        if ((yawAngle > 0 && yawAngle <= 90) || (yawAngle > 270 && yawAngle <= 360)) {
            yawValue = yawValue * -1;
        }

        Log.d(TAG, "yawOrientationValue: " + yawValue);

        return yawValue;
    }


    // Return yaw value that ranges from.. totally right=150 (-pi * 0.8).. totally left=150(pi*0.8)
    private float getYawSmoothMode() {
        Log.d(TAG, "yawAngle: " + yawAngle);
        Log.d(TAG, "yawStrength: " + yawStrength);

        float yawValue = 0F;

        //robot's head turn right
        if (yawAngle > 0 && yawAngle <= 90) {
            yawValue = -HALF_OF_PI + ((yawAngle / 90F) * HALF_OF_PI);
        }
        //robot's head turn left
        else if (yawAngle > 90 && yawAngle <= 180) {
            yawValue = 0 + (((yawAngle - 90F) / 90F) * HALF_OF_PI);
        }
        //robot's head turn left
        else if (yawAngle > 180 && yawAngle <= 270) {
            float tempAngle = yawAngle;
            if (yawAngle > 240) {
                tempAngle = 240;
            }

            yawValue = HALF_OF_PI + (((tempAngle - 180) / 90) * HALF_OF_PI);
        }
        //robot's head turn right
        else if (yawAngle > 270 && yawAngle <= 360) {

            float tempAngle = yawAngle;
            if (yawAngle < 300) {
                tempAngle = 300;
            }

            yawValue = -HALF_OF_PI - (((360 - tempAngle) / 90) * HALF_OF_PI);
        } else {
            Log.e(TAG, "Received pitch joystick value, which is out of scope");
        }

        Log.d(TAG, "yawSmoothValue: " + yawValue);
        //return yaw value to send, based on joystick postion
        return yawValue;
    }




}
