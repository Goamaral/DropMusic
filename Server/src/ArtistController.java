import java.util.ArrayList;

public class ArtistController implements ArtistInterface {
    Server server;

    ArtistController(Server server) {
        this.server = server;
    }

    // Album
    public ArrayList<Artist> index() throws CustomException {
        String response = this.server.dbRequest("artist_all", new Object());
        ArrayList<Artist> artists = (ArrayList<Artist>) Serializer.deserialize(response);

        System.out.println("Action artist index: " + artists.size() + " artists");

        return artists;
    }

    public void create(int user_id, Artist artist) throws CustomException {
        System.out.print("Action artist create: " + artist.name);

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(artist);

        try {
            artist.validate();
            this.server.dbRequest("artist_create", args);
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }

        System.out.println("success");
    }

    public Artist read(int id) throws CustomException {
        System.out.print("Action artist(" + id + ") read: ");

        String response = this.server.dbRequest("artist_find", id);

        Artist artist = (Artist) Serializer.deserialize(response);

        System.out.println("success");

        return artist;
    }

    public void update(int user_id, Artist new_artist) throws CustomException {
        System.out.print("Action artist(" + new_artist.id + ") update: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(new_artist);

        try {
            new_artist.validate();

            String response = this.server.dbRequest("artist_update", args);
            ArrayList<Integer> editor_ids = (ArrayList<Integer>) Serializer.deserialize(response);

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

        this.server.dbRequest("artist_delete", id);

        System.out.println("success");
    }

    public ArrayList<Song> songs(int id) throws CustomException {
        System.out.println("Artist(" + id + ") songs");

        String response = this.server.dbRequest("artist_songs", id);

        return (ArrayList<Song>) Serializer.deserialize(response);
    }

    public Artist search(String query) throws CustomException {
        System.out.print("Actions search artist (" + query + "): ");

        String response = this.server.dbRequest("artist_all", new Object());
        ArrayList<Artist> artists = (ArrayList<Artist>) Serializer.deserialize(response);

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