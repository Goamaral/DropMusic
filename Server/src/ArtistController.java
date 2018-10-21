import java.util.ArrayList;

public class ArtistController implements ArtistInterface {
    Server server;

    ArtistController(Server server) {
        this.server = server;
    }

    // Album
    public ArrayList<Artist> index() {
        ArrayList<Artist> artists = server.database.artist_all();
        System.out.println("Action artist index: " + artists.size() + " artists");

        return artists;
    }

    public void create(Artist artist) throws CustomException {
        System.out.print("Action artist create: " + artist.name);

        try {
            artist.validate();
            server.database.artist_create(artist);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public Artist read(int id) throws CustomException {
        System.out.print("Action artist(" + id + ") read: ");

        try {
            Artist artist = this.server.database.artist_find(id);
            System.out.println("success");
            return artist;
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }
/*
    public void update(Album new_album) throws CustomException {
        System.out.print("Action album(" + new_album.id + ") update: " + new_album.toString());

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
        System.out.println("Action album(" + id + ") delete: ");

        server.database.album_delete(id);
    }
*/
}