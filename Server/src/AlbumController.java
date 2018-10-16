import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Controller
    public ArrayList<Album> index() {
        ArrayList<Album> albums = server.database.album_all();
        System.out.println("Action album index: " + albums.size() + " albums");

        return albums;
    }

    public void create(Album album) throws CustomException {
        System.out.print("Action album create: " + album.toString());

        try {
            album.validate();
            server.database.album_create(album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public Album read(int id) throws CustomException {
        System.out.print("Action album read: " + id);

        try {
            Album album = this.server.database.album_find(id);
            System.out.println(" found");
            return album;
        } catch (CustomException ce) {
            System.out.println(" not found");
            throw ce;
        }
    }

    public void update(Album new_album) throws CustomException {
        System.out.print("Action album update: " + new_album.toString());

        try {
            new_album.validate();
            server.database.album_update(new_album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public void delete(int id) {
        System.out.println("Action album delete: " + id);

        server.database.album_delete(id);
    }

    public ArrayList<Critic> critics(int album_id) throws CustomException {
        ArrayList<Critic> critics = this.server.database.album_critics(album_id);
        System.out.println("Action album critics: " + album_id + " " + critics.size() + " critics");

        return critics;
    }


    // ORM
}
