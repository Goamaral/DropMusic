import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Controller
    public ArrayList<Album> index() {
        return server.database.album_all();
    }

    public void create(Album album) throws RemoteException, CustomException {

    }


    public void update(int album_id, Album new_album) throws RemoteException, CustomException {

    }

    public Album read(int album_id) throws RemoteException, CustomException {
        return null;
    }

    public void delete(int album_id) throws RemoteException, CustomException {

    }

    // ORM
}
