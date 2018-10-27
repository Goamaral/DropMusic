import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumController implements AlbumInterface {
    Server server;

    AlbumController(Server server) { this.server = server; }

    // Album
    public ArrayList<Album> index() {
        Object obj = null;
        ArrayList<Album> albums = null;

        String stringRecieved = this.server.dbRequest("album_all", obj);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            albums = (ArrayList<Album>) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Action album index: " + albums.size() + " albums");

        return albums;
    }

    public void create(Album album) throws CustomException {
        System.out.print("Action album create: " + album.toString());

        try {
            album.validate();
            String stringRecieved = this.server.dbRequest("album_all", album);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public Album read(int id) throws CustomException {
        Album album = null;
        System.out.print("Action album(" + id + ") read: ");

        String stringRecieved = this.server.dbRequest("album_find", id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            album = (Album) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("success");
        return album;
    }

    public void update(Album new_album) throws CustomException {
        System.out.print("Action album(" + new_album.id + ") update: " + new_album.toString());

        try {
            new_album.validate();

            String stringRecieved = this.server.dbRequest("album_update", new_album);

        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public void delete(int id) {
        System.out.println("Action album(" + id + ") delete: ");

        String stringRecieved = this.server.dbRequest("album_delete", id);

        System.out.println(" success");
    }

    public String artists(int id) {
        String artists = null;

        String stringRecieved = this.server.dbRequest("album_artists", id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            artists = (String) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("success");

        return artists;
    }

    public String genres(int id) throws CustomException {
        String genres = null;

        String stringRecieved = this.server.dbRequest("album_genres", id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            genres = (String) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("success");

        return genres;
    }

    //------------------------------------------------------------------------------------------------------------------

    // Critic
    public ArrayList<Critic> critics(int album_id) throws CustomException {

        ArrayList<Critic> critics = null;

        String stringRecieved = this.server.dbRequest("album_critics", album_id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            critics = (ArrayList<Critic>) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Action album(" + album_id +") critics: " + critics.size() + " critics");

        return critics;
    }

    public void critic_create(int album_id, Critic critic) throws CustomException {
        System.out.print("Action album critic create:");
        ArrayList<Object> obj = null;
        obj.add(album_id);
        obj.add(critic);

        try {
            critic.validate();
            String stringRecieved = this.server.dbRequest("album_critic_create", obj);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Critic critic(int critic_id) throws CustomException {
        Critic critic = null;
        System.out.print("Action critic(" + critic_id + ") read:");

        String stringRecieved = this.server.dbRequest("album_critic_find", critic_id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            critic = (Critic) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(" success");
        return critic;
    }

    //----------------------------------------------------------------------------------------------------------------
    // Song
    public ArrayList<Song> songs(int album_id) throws CustomException {
        ArrayList<Song> songs = null;

        String stringRecieved = this.server.dbRequest("album_song_all", album_id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            songs = (ArrayList<Song>) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Action album(" + album_id + ") song index: " + songs.size() + " songs");

        return songs;
    }

    public void song_create(int album_id, Song song) throws CustomException {
        ArrayList<Object> obj = null;
        obj.add(album_id);
        obj.add(song);
        System.out.print("Action album(" + album_id + ") song create:");

        try {
            song.validate();
            String stringRecieved = this.server.dbRequest("album_song_create", obj);
            System.out.println(" success");
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
    }

    public Song song(int song_id) throws CustomException {
        Song song = null;

        System.out.print("Action song(" + song_id +") read:");

        String stringRecieved = this.server.dbRequest("song_find", song_id);
        System.out.println(" success");

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            song = (Song) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return song;

    }

    public void song_update(Song new_song) throws CustomException {
        System.out.print("Action song(" + new_song.id + ") update: ");

        try {
            new_song.validate();
            String stringRecieved = this.server.dbRequest("album_song_update", new_song);
            System.out.println("success");
        } catch (CustomException ce) {
            System.out.println("failure");
            throw ce;
        }
    }

    public void song_delete(int album_id, int song_id) {
        ArrayList<Object> obj = null;
        obj.add(album_id);
        obj.add(song_id);
        System.out.println("Action album(" + album_id + ") song(" + song_id + ") delete");

        String stringRecieved = this.server.dbRequest("album_song_delete", obj);
    }

    // Genre
    public ArrayList<Genre> genres_all() {
        ArrayList<Genre> genres = null;
        Object obj = null;

        String stringRecieved = this.server.dbRequest("genre_all", obj);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            genres = (ArrayList<Genre>) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Action genres: " + genres.size() + " genres");

        return genres;
    }

    public void song_genre_add(int song_id, int genre_id) throws CustomException {
        ArrayList<Object> obj = null;
        obj.add(song_id);
        obj.add(genre_id);

        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") add: ");

        String stringRecieved = this.server.dbRequest("album_song_genre_add", obj);
        System.out.println("success");
    }

    public void song_genre_create(Genre genre) throws CustomException {
        System.out.print("Action genre create: " + genre.name + " ");

        String stringRecieved = this.server.dbRequest("genre_create", genre);
        System.out.println("success");
    }

    public ArrayList<Genre> song_genres(Song song) {
        Genre genre = null;
        System.out.print("Action song(" + song.id + ") genres: ");

        ArrayList<Genre> genres = new ArrayList<>();

        for (int genre_id : song.genres_ids) {
            String stringRecieved = this.server.dbRequest("genre_find", genre_id);

            byte stringByteRecieved[] = Base64.decode(stringRecieved);
            ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
            ObjectInputStream oIS;
            try {
                oIS = new ObjectInputStream(bAIS);
                genre = (Genre) oIS.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            genres.add(genre);
        }

        System.out.println("success");

        return genres;
    }

    public void song_genre_delete(int song_id, int genre_id) {
        ArrayList<Object> obj = null;
        obj.add(song_id);
        obj.add(genre_id);

        String stringRecieved = this.server.dbRequest("album_song_genre_remove", obj);

        System.out.print("Action song(" + song_id + ") genre(" + genre_id + ") delete");

    }

    // Artists
    public void song_artist_add(int song_id, int artist_id) throws CustomException {
        ArrayList<Object> obj = null;
        obj.add(song_id);
        obj.add(artist_id);

        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") add: ");

        String stringRecieved = this.server.dbRequest("album_song_artist_add", obj);
        System.out.println("success");
    }

    public ArrayList<Artist> song_artists(Song song) {
        Artist artist = null;
        System.out.print("Action song(" + song.id + ") artists: ");

        ArrayList<Artist> artists = new ArrayList<>();

        for (int artist_id : song.artist_ids) {
            String stringRecieved = this.server.dbRequest("artist_find", artist_id);

            byte stringByteRecieved[] = Base64.decode(stringRecieved);
            ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
            ObjectInputStream oIS;
            try {
                oIS = new ObjectInputStream(bAIS);
                artist = (Artist) oIS.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            artists.add(artist);
        }

        System.out.println("success");

        return artists;
    }

    public void song_artist_delete(int song_id, int artist_id) {
        ArrayList<Object> obj = null;
        obj.add(song_id);
        obj.add(artist_id);

        System.out.print("Action song(" + song_id + ") artist(" + artist_id + ") delete");

        String stringRecieved = this.server.dbRequest("album_song_artist_remove", obj);
    }


}
