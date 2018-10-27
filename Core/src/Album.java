import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Album extends Model implements Serializable {
    int id;
    String name;
    String info;
    Date releaseDate;
    int points = 0;
    String releaseDateString;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Relationships
    ArrayList<Integer> critic_ids = new ArrayList<>();
    ArrayList<Integer> song_ids = new ArrayList<>();

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
            this.releaseDateString = this.dateFormat.format(this.releaseDate);
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

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("name", this.name);
        obj.put("info", this.info);
        obj.put("points", this.points);
        obj.put("releaseDateString", this.releaseDateString);

        JSONArray list = new JSONArray();
        for(int critic_id : this.critic_ids ){
            list.add(critic_id);
        }
        obj.put("critic_ids", list);

        JSONArray list2 = new JSONArray();
        for(int song_id : this.song_ids){
            list.add(song_id);
        }
        obj.put("song_ids", list2);

        return obj;

    }
}
