package core;

import java.io.Serializable;

public class Request implements Serializable {
    public String type;
    public Object data;
    public int id;

    public Request(int id, String type, Object data){
        this.id = id;
        this.type = type;
        this.data = data;
    }
}
