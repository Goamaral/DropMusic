import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AlbumInterface extends Remote {
    ArrayList<Album> index() throws  RemoteException;
    void create(int user_id, Album album) throws RemoteException, CustomException;
    Album read(int id) throws RemoteException, CustomException;
    void update(int user_id, Album new_album) throws RemoteException, CustomException;
    void delete(int id) throws RemoteException;
    String artists(int id) throws RemoteException, CustomException;
    String genres(int id) throws RemoteException, CustomException;
    Album search(String query) throws RemoteException, CustomException;

    // Critic
    ArrayList<Critic> critics(int album_id) throws RemoteException, CustomException;
    void critic_create(int album_id, Critic critic) throws RemoteException, CustomException;
    Critic critic(int critic_id) throws RemoteException, CustomException;

    // Song
    ArrayList<Song> songs(int album_id) throws RemoteException, CustomException;
    void song_create(int album_id, Song song) throws RemoteException, CustomException;
    Song song(int song_id) throws CustomException, RemoteException;
    void song_update(Song new_song) throws RemoteException, CustomException;
    void song_delete(int album_id, int song_id) throws RemoteException, CustomException;
    IpPort requestSongUpload(int user_id, int song_id, String ext) throws RemoteException, UnknownHostException, CustomException;
    ArrayList<StoredSong> song_downloads(int song_id, int user_id) throws RemoteException, CustomException;
    IpPort requestSongDownload(int user_id, int stored_song_id) throws RemoteException, UnknownHostException, CustomException;
    void song_share(int song_id, int user_id) throws RemoteException;

    // Genres
    ArrayList<Genre> genres_all() throws RemoteException;
    void song_genre_add(int song_id, int genre_id) throws RemoteException, CustomException;
    void song_genre_create(Genre genre) throws RemoteException, CustomException;
    ArrayList<Genre> song_genres(Song song) throws RemoteException;
    void song_genre_delete(int song_id, int genre_id) throws CustomException, RemoteException;

    // Artists
    void song_artist_add(int song_id, int artist_id) throws CustomException, RemoteException;
    ArrayList<Artist> song_artists(Song song) throws RemoteException;
    void song_artist_delete(int song_id, int artist_id) throws CustomException, RemoteException;
}

