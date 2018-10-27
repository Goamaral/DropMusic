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
    int points = 0;
    String releaseDateString;

    // Relationships
    ArrayList<Integer> critic_ids = new ArrayList<>();
    ArrayList<Integer> song_ids = new ArrayList<>();
    ArrayList<Integer> editor_ids = new ArrayList<>();

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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = dateFormat.parse(this.releaseDateString);
            this.releaseDateString = dateFormat.format(date);
        } catch (ParseException pe) {
            throw new CustomException("Invalid date format");
        }
    }

    public float getRating() {
        if (this.critic_ids.size() != 0) {
            return this.points / (float)this.critic_ids.size();
        }

        return 0;
    }

    public void addCritic(Critic critic) { this.critic_ids.add(critic.id); }

    public void addSong(int song_id) { this.song_ids.add(song_id); }

    public void removeSong(int song_id) { this.song_ids.remove(song_id); }
}
