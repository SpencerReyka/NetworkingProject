import java.io.Serializable;

/**
 * Created by Dartyx on 12/7/2016.
 */
public class PieceMessage extends Message implements Serializable {

    private byte[] piece;
    private int index;
    public PieceMessage(int index){
        type=7;
        this.index=index;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPiece(byte[] piece) {
        this.piece = piece;
    }

    public byte[] getPiece() {
        return piece;
    }
}
