import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ArtistInterface extends Remote {
    ArrayList<Artist> index() throws RemoteException;
    void create(Artist artist) throws RemoteException, CustomException;
    Artist read(int id) throws RemoteException, CustomException;
/*
    void update(Artist new_artist) throws RemoteException, CustomException;
    void delete(int id) throws RemoteException;
*/
}