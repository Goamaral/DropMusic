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

    public void update(Artist new_artist) throws CustomException {
        System.out.print("Action artist(" + new_artist.id + ") update: ");

        try {
            new_artist.validate();
            server.database.artist_update(new_artist);
        } catch (CustomException ce) {
            System.out.println("failed");
            throw ce;
        }

        System.out.println("success");
    }

    public void delete(int id) {
        System.out.println("Action artist(" + id + ") delete: ");

        server.database.artist_delete(id);
    }

}