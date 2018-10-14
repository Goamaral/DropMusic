import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AlbumInterface extends Remote {
    List<Album> index() throws  RemoteException;
    void create(Album album) throws RemoteException, CustomException;
    void edit(int album_id, Album new_album) throws RemoteException, CustomException;
    Album update(int album_id) throws RemoteException, CustomException;
    void delete(int album_id) throws RemoteException, CustomException;
}
