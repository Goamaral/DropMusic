import java.rmi.RemoteException;
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

    public void create(int user_id, Artist artist) throws CustomException {
        System.out.print("Action artist create: " + artist.name);

        try {
            artist.validate();
            server.database.artist_create(user_id, artist);
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

    public void update(int user_id, Artist new_artist) throws CustomException {
        System.out.print("Action artist(" + new_artist.id + ") update: ");

        try {
            new_artist.validate();

            ArrayList<Integer> editor_ids = server.database.artist_update(user_id, new_artist);

            for (int editor_id : editor_ids) {
                this.server.send_notifications(new Job(editor_id, "Artist " + new_artist.id + " was edited"));
            }
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

    public ArrayList<Song> songs(int id) throws CustomException {
        return this.server.database.artist_songs(id);
    }

    public Artist search(String query) throws CustomException {
        System.out.print("Actions search artist (" + query + "): ");

        ArrayList<Artist> artists = this.server.database.artist_all();

        for (Artist artist : artists) {
            if (artist.name.contains(query)) {
                System.out.println("success");
                return artist;
            }
        }

        System.out.println("failure");
        throw new CustomException("Artist not found");
    }

}