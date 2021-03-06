import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Album
    public ArrayList<Album> index() {
        ArrayList<Album> albums = server.database.album_all();
        System.out.println("Action album index: " + albums.size() + " albums");

        return albums;
    }

    public void create(int user_id, Album album) throws CustomException {
        System.out.print("Action album create by (" + user_id + "): " + album.toString());

        try {
            album.validate();
            this.server.database.album_create(user_id, album);
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
            System.out.println("success");
            return album;
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public void update(int user_id, Album new_album) throws CustomException {
        System.out.print("Action album(" + new_album.id + ") update by (" + user_id + "): " + new_album.toString());

        try {
            new_album.validate();
            ArrayList<Integer> editor_ids = this.server.database.album_update(user_id, new_album);

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

        server.database.album_delete(id);
    }

    public String artists(int id) throws CustomException { return this.server.database.album_artists(id); }

    public String genres(int id) throws CustomException { return this.server.database.album_genres(id); }

    public Album search(String query) throws CustomException {
        System.out.println("Actions search album (" + query + "): ");

        ArrayList<Album> albums = this.server.database.album_all();

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
        ArrayList<Critic> critics = this.server.database.album_critics(album_id);
        System.out.println("Action album(" + album_id +") critics: " + critics.size() + " critics");

        return critics;
    }

    public void critic_create(int album_id, Critic critic) throws CustomException {
        System.out.print("Action album critic create:");

        try {
            critic.validate();
            this.server.database.album_critic_create(album_id, critic);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Critic critic(int critic_id) throws CustomException {
        System.out.print("Action critic(" + critic_id + ") read:");

        try {
            Critic critic = this.server.database.album_critic_find(critic_id);
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
            this.server.database.album_song_create(album_id, song);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Song song(int song_id) throws CustomException {
        Song song;

        System.out.print("Action song(" + song_id +") read:");

        try {
            song = this.server.database.song_find(song_id);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failure");
            throw ce;
        }

        return song;
    }

    public void song_update(Song new_song) throws CustomException {
        System.out.print("Action song(" + new_song.id + ") update: ");

        try {
            new_song.validate();
            this.server.database.album_song_update(new_song);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public void song_delete(int album_id, int song_id) {
        System.out.println("Action album(" + album_id + ") song(" + song_id + ") delete");
        this.server.database.album_song_delete(album_id, song_id);
    }

    public IpPort requestSongUpload(int user_id, int song_id, String ext) throws UnknownHostException, CustomException {
        System.out.println("Action upload request by " + user_id);
        int port = this.server.database.requestSongUpload(user_id, song_id, ext);
        return new IpPort(InetAddress.getByName("127.0.0.1"), port);
    }

    public ArrayList<StoredSong> song_downloads(int song_id, int user_id) throws CustomException {
        System.out.println("Action download list");
        return this.server.database.song_downloads(song_id, user_id);
    }

    public IpPort requestSongDownload(int user_id, int stored_song_id) throws UnknownHostException, CustomException {
        System.out.println("Action download request by " + user_id);
        int port = this.server.database.requestSongDownload(user_id, stored_song_id);
        return new IpPort(InetAddress.getByName("127.0.0.1"), port);
    }

    public ArrayList<StoredSong> user_uploads(int user_id, int song_id) {
        System.out.println("Action user(" + user_id + ") uploads");
        return this.server.database.user_uploads(user_id, song_id);
    }

    public void song_share(int stored_song_id, int user_id) throws CustomException {
        System.out.println("Action share music with " + user_id);
        this.server.database.song_share(stored_song_id, user_id);
    }

    // Genre
    public ArrayList<Genre> genres_all() {
        ArrayList<Genre> genres = this.server.database.genre_all();

        System.out.println("Action genres: " + genres.size() + " genres");

        return genres;
    }

    public void song_genre_add(int song_id, int genre_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") add: ");

        try {
            this.server.database.album_song_genre_add(song_id, genre_id);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public void song_genre_create(Genre genre) throws CustomException {
        System.out.print("Action genre create: " + genre.name + " ");

        try {
            this.server.database.genre_create(genre);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public ArrayList<Genre> song_genres(Song song) {
        System.out.print("Action song(" + song.id + ") genres: ");

        ArrayList<Genre> genres = new ArrayList<>();

        for (int genre_id : song.genres_ids) {
            try {
                genres.add(this.server.database.genre_find(genre_id));
            } catch (CustomException ce) {
                // ignore if genre not found
            }
        }

        System.out.println("success");

        return genres;
    }

    public void song_genre_delete(int song_id, int genre_id) {
        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") delete");

        this.server.database.album_song_genre_remove(song_id, genre_id);
    }

    // Artists
    public void song_artist_add(int song_id, int artist_id) throws CustomException {
        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") add: ");

        try {
            this.server.database.album_song_artist_add(song_id, artist_id);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public ArrayList<Artist> song_artists(Song song) {
        System.out.print("Action song(" + song.id + ") artists: ");

        ArrayList<Artist> artists = new ArrayList<>();

        for (int artist_id : song.artist_ids) {
            try {
                artists.add(this.server.database.artist_find(artist_id));
            } catch (CustomException ce) {
                // ignore if artist not found
            }
        }

        System.out.println("success");

        return artists;
    }

    public void song_artist_delete(int song_id, int artist_id) {
        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") delete");

        this.server.database.album_song_artist_remove(song_id, artist_id);
    }


}
