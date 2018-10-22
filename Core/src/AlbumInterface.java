import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AlbumInterface extends Remote {
    ArrayList<Album> index() throws  RemoteException;
    void create(Album album) throws RemoteException, CustomException;
    Album read(int id) throws RemoteException, CustomException;
    void update(Album new_album) throws RemoteException, CustomException;
    void delete(int id) throws RemoteException;

    // Critic
    ArrayList<Critic> critics(int album_id) throws RemoteException, CustomException;
    void critic_create(Critic critic) throws RemoteException, CustomException;
    Critic critic(int album_id, int critic_pos) throws RemoteException, CustomException;

    // Song
    ArrayList<Song> songs(int album_id) throws RemoteException, CustomException;
    void song_create(int album_id, Song song) throws RemoteException, CustomException;
    Song song(int album_id, int song_id) throws RemoteException, CustomException;
    void song_update(int album_id, Song song) throws RemoteException, CustomException;
    void song_delete(int album_id, int song_id) throws RemoteException, CustomException;

    // Genres
    ArrayList<Genre> song_genres(int album_id, int song_id) throws RemoteException, CustomException;
    void song_genre_add(int album_id, int song_id, int genre_id) throws RemoteException, CustomException;
}
