import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Controller
    public ArrayList<Album> index() {
        ArrayList<Album> albums = server.database.album_all();
        System.out.println("Action album index: " + albums.size() + " albums");

        return albums;
    }

    public void create(Album album) throws CustomException {
        System.out.print("Action album create: " + album.toString());

        try {
            album.validate();
            server.database.album_create(album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public Album read(int id) throws CustomException {
        System.out.print("Action album(" + id + ") read: ");

        try {
            Album album = this.server.database.album_find(id);
            System.out.println(" found");
            return album;
        } catch (CustomException ce) {
            System.out.println(" not found");
            throw ce;
        }
    }

    public void update(Album new_album) throws CustomException {
        System.out.print("Action album(" + new_album.id + ") update: " + new_album.toString());

        try {
            new_album.validate();
            server.database.album_update(new_album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public void delete(int id) {
        System.out.println("Action album(" + id + ") delete: ");

        server.database.album_delete(id);
    }

    // Critic
    public ArrayList<Critic> critics(int album_id) throws CustomException {
        ArrayList<Critic> critics = this.server.database.album_critics(album_id);
        System.out.println("Action album(" + album_id +") critics: " + critics.size() + " critics");

        return critics;
    }

    public void critic_create(Critic critic) throws CustomException {
        System.out.print("Action album critic create:");

        try {
            critic.validate();
            this.server.database.album_critic_create(critic);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Critic critic(int album_id, int critic_pos) throws CustomException {
        System.out.print("Action album(" + album_id + ") critic(" + critic_pos + ") read:");

        try {
            Critic critic = this.server.database.album_critic_find(album_id, critic_pos);
            System.out.println(" success");
            return critic;
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    // Song
    public ArrayList<Song> songs(int album_id) throws CustomException {
        ArrayList<Song> songs = this.server.database.album_song_all(album_id);
        System.out.println("Action album(" + album_id + ") song index: " + songs.size() + " songs");

        return songs;
    }

    public void song_create(int album_id, Song song) throws CustomException {
        System.out.print("Action album(" + album_id + ") song create:");

        try {
            song.validate();
            this.server.database.albums_song_create(album_id, song);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Song song(int album_id, int song_id) throws RemoteException, CustomException {
        Album album;

        System.out.println("Action album(" + album_id + ") song(" + song_id +") read:");

        try {
            album = this.server.database.album_find(album_id);
        } catch (CustomException ce) {
            System.out.println(" failed");
            ce.extraFlag = 1;
            throw ce;
        }

        if (song_id >= album.songs.size()) {
            System.out.println(" failed");
            throw new CustomException("Song not found");
        }

        System.out.println(" success");

        return album.songs.get(song_id);
    }

    public void song_update(int album_id, int song_id, Song new_song) throws RemoteException, CustomException {
        System.out.print("Action album(" + album_id + ") song(" + song_id + ") update: ");

        try {
            new_song.validate();
            this.server.database.album_song_update(album_id, song_id, new_song);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    // ORM
}
