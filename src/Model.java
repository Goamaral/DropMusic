public abstract class Model {
    abstract void create() throws CustomException;
    abstract void read() throws CustomException;
    abstract void update() throws CustomException;
    abstract void validator() throws CustomException;
    abstract void delete() throws CustomException;
}
