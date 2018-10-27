import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ArtistInterface extends Remote {
    ArrayList<Artist> index() throws RemoteException, CustomException;
    void create(int user_id, Artist artist) throws RemoteException, CustomException;
    Artist read(int id) throws RemoteException, CustomException;
    void update(int user_id, Artist new_artist) throws RemoteException, CustomException;
    void delete(int id) throws RemoteException;
    ArrayList<Song> songs(int id) throws RemoteException, CustomException;
    Artist search(String query) throws RemoteException, CustomException;
}