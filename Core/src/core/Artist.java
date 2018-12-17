package core;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable {
    public int id;
    public String name;
    public String info;

    // Relationships
    public ArrayList<Integer> song_ids = new ArrayList<>();
    public ArrayList<Integer> editor_ids = new ArrayList<>();

    public Artist(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public Artist() {

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


        if (errors.size() > 0) throw new CustomException(errors);
    }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    void infoValidator() throws CustomException {
        this.info.replaceAll("\\s","");

        if (this.info.length() == 0)
            throw new CustomException("Info can't be empty");
    }

    public void addSong(Song song) throws CustomException {
        if (this.song_ids.contains(song.id)) {
            throw new CustomException("Song already exists");
        } else {
            this.song_ids.add(song.id);
        }
    }

    public void removeSong(Song song) {
        int index = this.song_ids.indexOf(song.id);
        if (index != -1) this.song_ids.remove(index);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
