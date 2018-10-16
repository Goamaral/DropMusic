import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AlbumInterface extends Remote {
    ArrayList<Album> index() throws  RemoteException;
    void create(Album album) throws RemoteException, CustomException;
    Album read(int id) throws RemoteException, CustomException;
    void update(Album new_album) throws RemoteException, CustomException;
    void delete(int id) throws RemoteException;
    ArrayList<Critic> critics(int album_id) throws RemoteException, CustomException;
}
