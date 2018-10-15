import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Album implements Serializable {
    int id;
    String name;
    String info;
    Date releaseDate;
    String realeaseDateString;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Relationships
    ArrayList<Integer> critics = new ArrayList<>();
    ArrayList<Integer> genres = new ArrayList<>();
    ArrayList<Integer> songs = new ArrayList<>();
    ArrayList<Integer> artists = new ArrayList<>();

    Album(String name, String info, String releaseDateString) {
        this.name = name;
        this.info = info;
        this.realeaseDateString = releaseDateString;
    }

    public void validate() throws CustomException {
        this.dateValidator();
    }

    public void dateValidator() throws CustomException {
        try {
            this.releaseDate = this.dateFormat.parse(this.realeaseDateString);
        } catch (ParseException pe) {
            throw new CustomException("Invalid date format");
        }
    }

    public void addCritic(int critic_id) { this.critics.add(critic_id); }

    public void addGenre(int genre_id) { this.genres.add(genre_id); }

    public void addSong(int song_id) { this.songs.add(song_id); }

    public void addArtist(int artist_id) { this.artists.add(artist_id); }

    public String toString() {
        return "Album: { name: " + this.name + ", info: " + this.info + ", " + this.releaseDate.toString() + " }";
    }
}
