import java.util.ArrayList;

public class Database {
    ArrayList<User> users = new ArrayList<>();

    ArrayList<Album> albums = new ArrayList<>();

    ArrayList<Artist> artists = new ArrayList<>();

    // User
    void user_create(User user) throws CustomException {
        Index_SameObject result = this.user_exists(user);

        if (result.index == -1) {
            if (this.users.size() == 0) user.becomeEditor();

            user.id = this.users.size();
            this.users.add(user);
        } else {
            throw new CustomException("Username already exists");
        }
    }

    Index_SameObject user_exists(User new_user) {
        int index = 0;

        for (User user : this.users) {
            if (user.username.equals(new_user.username)) {
                return new Index_SameObject(index, user.id == new_user.id);
            }

            index += 1;
        }

        return new Index_SameObject(-1, false);
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

    void album_create(Album album) throws CustomException {
        album.id = this.albums.size();
        this.albums.add(album);
    }

    Index_SameObject album_exists(Album new_album) {
        int index = 0;

        for (Album album : this.albums) {
            if (album.name.equals(new_album.name)) return new Index_SameObject(index, album.id == new_album.id);
            index += 1;
        }

        return new Index_SameObject(-1, false);
    }

    Album album_find(int id) throws CustomException {
        for(Album album : this.albums) {
            if (album.id == id) return album;
        }

        throw new CustomException("Album not found");
    }

    void album_update(Album new_album) throws CustomException {
        Index_SameObject result = this.album_exists(new_album);

        if (result.sameObject || result.index == -1) {
            this.albums.set(new_album.id, new_album);
        } else {
            throw new CustomException("Album not found");
        }
    }

    void album_delete(int id) {
        if(id < this.albums.size()) this.albums.set(id, null);
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
            if (this.album_song_exists(album_id, song).index != -1) {
                try {
                    album = this.album_find(album_id);
                } catch (CustomException ce) {
                    ce.extraFlag = 1;
                    throw ce;
                }

                song.id = album.songs.size();
                album.addSong(song);
            } else {
                throw new CustomException("Song name already exists in album");
            }
        } catch (CustomException ce) {
            ce.extraFlag = 1;
            throw ce;
        }
    }

    Index_SameObject album_song_exists(int album_id, Song new_song) throws CustomException {
        Album album = this.album_find(album_id);
        int index = 0;

        for (Song song : album.songs) {
            if (song.name.equals(new_song.name)) return new Index_SameObject(index, song.id == new_song.id);
            index += 1;
        }

        return new Index_SameObject(-1, false);
    }

    void album_song_update(int album_id, Song new_song) throws CustomException {
        Album album;
        Index_SameObject result = this.album_song_exists(album_id, new_song);

        if (result.sameObject || result.index == -1) {
            try {
                album = this.album_find(album_id);
            } catch (CustomException ce) {
                ce.extraFlag = 1;
                throw ce;
            }

            album.songs.set(new_song.id, new_song);
        } else if (result.index != -1) {
            throw new CustomException("Song name already exists in album");
        } else {
            throw new CustomException("Song not found");
        }

    }

    void album_song_delete(int album_id, int song_id) throws CustomException {
        Album album = this.album_find(album_id);
        album.songs.set(song_id, null);
    }

    Song album_song_find(int album_id, int song_id) throws CustomException {
        Album album;

        try {
            album = this.album_find(album_id);
        } catch (CustomException ce) {
            ce.extraFlag = 1;
            throw ce;
        }

        for (Song song : album.songs) {
            if (song == null) continue;
            if (song.id == song_id) return song;
        }

        throw new CustomException("Song not found");
    }

    // Artist
    ArrayList<Artist> artist_all() { return this.artists; }

    void artist_create(Artist artist) throws CustomException {
        Index_SameObject result = this.artist_exists(artist);

        if (result.index == -1) {
            artist.id = this.artists.size();
            this.artists.add(artist);
        } else {
            throw new CustomException("Artist already exists");
        }
    }

    Index_SameObject artist_exists(Artist new_artist) {
        int index = 0;

        for (Artist artist : this.artists) {
            if (artist.name.equals(new_artist.name)) {
                return new Index_SameObject(index, artist.id == new_artist.id);
            }
            index += 1;
        }

        return new Index_SameObject(-1, false);
    }

    Artist artist_find(int id) throws CustomException {
        for(Artist artist : this.artists) {
            if (artist.id == id) return artist;
        }

        throw new CustomException("Artist not found");
    }

    void artist_update(Artist new_artist) throws CustomException {
        Index_SameObject result = this.artist_exists(new_artist);

        if (result.sameObject || result.index == -1) {
            this.artists.set(new_artist.id, new_artist);
        } else {
            throw new CustomException("Artist name already exists");
        }
    }
}

class Index_SameObject {
    int index;
    boolean sameObject;

    Index_SameObject(int index, boolean sameObject) {
        this.index = index;
        this.sameObject = sameObject;
    }
}