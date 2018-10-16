import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Controller
    public ArrayList<Album> index() {
        ArrayList<Album> albums = server.database.album_all();
        System.out.println("Action index: " + albums.size() + " albums");

        return albums;
    }

    public void create(Album album) throws CustomException {
        System.out.print("Action create: " + album.toString());

        try {
            album.validate();
            server.database.album_create(album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }


    public void update(Album new_album) throws CustomException {
        System.out.print("Action update: " + new_album.toString());

        try {
            new_album.validate();
            server.database.album_update(new_album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public Album read(int album_id) throws CustomException {
        System.out.print("Action read: " + album_id);

        try {
            Album album = this.server.database.album_find(album_id);
            System.out.println(" found");
            return album;
        } catch (CustomException ce) {
            System.out.println(" not found");
            throw ce;
        }
    }

    public void delete(int album_id) throws RemoteException, CustomException {

    }

    // ORM
}
