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
        String response = this.server.dbRequest("album_all", new Object());
        ArrayList<Album> albums = (ArrayList<Album>) Serializer.deserialize(response);

        System.out.println("Action album index: " + albums.size() + " albums");

        return albums;
    }

    public void create(int user_id, Album album) throws CustomException {
        System.out.print("Action album create by (" + user_id + "): " + album.toString());

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(album);

        try {
            album.validate();
            this.server.dbRequest("album_create", args);
        } catch (CustomException ce) {
            System.out.println(" failure");
            throw ce;
        }

        System.out.println(" success");
    }

    public Album read(int id) throws CustomException {
        System.out.print("Action album(" + id + ") read: ");

        String response = this.server.dbRequest("album_find", id);

        Album album = (Album) Serializer.deserialize(response);

        System.out.println("success");

        return album;
    }

    public void update(int user_id, Album new_album) throws CustomException {
        System.out.print("Action album(" + new_album.id + ") update by (" + user_id + "): " + new_album.toString());

        try {
            new_album.validate();

            ArrayList<Object> args = new ArrayList<>();
            args.add(user_id);
            args.add(new_album);

            String response = this.server.dbRequest("album_update", args);
            ArrayList<Integer> editor_ids = (ArrayList<Integer>) Serializer.deserialize(response);

            for (int editor_id : editor_ids) {
                this.server.send_notifications(new Job(editor_id, "Album " + new_album.id + " was edited"));
            }
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public void delete(int id) {
        System.out.println("Action album(" + id + ") delete: ");

        this.server.dbRequest("album_delete", id);

        System.out.println(" success");
    }

    public String artists(int id) throws CustomException {
        String response = this.server.dbRequest("album_artists", id);

        String artists = (String) Serializer.deserialize(response);

        System.out.println("success");

        return artists;
    }

    public String genres(int id) throws CustomException {
        String response = this.server.dbRequest("album_genres", id);

        String genres = (String) Serializer.deserialize(response);

        System.out.println("success");

        return genres;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Album search(String query) throws CustomException {
        System.out.println("Actions search album (" + query + "): ");

        String response = this.server.dbRequest("album_all", new Object());
        ArrayList<Album> albums = (ArrayList<Album>) Serializer.deserialize(response);

        for (Album album : albums) {
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
        String response = this.server.dbRequest("album_critics", album_id);
        ArrayList<Critic> critics = (ArrayList<Critic>) Serializer.deserialize(response);

        System.out.println("Action album(" + album_id +") critics: " + critics.size() + " critics");

        return critics;
    }

    public void critic_create(int album_id, Critic critic) throws CustomException {
        System.out.print("Action album critic create:");

        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(critic);

        try {
            critic.validate();
            this.server.dbRequest("album_critic_create", args);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Critic critic(int critic_id) throws CustomException {
        System.out.print("Action critic(" + critic_id + ") read:");

        String response = this.server.dbRequest("album_critic_find", critic_id);
        Critic critic = (Critic) Serializer.deserialize(response);

        System.out.println(" success");
        return critic;
    }

    //----------------------------------------------------------------------------------------------------------------
    // Song
    public ArrayList<Song> songs(int album_id) throws CustomException {
        String response = this.server.dbRequest("album_song_all", album_id);
        ArrayList<Song> songs = (ArrayList<Song>) Serializer.deserialize(response);

        System.out.println("Action album(" + album_id + ") song index: " + songs.size() + " songs");

        return songs;
    }

    public void song_create(int album_id, Song song) throws CustomException {
        System.out.print("Action album(" + album_id + ") song create:");

        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(song);

        try {
            song.validate();
            this.server.dbRequest("album_song_create", args);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Song song(int song_id) throws CustomException {
        System.out.print("Action song(" + song_id +") read:");

        String response = this.server.dbRequest("song_find", song_id);
        Song song = (Song) Serializer.deserialize(response);

        System.out.println(" success");

        return song;

    }

    public void song_update(Song new_song) throws CustomException {
        System.out.print("Action song(" + new_song.id + ") update: ");

        try {
            new_song.validate();
            this.server.dbRequest("album_song_update", new_song);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public void song_delete(int album_id, int song_id) {
        System.out.println("Action album(" + album_id + ") song(" + song_id + ") delete");

        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(song_id);

        this.server.dbRequest("album_song_delete", args);
    }

    public IpPort requestSongUpload(int user_id, int song_id, String ext) throws UnknownHostException, CustomException {
        System.out.println("Action upload request by " + user_id);

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(song_id);
        args.add(ext);

        String response = this.server.dbRequest("request_song_upload", args);
        int port = (int) Serializer.deserialize(response);

        return new IpPort(InetAddress.getByName("127.0.0.1"), port);
    }

    public ArrayList<StoredSong> song_downloads(int song_id, int user_id) throws CustomException {
        System.out.println("Action download list");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(user_id);

        String response = this.server.dbRequest("song_downloads", args);
        return (ArrayList<StoredSong>) Serializer.deserialize(response);
    }

    public IpPort requestSongDownload(int user_id, int stored_song_id) throws UnknownHostException, CustomException {
        System.out.println("Action download request by " + user_id);

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(stored_song_id);

        String response = this.server.dbRequest("request_song_download", args);
        int port = (int) Serializer.deserialize(response);

        return new IpPort(InetAddress.getByName("127.0.0.1"), port);
    }

    public ArrayList<StoredSong> user_uploads(int user_id, int song_id) throws CustomException {
        System.out.println("Action user(" + user_id + ") uploads");

        ArrayList<Object> args = new ArrayList<>();
        args.add(user_id);
        args.add(song_id);

        String response = this.server.dbRequest("user_uploads", args);
        return (ArrayList<StoredSong>) Serializer.deserialize(response);
    }

    public void song_share(int stored_song_id, int user_id) {
        System.out.println("Action share music with " + user_id);

        ArrayList<Object> args = new ArrayList<>();
        args.add(stored_song_id, user_id);

        this.server.dbRequest("song_share", args);
    }

    // Genre
    public ArrayList<Genre> genres_all() throws CustomException {
        String response = this.server.dbRequest("genre_all", new Object());

        ArrayList<Genre> genres = (ArrayList<Genre>) Serializer.deserialize(response);

        System.out.println("Action genres: " + genres.size() + " genres");

        return genres;
    }

    public void song_genre_add(int song_id, int genre_id) {
        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") add: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(genre_id);

        this.server.dbRequest("album_song_genre_add", args);

        System.out.println("success");
    }

    public void song_genre_create(Genre genre) {
        System.out.print("Action genre create: " + genre.name + " ");

        this.server.dbRequest("genre_create", genre);

        System.out.println("success");
    }

    public ArrayList<Genre> song_genres(Song song) throws CustomException {
        System.out.print("Action song(" + song.id + ") genres: ");

        ArrayList<Genre> genres = new ArrayList<>();

        for (int genre_id : song.genres_ids) {
            String response = this.server.dbRequest("genre_find", genre_id);
            Genre genre = (Genre) Serializer.deserialize(response);
            genres.add(genre);
        }

        System.out.println("success");

        return genres;
    }

    public void song_genre_delete(int song_id, int genre_id) {
        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") delete");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(genre_id);

        this.server.dbRequest("album_song_genre_remove", args);
    }

    // Artists
    public void song_artist_add(int song_id, int artist_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") add: ");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(artist_id);

        this.server.dbRequest("album_song_artist_add", args);

        System.out.println("success");
    }

    public ArrayList<Artist> song_artists(Song song) throws CustomException {
        System.out.print("Action song(" + song.id + ") artists: ");

        ArrayList<Artist> artists = new ArrayList<>();

        for (int artist_id : song.artist_ids) {
            String response = this.server.dbRequest("artist_find", artist_id);
            Artist artist = (Artist) Serializer.deserialize(response);

            artists.add(artist);
        }

        System.out.println("success");

        return artists;
    }

    public void song_artist_delete(int song_id, int artist_id) {
        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") delete");

        ArrayList<Object> args = new ArrayList<>();
        args.add(song_id);
        args.add(artist_id);

        this.server.dbRequest("album_song_artist_remove", args);
    }
}
