import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Song extends Model implements Serializable {
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

    void addArtist(Artist artist) throws CustomException {
        if (this.artist_ids.contains(artist.id)) throw new CustomException("Artist already exists");

        if (this.artists.length() != 0) this.artists += ", ";
        this.artists += artist.name;

        this.artist_ids.add(artist.id);
    }

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

    void removeArtist(Artist artist) {
        String[] artist_parts;
        List<String> artist_parts_list;

        int index = this.artist_ids.indexOf(artist.id);

        if (index != -1) {
            artist_parts = this.artists.split(", ");
            artist_parts_list = Arrays.asList(artist_parts);

            this.artists = "";

            for (int i = 0; i < artist_parts_list.size(); ++i) {
                if (i == index) continue;

                if (this.artists.equals("")) {
                    this.artists = artist_parts_list.get(i);
                } else {
                    this.artists += ", " + artist_parts_list.get(i);
                }
            }

            this.artist_ids.remove(index);
        }
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("name", this.name);
        obj.put("info", this.info);
        obj.put("artists", this.artists);
        obj.put("genres", this.genres);

        JSONArray list = new JSONArray();
        for(int i = 0; i < this.artist_ids.size(); i++){
            list.add(this.artist_ids.get(i));
        }
        obj.put("artist_ids", list);

        JSONArray list2 = new JSONArray();
        for(int genres_id : this.genres_ids){
            list.add(genres_id);
        }
        obj.put("genres_ids", list2);

        return obj;

    }
}
