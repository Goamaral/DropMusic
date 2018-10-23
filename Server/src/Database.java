import java.util.ArrayList;

public class Database {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Album> albums = new ArrayList<>();
    ArrayList<Artist> artists = new ArrayList<>();
    ArrayList<Genre> genres = new ArrayList<>();

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
            if (user == null) continue;

            if (user.username.equals(new_user.username)) {
                return new Index_SameObject(index, user.id == new_user.id);
            }

            index += 1;
        }

        return new Index_SameObject(-1, false);
    }

    User user_findByUsername(String username) throws CustomException {
        for (User user : this.users) {
            if (user == null) continue;

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
            if (album == null) continue;

            if (album.name.equals(new_album.name)) return new Index_SameObject(index, album.id == new_album.id);
            index += 1;
        }

        return new Index_SameObject(-1, false);
    }

    Album album_find(int id) throws CustomException {
        for(Album album : this.albums) {
            if (album == null) continue;

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

    String album_artists(int id) throws CustomException {
        Album album = this.album_find(id);
        ArrayList<Integer> artist_ids = new ArrayList<>();
        String artists = "";
        Artist artist;

        for (Song song : album.songs) {
            for (int artist_id : song.artist_ids) {
                if (!artist_ids.contains(artist_id)) {
                    artist_ids.add(artist_id);
                }
            }
        }

        for (int artist_id : artist_ids) {
            try {
                artist = this.artist_find(artist_id);

                if (artists.length() == 0) {
                    artists = artist.name;
                } else {
                    artists += ", " + artist.name;
                }
            } catch (CustomException ce) {
                // Ignore if artist is missing
            }
        }

        return artists;
    }

    String album_genres(int id) throws CustomException {
        Album album = this.album_find(id);
        ArrayList<Integer> genre_ids = new ArrayList<>();
        String genres = "";
        Genre genre;

        for (Song song : album.songs) {
            for (int genre_id : song.genres_ids) {
                if (!genre_ids.contains(genre_id)) {
                    genre_ids.add(genre_id);
                }
            }
        }

        for (int genre_id : genre_ids) {
            try {
                genre = this.genre_find(genre_id);

                if (genres.length() == 0) {
                    genres = genre.name;
                } else {
                    genres += ", " + genre.name;
                }
            } catch (CustomException ce) {
                // Ignore if genre is missing
            }
        }

        return genres;
    }

    // Critic
    ArrayList<Critic> album_critics(int album_id) throws CustomException {
        for(Album album : this.albums) {
            if (album == null) continue;

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
        Index_SameObject result = this.album_song_exists(album_id, song);

        try {
            if (result.index == -1) {
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
            if (song == null) continue;

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
        album.removeSong(song_id);
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

    // Genre
    ArrayList<Genre> genre_all() { return this.genres; }

    Genre genre_find(int id) throws CustomException {
        for (Genre genre : this.genres) {
            if (genre.id == id) return genre;
        }

        throw new CustomException("Genre not found");
    }

    void genre_create(Genre genre) throws CustomException {
        Index_SameObject result = this.genre_exists(genre);

        if (result.index == -1) {
            genre.id = this.genres.size();
            this.genres.add(genre);
        } else {
            throw new CustomException("Genre already exists");
        }
    }

    Index_SameObject genre_exists(Genre new_genre) {
        int index = 0;

        for (Genre genre : this.genres) {
            if (genre == null) continue;

            if (genre.name.equals(new_genre.name)) {
                return new Index_SameObject(index, new_genre.id == new_genre.id);
            }
            index += 1;
        }

        return new Index_SameObject(-1, false);
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
            if (artist == null) continue;

            if (artist.name.equals(new_artist.name)) {
                return new Index_SameObject(index, artist.id == new_artist.id);
            }
            index += 1;
        }

        return new Index_SameObject(-1, false);
    }

    Artist artist_find(int id) throws CustomException {
        for(Artist artist : this.artists) {
            if (artist == null) continue;

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

    void artist_delete(int id) {
        if(id < this.artists.size()) this.artists.set(id, null);
    }

    void album_song_genre_add(int album_id, int song_id, int genre_id) throws CustomException {
        Song song;
        Genre genre = this.genre_find(genre_id);

        try {
            song = this.album_song_find(album_id, song_id);
        } catch (CustomException ce) {
            ce.extraFlag += 1;
            throw ce;
        }

        song.addGenre(genre);
    }

    void album_song_genre_remove(int album_id, int song_id, int genre_id) {
        Song song;
        Genre genre;

        try {
            song = this.album_song_find(album_id, song_id);
            genre = this.genre_find(genre_id);
            song.removeGenre(genre);
        } catch (CustomException ce) {
            // if song, album or genre not found ignore
        }
    }

    // Song Artists
    void album_song_artist_add(int album_id, int song_id, int artist_id) throws CustomException {
        Song song;
        Artist artist = this.artist_find(artist_id);

        try {
            song = this.album_song_find(album_id, song_id);
        } catch (CustomException ce) {
            ce.extraFlag += 1;
            throw ce;
        }

        song.addArtist(artist);
        artist.addSong(song);
    }

    void album_song_artist_remove(int album_id, int song_id, int artist_id) {
        Song song;
        Artist artist;

        try {
            song = this.album_song_find(album_id, song_id);
            artist = this.artist_find(artist_id);
            song.removeArtist(artist);
            artist.removeSong(song);
        } catch (CustomException ce) {
            // if song, album or genre not found ignore
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