import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable {
    String name;
    String info;
    String artists = "";

    // Relationships
    int album_id;
    ArrayList<Integer> artist_ids;

    Song(String name, String info) {
        this.name = name;
        this.info = info;
    }

    void validate() throws CustomException {
        ArrayList<String> errors = new ArrayList();

        try {
            this.nameValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        if (errors.size() > 0) throw new CustomException(errors);
    }


    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    void addArtist(int artist_id) {
        this.artist_ids.add(artist_id);
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
