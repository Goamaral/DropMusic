import java.util.ArrayList;

public class ArtistController implements ArtistInterface {
    Server server;

    ArtistController(Server server) {
        this.server = server;
    }

    // Album
    public ArrayList<Artist> index() throws CustomException {
        System.out.print("Action artist index ");

        Object response_object = this.server.dbRequest("artist_all", true);

        this.server.catch_response_exception(response_object);

        ArrayList<Artist> artists = (ArrayList<Artist>) response_object;

        System.out.println(" " + artists.size() + " artists");

        return artists;
    }

    public void create(int user_id, Artist artist) throws CustomException {
        System.out.print("Action artist create: " + artist.name);

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(artist);

        artist.validate();
        Object response_object = this.server.dbRequest("artist_create", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public Artist read(int id) throws CustomException {
        System.out.print("Action artist(" + id + ") read: ");

        Object response_object = this.server.dbRequest("artist_find", id);

        this.server.catch_response_exception(response_object);

        System.out.println("success");

        return (Artist) response_object;
    }

    public void update(int user_id, Artist new_artist) throws CustomException {
        System.out.print("Action artist(" + new_artist.id + ") update: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(new_artist);

        new_artist.validate();

        Object response_object = this.server.dbRequest("artist_update", args);

        this.server.catch_response_exception(response_object);

        for (int editor_id : (ArrayList<Integer>) response_object) {
            this.server.send_notifications(new Job(editor_id, "Artist " + new_artist.id + " was edited"));
        }

        System.out.println("success");
    }

    public void delete(int id) {
        System.out.println("Action artist(" + id + ") delete: ");

        this.server.dbRequest("artist_delete", id);

        System.out.println("success");
    }

    public ArrayList<Song> songs(int id) throws CustomException {
        System.out.println("Artist(" + id + ") songs");

        Object response_object = this.server.dbRequest("artist_songs", id);

        this.server.catch_response_exception(response_object);

        System.out.println("success");

        return (ArrayList<Song>) response_object;
    }

    public Artist search(String query) throws CustomException {
        System.out.print("Actions search artist (" + query + "): ");

        Object response_object = this.server.dbRequest("artist_all", true);

        this.server.catch_response_exception(response_object);

        ArrayList<Artist> artists = (ArrayList<Artist>) response_object;

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