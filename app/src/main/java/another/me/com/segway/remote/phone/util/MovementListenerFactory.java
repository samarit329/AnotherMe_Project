package another.me.com.segway.remote.phone.util;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import another.me.com.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import io.github.controlwear.virtual.joystick.android.JoystickView;



// this class to listening for the movement of the joysticks
public class MovementListenerFactory {

    public static final int JOYSTICK_SPEED = 1;
    public static final int JOYSTICK_DIRECTION = 2;
    public static final int JOYSTICK_PITCH = 3;
    public static final int JOYSTICK_YAW = 4;


    public static View.OnTouchListener getJoyStickReleaseListener(final JoyStickControllerFragment joystickController, int type) {

        // if the joysticks release
        View.OnTouchListener listener = null;

        switch (type) {

             // in case of the body joysticks (speed) touch
            case JOYSTICK_SPEED:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // check if the joystick release (when joystick action equal ACTION_UP (pressed gesture has finished))
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.speedAngle = 0; // set the speed angle equal zero
                            joystickController.speedStrength = 0;// set the speed strength equal zero
                            joystickController.sendMoveCommand(true);// set force vale to true and send it to sendMoveCommand() method
                        }
                        return false;
                    }
                };
                break;

            // in case of the body joysticks (direction) touch
            case JOYSTICK_DIRECTION:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // check if the joystick release (when joystick action equal ACTION_UP (pressed gesture has finished))
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.directionAngle = 0; // set the direction angle equal zero
                            joystickController.directionStrength = 0; // set the direction strength equal zero
                            joystickController.sendMoveCommand(true);// set force vale to true and send it to sendMoveCommand() method
                        }
                        return false;
                    }
                };
                break;

            // in case of the head joysticks (pitch) touch
            case JOYSTICK_PITCH:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // check if the joystick release (when joystick action equal ACTION_UP (pressed gesture has finished))
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.pitchAngle = 0; // set the pitch angle equal zero
                            joystickController.pitchStrength = 0;// set the pitch strength equal zero

                            // if the head not in smooth mode
                            if (!joystickController.smoothMode) {
                                joystickController.sendHeadStopOrientation(); // call sendHeadStopOrientation() method
                            }
                        }
                        return false;
                    }
                };
                break;

            // in case of the head joysticks (yaw) touch
            case JOYSTICK_YAW:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // check if the joystick release (when joystick action equal ACTION_UP (pressed gesture has finished))
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.yawAngle = 0; // set the yaw angle equal zero
                            joystickController.yawStrength = 0; // set the yaw strength equal zero

                            // if the head not in smooth mode
                            if (!joystickController.smoothMode) {
                                joystickController.sendHeadStopOrientation(); // call sendHeadStopOrientation() method
                            }
                        }
                        return false;
                    }
                };
                break;
        }

        return listener; // return listener to the calling method
    }//end method getJoyStickReleaseListener




    public static JoystickView.OnMoveListener getJoystickMoveListener(final JoyStickControllerFragment fragment, int type) {

        // if the joysticks touch to move
        JoystickView.OnMoveListener listener = null;

        switch (type) {


            // in case of the body joysticks (speed) touch
            case JOYSTICK_SPEED:
                listener = new JoystickView.OnMoveListener() {
                    @Override

                    public void onMove(int angle, int strength) {
                        fragment.speedAngle = angle; // set the direction angle equal to the receiving value
                        fragment.speedStrength = strength; // set the direction strength equal to the receiving value
                        fragment.sendMoveCommand(false); // set force vale to false and send it to sendMoveCommand() method
                    }
                };
                break;

            // in case of the body joysticks (direction) touch
            case JOYSTICK_DIRECTION:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.directionAngle = angle; // set the direction angle equal to the receiving value
                        fragment.directionStrength = strength; // set the direction strength equal to the receiving value
                        fragment.sendMoveCommand(false); // set force vale to false and send it to sendMoveCommand() method
                    }
                };
                break;

            // in case of the head joysticks (pitch) touch
            case JOYSTICK_PITCH:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.pitchAngle = angle;// set the pitch angle equal to the receiving value
                        fragment.pitchStrength = strength; // set the pitch strength equal to the receiving value
                        fragment.sendHeadCommand(false);  // set force vale to false and send it to sendMoveCommand() method
                    }
                };
                break;

            // in case of the head joysticks (yaw) touch
            case JOYSTICK_YAW:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.yawAngle = angle;// set the yaw angle equal to the receiving value
                        fragment.yawStrength = strength; // set the yaw strength equal to the receiving value
                        fragment.sendHeadCommand(false);// set force vale to false and send it to sendMoveCommand() method
                    }
                };
                break;

        }

        return listener;
    }// end getJoystickMoveListener method
}//end class
