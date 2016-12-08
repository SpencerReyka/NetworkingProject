import java.io.Serializable;

/**
 * Created by Dartyx on 12/7/2016.
 */
public class Message implements Serializable {
    protected int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    protected byte message[];

    public Message(){

    }

}
