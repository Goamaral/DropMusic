import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable {
    int id;
    String name;
    String info;

    // Relationships
    int album_id;
    ArrayList<Integer> artist_ids;
    ArrayList<Integer> genres_ids;

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

    void addGenre(int genre_id, String genre_name) { this.genres_ids.add(genre_id); }

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
