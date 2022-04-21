package another.me.com.segway.remote.robot.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.connectivity.BufferMessage;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.Frame;
import com.segway.robot.sdk.vision.stream.StreamType;

import java.io.ByteArrayOutputStream;



public class StreamVideoService {

    private static final String TAG = "StreamVideoService";
    private final Context context;

    public Vision vision;

    public static StreamVideoService instance;


    //check if streming viedo initalize or not
    public static  StreamVideoService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Stream Video is not initialized ");
        }
        // if it was initalize then return streaming video instance
        return instance;
    }

    public StreamVideoService(Context context) {
        this.context = context;
        initalization();
        instance = this;
    }

    // calling restartService() in class Receiver to start VedioStreaming service
    public void restartService()
    {
        initalization();
    }

    public void initalization () {
        //initialize the Vision instance
        this.vision = Vision.getInstance();


        //bind to the service

        this.vision.bindService(this.context, new ServiceBinder.BindStateListener() {
            @Override
            // bind video streaming service

            public void onBind() {
                Log.d(TAG, "Video streaming bind successfully");
            }

            @Override
            // unbind video streaming service

            public void onUnbind(String reason) {
                Log.d(TAG, "Video streaming  unbound");
            }
        });





    }

    public void startTransferringImageStream(final MessageConnection messageConnection) {
        // send message to start transferring image stream
        Log.i(TAG, "startTransferringImageStream called");

        //get the images captured by the Intel RealSense
        this.vision.startListenFrame(StreamType.COLOR, new Vision.FrameListener() {

            private int frameCount = 0;

            @Override
            public void onNewFrame(int streamType, Frame frame) {
                if (frameCount == 0) {
                    Log.d(TAG, "sending frame");

                    try {
                        // Get image frame as bitmap
                        // default image format (Bitmap)
                        Bitmap bitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(frame.getByteBuffer());
                        // Convert from bitmap to jpg
                        // Compress via JPG
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                        byte[] byteArray = os.toByteArray();
                        // if the image size bigger than the maximum size (1 MB)

                        Log.d(TAG, "Byte size: " + byteArray.length);
                        if (byteArray.length > 1000000) {
                            Log.w(TAG, "Bigger than 1MB!!");
                        } else {


                            // if the size is applicable then send the message

                            messageConnection.sendMessage(new BufferMessage(byteArray));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                    frameCount = 0;
                } else {
                    frameCount++;
                }
            }
        });





    }//end startTransferringImageStream

    // stop images streaming
   public void stopTransferringImageStream() {
        Log.d(TAG, "stopTransferringImageStream called");
        //send message to stop transferring image stream
        this.vision.stopListenFrame(StreamType.COLOR);

    }
    // calling disconnect() in class LoomoService to stop vedioSreaming service
    public void disconnect() {
        this.vision.unbindService();
    }



}// end StreamViedo class
