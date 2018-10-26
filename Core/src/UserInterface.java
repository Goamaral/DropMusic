import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface UserInterface extends Remote {
    User login(User user, int tcp) throws RemoteException, CustomException, NoSuchAlgorithmException;
    void register(User user) throws RemoteException, CustomException, NoSuchAlgorithmException;
    ArrayList<User> normal_users() throws RemoteException;
    void promote(int user_id) throws RemoteException, CustomException;
    User read(int id) throws RemoteException, CustomException;
}