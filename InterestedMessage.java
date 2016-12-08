import java.io.Serializable;

/**
 * Created by Dartyx on 12/7/2016.
 */
public class InterestedMessage extends Message implements Serializable {


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;
    public InterestedMessage(int index){
        this.index=index;
        this.type=2;
    }


}
