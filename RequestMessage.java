import java.io.Serializable;

/**
 * Created by Dartyx on 12/7/2016.
 */
public class RequestMessage extends Message implements Serializable {
    private int index;
    public RequestMessage(int index){
        type=6;
        this.index=index;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
