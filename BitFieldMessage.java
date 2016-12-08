import java.io.Serializable;

/**
 * Created by Dartyx on 12/7/2016.
 */
public class BitFieldMessage extends Message implements Serializable {
        private boolean bitfield[];

        public BitFieldMessage(boolean bitfield[]){
            type=5;
            this.bitfield=bitfield;
        }

    public void setBitfield(boolean[] bitfield) {
        this.bitfield = bitfield;
    }

    public boolean[] getBitfield() {
        return bitfield;
    }
}
