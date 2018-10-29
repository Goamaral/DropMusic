import java.io.Serializable;

class StoredSong implements Serializable {
    int song_id;
    int uploader_id;
    int id;
    String ext;

    StoredSong(int id, int song_id, int uploader_id, String ext) {
        this.id = id;
        this.song_id = song_id;
        this.uploader_id = uploader_id;
        this.ext = ext;
    }
}