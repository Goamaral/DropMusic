import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface UserInterface extends Remote {
    User login(User user) throws RemoteException, CustomException, NoSuchAlgorithmException;
    void register(User user) throws RemoteException, CustomException, NoSuchAlgorithmException;
}