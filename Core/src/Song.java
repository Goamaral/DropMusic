import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Song implements Serializable {
    int id;
    String name;
    String info;
    String artists = "";
    String genres = "";

    // Relationships
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

    void removeGenre(Genre genre) {
        String[] genre_parts;
        List<String> genre_parts_list;

        int index = this.genres_ids.indexOf(genre.id);

        if (index != -1) {
            genre_parts = this.genres.split(", ");
            genre_parts_list = Arrays.asList(genre_parts);

            this.genres = "";

            for (int i = 0; i < genre_parts_list.size(); ++i) {
                if (i == index) continue;

                if (this.genres.equals("")) {
                    this.genres = genre_parts_list.get(i);
                } else {
                    this.genres += ", " + genre_parts_list.get(i);
                }
            }

            this.genres_ids.remove(index);
        }
    }

    public String toString() {
        return "Song: { "
                + "name: " + this.name + ", "
                + "info: " + this.info
                + " }";
    }
}
