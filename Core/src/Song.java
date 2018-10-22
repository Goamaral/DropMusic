import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable {
    int id;
    String name;
    String info;
    String artists = "";
    String genres = "";

    // Relationships
    int album_id;
    ArrayList<Integer> artist_ids = new ArrayList<>();
    ArrayList<Integer> genres_ids = new ArrayList<>();

    Song(String name, String info) {
        this.name = name;
        this.info = info;
    }

    void validate() throws CustomException { this.nameValidator(); }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    void addArtist(int artist_id, String artist_name) { this.artist_ids.add(artist_id); }

    void addGenre(Genre genre) throws CustomException {
        if (this.genres_ids.contains(genre.id)) throw new CustomException("Genre already exists");

        if (this.genres.length() != 0) this.genres += ", ";
        this.genres += genre.name;

        this.genres_ids.add(genre.id);
    }

    void bindAlbum(int album_id) {
        this.album_id = album_id;
    }

    public String toString() {
        return "Song: { "
                + "name: " + this.name + ", "
                + "info: " + this.info
                + " }";
    }
}
