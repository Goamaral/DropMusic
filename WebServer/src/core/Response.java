package core;

import java.io.Serializable;

public class Response implements Serializable {
    public int id;
    public Object data;

    Response(int id, Object data) {
        this.id = id;
        this.data = data;
    }
}
