import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserInterface extends Remote {
    // Controller
    void login(User user) throws RemoteException, CustomException;
    void register(User user) throws RemoteException, CustomException;
}