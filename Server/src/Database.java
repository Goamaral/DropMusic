import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Database {
    ArrayList<User> users = new ArrayList<>();
    int next_user_id = 0;

    ArrayList<Album> albums = new ArrayList<>();
    int next_album_id = 0;

    // User
    void user_create(User user) throws CustomException {
        for (User user_i : this.users) {
            if (user_i.username.equals(user.username)) {
                throw new CustomException("Username already exists");
            }
        }

        if  (this.users.size() == 0) user.becomeEditor();

        user.id = this.next_user_id;

        this.users.add(user);
        this.next_user_id += 1;
    }

    User user_findByUsername(String username) throws CustomException {
        for (User user : this.users) {
            if (user.username.equals(username)) {
                return user;
            }
        }

        throw new CustomException("Username not found");
    }

    // Album
    ArrayList<Album> album_all() {
        return this.albums;
    }

    void album_create(Album album) {
        album.id = this.next_album_id;

        this.albums.add(album);
        this.next_album_id += 1;
    }

    Album album_find(int id) throws CustomException {
        for(Album album : this.albums) {
            if (album.id == id) return album;
        }

        throw new CustomException("Album not found");
    }

    void album_update(Album new_album) throws CustomException {
        int position = 0;

        for(Album album : this.albums) {
            if (album.id == new_album.id) break;
            position += 1;
        }

        if (position == this.albums.size()) throw new CustomException("Album not found");

        this.albums.set(position, new_album);
    }

    void album_delete(int id) {
        int position = 0;

        for(Album album : this.albums) {
            if(album.id == id) break;
            position += 1;
        }

        if(position != this.albums.size()) {
            this.albums.remove(position);
        }
    }

    // Critic
    ArrayList<Critic> album_critics(int album_id) throws CustomException {
        for(Album album : this.albums) {
            if (album.id == album_id) return album.critics;
        }

        throw new CustomException("Album not found");
    }

    void album_critic_create(Critic critic) throws CustomException {
        Album album = this.album_find(critic.album.id);
        album.points += critic.rating;
        album.addCritic(critic);
    }

    Critic album_critic_find(int album_id, int critic_pos) throws CustomException {
        try {
            Album album = this.album_find(album_id);
            return album.critics.get(critic_pos);
        } catch (CustomException ce) {
            ce.extraFlag = 1;
            throw ce;
        } catch (IndexOutOfBoundsException ioobe) {
            throw new CustomException("Critic not found");
        }
    }

    // Song
    ArrayList<Song> album_song_all(int album_id) throws CustomException {
        Album album = this.album_find(album_id);

        return album.songs;
    }

    void albums_song_create(int album_id, Song song) throws CustomException {
        Album album;

        try {
            album = this.album_find(album_id);
        } catch (CustomException ce) {
            ce.extraFlag = 1;
            throw ce;
        }

        song.id = album.songs.size();
        album.addSong(song);

    }

    void album_song_update(int album_id, int song_id, Song new_song) throws CustomException {
        Album album;

        try {
            album = this.album_find(album_id);
        } catch (CustomException ce) {
            ce.extraFlag = 1;
            throw ce;
        }

        new_song.id = song_id;
        album.songs.set(song_id, new_song);
    }

    void album_song_delete(int album_id, int song_id) throws CustomException {
        Album album = this.album_find(album_id);
        album.songs.set(song_id, null);
    }
}
