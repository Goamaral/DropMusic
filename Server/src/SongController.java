import java.rmi.RemoteException;
import java.util.ArrayList;

public class SongController implements SongInterface {
    Server server;

    SongController(Server server) { this.server = server; }

    public ArrayList<Song> index() {
        ArrayList<Song> songs = this.server.database.song_all();
        System.out.println("Action song index: " + songs.size() + " songs");
        return songs;
    }

    public void create(Song song) throws CustomException {

    }

    public Song read(int id) throws CustomException {
        return null;
    }

    public void update(Song new_song) throws CustomException {

    }

    public void delete(int id) {

    }

    public String artistsToString(int id) throws CustomException {
        return null;
    }

}
