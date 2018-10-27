import java.io.Serializable;

public class Request implements Serializable {
    public String type;
    public Object data;

    Request(String type, Object data){
        this.type = type;
        this.data = data;
    }
}
