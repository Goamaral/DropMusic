import java.io.*;
import java.net.*;
import java.util.ArrayList;
import core.*;

public class Database {
    ArrayList<User> users = new ArrayList<>();
    int next_user_id = 0;

    ArrayList<Album> albums = new ArrayList<>();
    int next_album_id = 0;

    ArrayList<Song> songs = new ArrayList<>();
    int next_song_id = 0;

    ArrayList<Artist> artists = new ArrayList<>();
    int next_artist_id = 0;

    ArrayList<Genre> genres = new ArrayList<>();
    int next_genre_id = 0;

    ArrayList<Critic> critics = new ArrayList<>();
    int next_critic_id = 0;

    ArrayList<StoredSong> storedSongs = new ArrayList<StoredSong>();
    int next_stored_song_id = 0;

    // User
    int user_findIndexUsername(User new_user) {
        for (User user : this.users) {
            if (user.username.equals(new_user.username)) {
                return this.users.indexOf(user);
            }
        }

        return -1;
    }

    User user_findByUid(String uid) throws CustomException {
        for (User user : this.users) {
            if (user.getUid() != null) {
                if (user.getUid().equals(uid)) {
                    return user;
                }
            }
        }

        throw new CustomException("User not found");
    }

    void user_setUid(Integer user_id, String uid) {
        for (User user : this.users) {
            if (user.id == user_id) {
                user.setUid(uid);
                break;
            }
        }
    }


    Boolean user_create(User user) throws CustomException {
        int index = this.user_findIndexUsername(user);

        if (index == -1) {
            if (this.users.size() == 0) user.becomeEditor();

            user.id = this.next_user_id;
            this.next_user_id += 1;
            this.users.add(user);
        } else {
            throw new CustomException("Username already exists");
        }

        return true;
    }

    User user_findByUsername(String username) throws CustomException {
        for (User user : this.users) {
            if (user.username.equals(username)) {
                return user;
            }
        }

        throw new CustomException("Username not found");
    }

    ArrayList<User> normal_users() {
        ArrayList<User> normal_users = new ArrayList<>();

        for (User user : this.users) {
            if (!user.isEditor) normal_users.add(user);
        }

        return normal_users;
    }

    User user_find(int id) throws CustomException {
        for (User user : this.users) {
            if (user.id == id) return user;
        }

        throw new CustomException("User not found");
    }

    void user_promote(int id) throws CustomException {
        User user = this.user_find(id);
        user.becomeEditor();
    }

    ArrayList<User> user_all() { return this.users; }

    // Album
    ArrayList<Album> album_all() {
        return this.albums;
    }

    Album album_create(int user_id, Album album) {
        album.editor_ids.add(user_id);
        album.id = this.next_album_id;
        this.next_album_id += 1;
        this.albums.add(album);
        return album;
    }

    Album album_find(int id) throws CustomException {
        for(Album album : this.albums) {
            if (album.id == id) return album;
        }

        throw new CustomException("Album not found");
    }

    int album_findIndex(Album new_album) {
        for (Album album : this.albums) {
            if (album.id == new_album.id) {
                return this.albums.indexOf(album);
            }
        }

        return -1;
    }

    ArrayList<Integer> album_update(int user_id, Album new_album) throws CustomException {
        int index = this.album_findIndex(new_album);

        if (index != -1) {
            this.albums.get(index).name = new_album.name;
            this.albums.get(index).info = new_album.info;
            this.albums.get(index).releaseDateString = new_album.releaseDateString;
            ArrayList<Integer> author_ids = (ArrayList<Integer>)this.albums.get(index).editor_ids.clone();
            if (!this.albums.get(index).editor_ids.contains(user_id)) {
                this.albums.get(index).editor_ids.add(user_id);
            }
            return author_ids;
        } else {
            throw new CustomException("Album not found");
            //return new ArrayList<Integer>();
        }
    }

    void album_delete(int id) {
        Album album;
        try {
            album = this.album_find(id);
            this.albums.remove(album);
        } catch (CustomException e) {
            // Return if album not found
            return;
        }
    }

    String album_artists(int id) throws CustomException {
        Album album = this.album_find(id);
        ArrayList<Integer> artist_ids = new ArrayList<>();
        String artists = "";
        Artist artist;

        for (int song_id : album.song_ids) {
            Song song;

            try {
                song = this.song_find(song_id);
            } catch (CustomException ce) {
                // Skip song if not found
                continue;
            }

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

        for (int song_id : album.song_ids) {
            Song song;

            try {
                song = this.song_find(song_id);
            } catch (CustomException ce) {
                continue;
            }

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
        ArrayList<Critic> critics = new ArrayList<>();
        Album album = this.album_find(album_id);

        for(int critic_id : album.critic_ids) {
            try {
                critics.add(this.album_critic_find(critic_id));
            } catch (CustomException ce) {
                // ignore if critic not found
            }
        }

        return critics;
    }

    void album_critic_create(int album_id, Critic critic) throws CustomException {
        Album album = this.album_find(album_id);
        album.points += critic.rating;
        critic.id = this.next_critic_id;
        album.addCritic(critic);
        this.critics.add(critic);
        this.next_critic_id += 1;
    }

    Critic album_critic_find(int id) throws CustomException {
        for (Critic critic : this.critics) {
            if (critic.id == id) {
                return critic;
            }
        }

        throw new CustomException("Critic not found");
    }

    // Song
    ArrayList<Song> album_song_all(int album_id) throws CustomException {
        Album album = this.album_find(album_id);
        ArrayList<Song> songs = new ArrayList<>();
        Song song;

        for (int song_id : album.song_ids) {
            try {
                song = this.song_find(song_id);
                songs.add(song);
            } catch (CustomException ce) {
                // ignore if song not found
            }
        }

        return songs;
    }

    Boolean album_song_create(int album_id, Song song) throws CustomException {
        Album album;

        try {
            album = this.album_find(album_id);
        } catch (CustomException ce) {
            ce.extraFlag += 1;
            throw ce;
        }

        song.id = this.next_song_id;
        song.album_id = album_id;
        this.songs.add(song);
        this.next_song_id += 1;
        album.addSong(song.id);
        return true;
    }

    int song_findIndex(Song new_song) {
        for (Song song : this.songs) {
            if (song.id == new_song.id) {
                return this.songs.indexOf(song);
            }
        }

        return -1;
    }

    int song_findIndexByName(Song new_song) {
        for (Song song : this.songs) {
            if (song.name.equals(new_song.name)) {
                return this.songs.indexOf(song);
            }
        }

        return -1;
    }

    void album_song_update(Song new_song) throws CustomException {
        int index = this.song_findIndex(new_song);
        int index2 = this.song_findIndexByName(new_song);

        if (index != -1 || new_song.id == this.songs.get(index2).id) {
            this.songs.get(index).name = new_song.name;
            this.songs.get(index).info = new_song.info;
        } else {
            throw new CustomException("Song not found");
        }
    }

    Boolean album_song_delete(int album_id, int song_id) {
        try {
            this.album_find(album_id).removeSong(song_id);
        } catch (CustomException ce) {
            // ignore if album not found
        }

        try {
            this.songs.remove(this.song_find(song_id));
        } catch (CustomException ce) {
            // ignore if song not found
        }
        return true;
    }

    Song song_find(int song_id) throws CustomException {
        for (Song song : this.songs) {
            if (song.id == song_id) return song;
        }

        throw new CustomException("Song not found");
    }

    int requestSongUpload(int user_id, int song_id, String ext) throws CustomException {
        ServerSocket serverSocket;
        int port = 15000;

        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                break;
            } catch (IOException e) {
                port += 1;
            }
        }

        int stored_song_id = this.next_stored_song_id;
        this.next_stored_song_id += 1;
        new UploadThread(new StoredSong(stored_song_id, user_id, song_id, ext), serverSocket, this);

        User user = this.user_find(user_id);
        user.addStoredSong(stored_song_id);

        user = this.user_find(user_id);

        System.out.println("SONGS: " + user.stored_song_ids.size());


        return port;
    }

    StoredSong stored_song_find(int id) throws CustomException {
        for (StoredSong storedSong : this.storedSongs) {
            if (storedSong.id == id) {
                return storedSong;
            }
        }

        throw new CustomException("Stored song not found");
    }

    ArrayList<StoredSong> song_downloads(int song_id, int user_id) throws CustomException {
        ArrayList<StoredSong> downloads = new ArrayList<>();
        User user = this.user_find(user_id);

        for (int stored_song_id : user.stored_song_ids) {
            try {
                StoredSong storedSong = this.stored_song_find(stored_song_id);
                if (storedSong.song_id == song_id) {
                    downloads.add(storedSong);
                }
            } catch (CustomException ce) {
                // ignore if storesong is not found
            }
        }

        return downloads;
    }

    int requestSongDownload(int user_id, int stored_song_id) throws CustomException {
        ServerSocket serverSocket;
        int port = 15000;

        User user = this.user_find(user_id);
        StoredSong storedSong = this.stored_song_find(stored_song_id);

        if (!user.stored_song_ids.contains(stored_song_id)) {
            throw new CustomException("Download not allowed");
        }

        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                break;
            } catch (IOException e) {
                port += 1;
            }
        }

        new DownloadThread(storedSong, serverSocket);

        return port;
    }

    ArrayList<StoredSong> user_uploads(int user_id, int song_id) {
        ArrayList<StoredSong> user_uploads = new ArrayList<>();

        for (StoredSong storedSong : this.storedSongs) {
            if (storedSong.uploader_id == user_id && storedSong.song_id == song_id) {
                user_uploads.add(storedSong);
            }
        }

        return user_uploads;
    }

    void song_share(int stored_song_id, int user_id) throws CustomException {
        User user = this.user_find(user_id);

        if (!user.stored_song_ids.contains(stored_song_id)) {
            user.stored_song_ids.add(stored_song_id);
        }
    }

    // Genre
    ArrayList<Genre> genre_all() { return this.genres; }

    Genre genre_find(int id) throws CustomException {
        for (Genre genre : this.genres) {
            if (genre.id == id) return genre;
        }

        throw new CustomException("Genre not found");
    }

    Boolean genre_create(Genre genre) throws CustomException {
        int index = this.genre_findIndexByName(genre);

        if (index == -1) {
            genre.id = next_genre_id;
            this.genres.add(genre);
            next_genre_id += 1;
        } else {
            throw new CustomException("Genre already exists");
        }
        return true;
    }

    int genre_findIndexByName(Genre new_genre) {
        for (Genre genre : this.genres) {
            if (genre.name.equals(new_genre.name)) {
                return this.genres.indexOf(genre);
            }
        }

        return -1;
    }

    // Artist
    ArrayList<Artist> artist_all() { return this.artists; }

    int artist_findIndex(Artist new_artist) {
        for (Artist artist : this.artists) {
            if (artist.id == new_artist.id) {
                return this.artists.indexOf(artist);
            }
        }

        return -1;
    }

    int artist_findIndexByName(Artist new_artist) {
        for (Artist artist : this.artists) {
            if (artist.name.equals(new_artist.name)) {
                return this.artists.indexOf(artist);
            }
        }

        return -1;
    }

    void artist_create(int user_id, Artist artist) throws CustomException {
        int index = this.artist_findIndexByName(artist);

        if (index == -1) {
            artist.editor_ids.add(user_id);
            artist.id = this.next_artist_id;
            this.artists.add(artist);
            this.next_artist_id += 1;
        } else {
            throw new CustomException("Artist already exists");
        }
    }

    Artist artist_find(int id) throws CustomException {
        for(Artist artist : this.artists) {
            if (artist.id == id) return artist;
        }

        throw new CustomException("Artist not found");
    }

    ArrayList<Integer> artist_update(int user_id, Artist new_artist) throws CustomException {
        int index = this.artist_findIndex(new_artist);
        int index2 = this.artist_findIndexByName(new_artist);

        if (index != -1 || new_artist.id == this.artists.get(index2).id) {
            this.artists.get(index).name = new_artist.name;
            this.artists.get(index).info = new_artist.info;

            ArrayList<Integer> author_ids = (ArrayList<Integer>) this.artists.get(index).editor_ids.clone();
            if (!this.artists.get(index).editor_ids.contains(user_id)) {
                this.artists.get(index).editor_ids.add(user_id);
            }

            return author_ids;
        } else {
            throw new CustomException("Artist name already exists");
        }
    }

    Boolean artist_delete(int id) {
        Artist artist;

        try {
            artist = this.artist_find(id);
            this.artists.remove(artist);
        } catch (CustomException ce) {
            // Ignore if artist doesnt exist
        }
        return true;
    }

    ArrayList<Song> artist_songs(int id) throws CustomException {
        Artist artist = this.artist_find(id);
        ArrayList<Song> songs = new ArrayList<>();

        for (int song_id : artist.song_ids) {
            try {
                songs.add(this.song_find(song_id));
            } catch (CustomException ce) {
                // ignore if not found
            }
        }

        return songs;
    }

    Genre album_song_genre_add(int song_id, int genre_id) throws CustomException {
        Song song;
        Genre genre = this.genre_find(genre_id);

        try {
            song = this.song_find(song_id);
        } catch (CustomException ce) {
            ce.extraFlag += 1;
            throw ce;
        }

        song.addGenre(genre);
        return genre;
    }

    Boolean album_song_genre_remove(int song_id, int genre_id) {
        Song song;
        Genre genre;

        try {
            song = this.song_find(song_id);
            genre = this.genre_find(genre_id);
            song.removeGenre(genre);
        } catch (CustomException ce) {
            // if song, album or genre not found ignore
        }
        return true;
    }

    // Song Artists
    void album_song_artist_add(int song_id, int artist_id) throws CustomException {
        Song song;
        Artist artist = this.artist_find(artist_id);

        try {
            song = this.song_find(song_id);
        } catch (CustomException ce) {
            ce.extraFlag += 1;
            throw ce;
        }

        song.addArtist(artist);
        artist.addSong(song);
    }

    void album_song_artist_remove(int song_id, int artist_id) {
        Song song;
        Artist artist;

        try {
            song = this.song_find(song_id);
            artist = this.artist_find(artist_id);
            song.removeArtist(artist);
            artist.removeSong(song);
        } catch (CustomException ce) {
            // if song, album or genre not found ignore
        }
    }
}

class UploadThread extends Thread {
    StoredSong storedSong;
    Database database;
    ServerSocket serverSocket;

    UploadThread(StoredSong storedSong, ServerSocket serverSocket, Database database) {
        this.storedSong = storedSong;
        this.database = database;
        this.serverSocket = serverSocket;
        this.start();
    }

    public void run() {
        try {
            Socket client = this.serverSocket.accept();
            byte[] file_bytes = new byte[10000];
            FileOutputStream fos = new FileOutputStream("u_song_" + this.storedSong.id + "." + this.storedSong.ext);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = client.getInputStream();

            int bytes_read = 0;

            while((bytes_read = is.read(file_bytes))!=-1)
                bos.write(file_bytes, 0, bytes_read);

            bos.flush();
            client.close();
            this.serverSocket.close();

            this.database.storedSongs.add(storedSong);
        } catch (IOException e) {
            /* Wait for server to reconnect */
        }
    }
}

class DownloadThread extends Thread {
    StoredSong storedSong;
    ServerSocket serverSocket;

    DownloadThread(StoredSong storedSong, ServerSocket serverSocket) {
        this.storedSong = storedSong;
        this.serverSocket = serverSocket;
        this.start();
    }

    public void run() {
        try {
            Socket client = this.serverSocket.accept();

            File file = new File("u_song_" + this.storedSong.id + "." + this.storedSong.ext);
            FileInputStream fis = new FileInputStream(file);

            OutputStream os = client.getOutputStream();

            BufferedInputStream bis = new BufferedInputStream(fis);

            byte[] file_bytes;
            long file_length = file.length();
            long sent_bytes = 0;

            while (sent_bytes != file_length) {
                int packet_size = 10000;
                long bytes_left = file_length - sent_bytes;
                if (bytes_left >= packet_size) {
                    sent_bytes += packet_size;
                } else {
                    packet_size = (int) bytes_left;
                    sent_bytes += packet_size;
                }

                file_bytes = new byte[packet_size];
                bis.read(file_bytes, 0, packet_size);
                os.write(file_bytes);
                System.out.println("\r" + (sent_bytes * 100) / file_length + "% complete!");
            }

            os.flush();
            client.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}