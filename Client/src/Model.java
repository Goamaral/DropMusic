import java.io.Serializable;

public abstract class Model implements Serializable {
    abstract void create() throws CustomException;
    abstract void read() throws CustomException;
    abstract void update() throws CustomException;
    abstract void validator() throws CustomException;
    abstract void delete() throws CustomException;
}
