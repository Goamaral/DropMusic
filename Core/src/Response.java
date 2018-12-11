import java.io.Serializable;

public class Response implements Serializable {
    int id;
    Object data;

    Response(int id, Object data) {
        this.id = id;
        this.data = data;
    }
}
