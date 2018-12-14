package core;

import java.io.Serializable;

public class StoredSong implements Serializable {
    public int song_id;
    public int uploader_id;
    public int id;
    public String ext;

    public StoredSong(int id, int song_id, int uploader_id, String ext) {
        this.id = id;
        this.song_id = song_id;
        this.uploader_id = uploader_id;
        this.ext = ext;
    }
}