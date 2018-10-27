import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Album
    public ArrayList<Album> index() throws CustomException {
        System.out.println("Action album index: ");

        Object response_object = this.server.dbRequest("album_all", true);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        ArrayList<Album> albums = (ArrayList<Album>) response.data;

        System.out.println(albums.size() + " albums");

        return albums;
    }

    public void create(int user_id, Album album) throws CustomException {
        System.out.print("Action album(" + album.name + ") create by (" + user_id + "): ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(album);

        album.validate();
        Object response_object = this.server.dbRequest("album_create", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public Album read(int id) throws CustomException {
        System.out.print("Action album(" + id + ") read: ");

        Object response_object = this.server.dbRequest("album_find", id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (Album) response.data;
    }

    public void update(int user_id, Album new_album) throws CustomException {
        System.out.print("Action album(" + new_album.id + ") update by (" + user_id + "): ");

        new_album.validate();

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(new_album);

        Object response_object = this.server.dbRequest("album_update", args);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        for (int editor_id : (ArrayList<Integer>) response.data) {
            this.server.send_notifications(new Job(editor_id, "Album " + new_album.id + " was edited"));
        }

        System.out.println("success");
    }

    public void delete(int id) {
        System.out.println("Action album(" + id + ") delete: ");

        this.server.dbRequest("album_delete", id);

        System.out.println("success");
    }

    public String artists(int id) throws CustomException {
        System.out.println("Action album(" + id + ") artists: ");

        Object response_object = this.server.dbRequest("album_artists", id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (String) response.data;
    }

    public String genres(int id) throws CustomException {
        System.out.println("Action album(" + id + ") genres: ");

        Object response_object = this.server.dbRequest("album_genres", id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (String) response.data;
    }

    public Album search(String query) throws CustomException {
        System.out.println("Actions search album (" + query + "): ");

        Object response_object = this.server.dbRequest("album_all", true);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        for (Album album : (ArrayList<Album>) response.data) {
            System.out.println(album.name + " - " + query + " -> " + album.name.contains(query));
            if (album.name.contains(query)) {
                System.out.println("success");

                return album;
            }
        }

        System.out.println("failure");
        throw new CustomException("Album not found");
    }

    // Critic
    public ArrayList<Critic> critics(int album_id) throws CustomException {
        System.out.println("action album(" + album_id + ") critics: ");

        Object response_object = this.server.dbRequest("album_critics", album_id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        ArrayList<Critic> critics = (ArrayList<Critic>) response.data;

        System.out.println(critics.size() + " critics");

        return critics;
    }

    public void critic_create(int album_id, Critic critic) throws CustomException {
        System.out.print("Action album critic create: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(critic);

        critic.validate();
        Object response_object = this.server.dbRequest("album_critic_create", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public Critic critic(int critic_id) throws CustomException {
        System.out.print("Action critic(" + critic_id + ") read: ");

        Object response_object = this.server.dbRequest("album_critic_find", critic_id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (Critic) response.data;
    }

    // Song
    public ArrayList<Song> songs(int album_id) throws CustomException {
        System.out.println("Action album(" + album_id + ") songs: ");

        Object response_object = this.server.dbRequest("album_song_all", album_id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        ArrayList<Song> songs = (ArrayList<Song>) response.data;

        System.out.println(songs.size() + " songs");

        return songs;
    }

    public void song_create(int album_id, Song song) throws CustomException {
        System.out.print("Action album(" + album_id + ") song create: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(song);

        song.validate();
        this.server.dbRequest("album_song_create", args);
        System.out.println("success");
    }

    public Song song(int song_id) throws CustomException {
        System.out.print("Action song(" + song_id +") read: ");

        Object response_object = this.server.dbRequest("song_find", song_id);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (Song) response.data;
    }

    public void song_update(Song new_song) throws CustomException {
        System.out.print("Action song(" + new_song.id + ") update: ");

        new_song.validate();
        Object response_object = this.server.dbRequest("album_song_update", new_song);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public void song_delete(int album_id, int song_id) {
        System.out.println("Action album(" + album_id + ") song(" + song_id + ") delete");

        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(song_id);

        this.server.dbRequest("album_song_delete", args);

        System.out.println("success");
    }

    public IpPort requestSongUpload(int user_id, int song_id, String ext) throws CustomException {
        System.out.println("Action upload request by " + user_id);

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(song_id);
        args.add(ext);

        Object response_object = this.server.dbRequest("request_song_upload", args);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (IpPort) response.data;
    }

    public ArrayList<StoredSong> song_downloads(int song_id, int user_id) throws CustomException {
        System.out.println("Action download list");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(user_id);

        Object response_object = this.server.dbRequest("song_downloads", args);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (ArrayList<StoredSong>) response.data;
    }

    public IpPort requestSongDownload(int user_id, int stored_song_id) throws UnknownHostException, CustomException {
        System.out.println("Action download request by " + user_id + " ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(stored_song_id);

        Object response_object = this.server.dbRequest("request_song_download", args);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        return (IpPort) response.data;
    }

    public ArrayList<StoredSong> user_uploads(int user_id, int song_id) throws CustomException {
        System.out.println("Action user(" + user_id + ") uploads");

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(song_id);

        Object response_object = this.server.dbRequest("user_uploads", args);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        System.out.println("success");

        return (ArrayList<StoredSong>) response.data;
    }

    public void song_share(int stored_song_id, int user_id) throws CustomException {
        System.out.println("Action share music with " + user_id + " ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(stored_song_id);
        args.add(user_id);

        Object response_object = this.server.dbRequest("song_share", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    // Genre
    public ArrayList<Genre> genres_all() throws CustomException {
        System.out.println("Action album genres ");

        Object response_object = this.server.dbRequest("genre_all", true);

        this.server.catch_response_exception(response_object);

        Response response = (Response) response_object;

        ArrayList<Genre> genres = (ArrayList<Genre>) response.data;

        System.out.println(genres.size() + " genres");

        return genres;
    }

    public void song_genre_add(int song_id, int genre_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") add: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(genre_id);

        Object response_object = this.server.dbRequest("album_song_genre_add", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public void song_genre_create(Genre genre) throws CustomException {
        System.out.print("Action genre create: " + genre.name + " ");

        Object response_object = this.server.dbRequest("genre_create", genre);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public ArrayList<Genre> song_genres(Song song) throws CustomException {
        System.out.print("Action song(" + song.id + ") genres: ");

        ArrayList<Genre> genres = new ArrayList<>();

        for (int genre_id : song.genres_ids) {
            Object response_object = this.server.dbRequest("genre_find", genre_id);

            this.server.catch_response_exception(response_object);

            Response response = (Response) response_object;

            genres.add((Genre) response.data);
        }

        System.out.println("success");

        return genres;
    }

    public void song_genre_delete(int song_id, int genre_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") delete");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(genre_id);

        Object response_object = this.server.dbRequest("album_song_genre_remove", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    // Artists
    public void song_artist_add(int song_id, int artist_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") add: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(artist_id);

        Object response_object = this.server.dbRequest("album_song_artist_add", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public ArrayList<Artist> song_artists(Song song) throws CustomException {
        System.out.print("Action song(" + song.id + ") artists: ");

        ArrayList<Artist> artists = new ArrayList<>();

        for (int artist_id : song.artist_ids) {
            Object response_object = this.server.dbRequest("artist_find", artist_id);

            this.server.catch_response_exception(response_object);

            Response response = (Response) response_object;

            artists.add((Artist) response.data);
        }

        System.out.println("success");

        return artists;
    }

    public void song_artist_delete(int song_id, int artist_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") delete ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(artist_id);

        Object response_object = this.server.dbRequest("album_song_artist_remove", args);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }
}