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
    String releaseDateString;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Relationships
    ArrayList<Integer> critics = new ArrayList<>();
    ArrayList<Integer> genres = new ArrayList<>();
    ArrayList<Integer> songs = new ArrayList<>();
    ArrayList<Integer> artists = new ArrayList<>();

    Album(String name, String info, String releaseDateString) {
        this.name = name;
        this.info = info;
        this.releaseDateString = releaseDateString;
    }

    public void validate() throws CustomException {
        ArrayList<String> errors = new ArrayList();

        try {
            this.nameValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        try {
            this.infoValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        try {
            this.dateValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        if (errors.size() > 0) throw new CustomException(errors);
    }

    private void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0) throw new CustomException("Name can't be empty");
    }

    private void infoValidator() throws CustomException {
        this.info.replaceAll("\\s","");

        if (this.info.length() == 0) throw new CustomException("Info can't be empty");
    }

    private void dateValidator() throws CustomException {
        try {
            this.releaseDate = this.dateFormat.parse(this.releaseDateString);
        } catch (ParseException pe) {
            throw new CustomException("Invalid date format");
        }
    }

    public void addCritic(int critic_id) { this.critics.add(critic_id); }

    public void addGenre(int genre_id) { this.genres.add(genre_id); }

    public void addSong(int song_id) { this.songs.add(song_id); }

    public void addArtist(int artist_id) { this.artists.add(artist_id); }

    public String toString() {
        return "Album: { name: " + this.name + ", info: " + this.info + ", release_date: " + this.releaseDateString + " }";
    }
}
