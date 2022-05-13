package another.me.com.segway.remote.phone.service;


 /*
 This is used so a Fragment can register itself with the ConnectionService to declare that it wishes
 to receive incoming ByteMessages. This is needed since the Service does not know which Fragment is currently active.
 */
public interface ByteMessageReceiver {

    void handleByteMessage(byte[] message);
}
