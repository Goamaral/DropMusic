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
            System.out.println("success");
            return album;
        } catch (CustomException ce) {
            System.out.println("failure");
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
        Song song;

        System.out.print("Action album(" + album_id + ") song(" + song_id +") read:");

        try {
            song = this.server.database.album_song_find(album_id, song_id);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failure");
            throw ce;
        }

        return song;
    }

    public void song_update(int album_id, Song new_song) throws CustomException {
        System.out.print("Action album(" + album_id + ") song(" + new_song.id + ") update: ");

        try {
            new_song.validate();
            this.server.database.album_song_update(album_id, new_song);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public void song_delete(int album_id, int song_id) throws CustomException {
        System.out.println("Action album(" + album_id + ") song(" + song_id + ") delete");
        this.server.database.album_song_delete(album_id, song_id);
    }

    // Genre
    public ArrayList<Genre> genres() {
        ArrayList<Genre> genres = this.server.database.genre_all();

        System.out.println("Action genres: " + genres.size() + " genres");

        return genres;
    }

    public void song_genre_add(int album_id, int song_id, int genre_id) throws CustomException {
        System.out.print("Action album(" + album_id + ") song(" + song_id + ") genre(" + genre_id + ") add: ");

        try {
            this.server.database.album_song_genre_add(album_id, song_id, genre_id);
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

    public void song_genre_delete(int album_id, int song_id, int genre_id) {
        System.out.print("Action album(" + album_id + ") song(" + song_id + ") genre(" + genre_id + ") delete");

        this.server.database.album_song_genre_remove(album_id, song_id, genre_id);
    }

    // Artists
    public void song_artist_add(int album_id, int song_id, int artist_id) throws CustomException {
        System.out.print("Action album(" + album_id + ") song(" + song_id + ") artist(" + artist_id + ") add: ");

        try {
            this.server.database.album_song_artist_add(album_id, song_id, artist_id);
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

    public void song_artist_delete(int album_id, int song_id, int artist_id) {
        System.out.print("Action album(" + album_id + ") song(" + song_id + ") artist(" + artist_id + ") delete");

        this.server.database.album_song_artist_remove(album_id, song_id, artist_id);
    }


}
