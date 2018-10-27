import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist extends Model implements Serializable {
    int id;
    String name;

    // Relationships
    ArrayList<Integer> song_ids = new ArrayList<>();

    Artist(String name) { this.name = name; }

    public void validate() throws CustomException { this.nameValidator(); }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    void addSong(Song song) throws CustomException {
        if (this.song_ids.contains(song.id)) {
            throw new CustomException("Song already exists");
        } else {
            this.song_ids.add(song.id);
        }
    }

    void removeSong(Song song) {
        int index = this.song_ids.indexOf(song.id);
        if (index != -1) this.song_ids.remove(index);
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("name", this.name);


        JSONArray list = new JSONArray();
        for(int song_id : this.song_ids){
            list.add(song_id );
        }
        obj.put("song_ids", list);

        return obj;

    }
}
