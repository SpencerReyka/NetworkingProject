import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by Dartyx on 12/7/2016.
 */
public class HandShakeMessage extends Message implements Serializable {
        private String header = "P2PFILESHARINGPROJ";

    public int getPeerID() {
        return peerID;
    }

    public void setPeerID(int peerID) {
        this.peerID = peerID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    private int peerID;

        public HandShakeMessage(int PeerID){
        type = -1;
        peerID = PeerID;
        }

}
