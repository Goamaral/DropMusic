import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable {
    int id;
    String name;

    // Relationships
    ArrayList<Integer> song_ids;

    Artist(String name) { this.name = name; }

    public void validate() throws CustomException { this.nameValidator(); }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    void addSong(int song_id) { this.song_ids.add(song_id); }
}
