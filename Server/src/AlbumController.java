import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Controller
    public ArrayList<Album> index() {
        ArrayList<Album> albums = server.database.album_all();
        System.out.print("List " + albums.size() + " albums");

        return albums;
    }

    public void create(Album album) throws CustomException {
        System.out.println("Create album " + album.name);

        try {
            album.validate();
            server.database.album_create(album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
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
