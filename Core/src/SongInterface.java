import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SongInterface extends Remote {
    ArrayList<Song> index() throws RemoteException;
    void create(Song song) throws RemoteException, CustomException;
    Song read(int id) throws RemoteException, CustomException;
    void update(Song new_song) throws RemoteException, CustomException;
    void delete(int id) throws RemoteException;

    String artistsToString(int id) throws RemoteException, CustomException;
}
