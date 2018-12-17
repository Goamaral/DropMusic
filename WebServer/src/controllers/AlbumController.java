package controllers;
import core.*;
import services.RmiService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AlbumController extends Controller {
    int album_id;
    int critic_id;
    int song_id;
    int genre_id;
    int artist_id;
    String query;

    Album album = new Album();
    Critic critic = new Critic();
    Song song = new Song();
    Genre genre = new Genre();
    ArrayList<Album> albums = new ArrayList<>();
    ArrayList<Critic> critics = new ArrayList<>();
    ArrayList<Song> songs = new ArrayList<>();
    ArrayList<Artist> artists = new ArrayList<>();
    ArrayList<Genre> genres = new ArrayList<>();
    String artistsString;
    String genresString;

    // Actions
    public String index() {
        return requestAlbums();
    }

    public String show() {
        String result = requestAlbum();

        if (result == ERROR) return ERROR;

        try {
            artistsString = RmiService.getInstance().albumInterface.artists(album_id);
            genresString = RmiService.getInstance().albumInterface.genres(album_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String create() {
        return SUCCESS;
    }

    public String create_post() {
        try {
            RmiService.getInstance().albumInterface.create(current_user.id, album);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String search() {
        return index();
    }

    public String search_post() {
        try {
            albums = RmiService.getInstance().albumInterface.search(query);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String edit() {
        return requestAlbum();
    }

    public String edit_post() {
        try {
            RmiService.getInstance().albumInterface.update(current_user.id, album);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String delete() {
        try {
            RmiService.getInstance().albumInterface.delete(album_id);
            return SUCCESS;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    // Critic Actions
    public String critics() {
        try {
            critics = RmiService.getInstance().albumInterface.critics(album_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String critic_create() {
        return SUCCESS;
    }

    public String critic_create_post() {
        try {
            RmiService.getInstance().albumInterface.critic_create(album_id, critic);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String critic_show() {
        try {
            critic = RmiService.getInstance().albumInterface.critic(critic_id);
            critic.album = RmiService.getInstance().albumInterface.read(album_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    // Song Actions
    public String songs() {
        try {
            songs = RmiService.getInstance().albumInterface.songs(album_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_show() {
        return requestSong();
    }

    public String song_create() {
        return SUCCESS;
    }

    public String song_create_post() {
        try {
            RmiService.getInstance().albumInterface.song_create(album_id, song);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_edit() {
        return song_show();
    }

    public String song_edit_post() {
        try {
            RmiService.getInstance().albumInterface.song_update(song);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_delete() {
        try {
            RmiService.getInstance().albumInterface.song_delete(album_id, song_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    // TODO NOT IMPLEMENTED
    public String song_upload() {
        return SUCCESS;
    }

    // TODO NOT IMPLEMENTED
    public String song_download() {
        return SUCCESS;
    }

    // TODO NOT IMPLEMENTED
    public String song_share() {
        return SUCCESS;
    }

    // Song Artist Actions
    public String song_artists() {
        return requestSong();
    }

    public String song_artist_add() {
        try {
            artists = RmiService.getInstance().artistInterface.index();
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_artist_add_post() {
        try {
            RmiService.getInstance().albumInterface.song_artist_add(song_id, artist_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_artist_remove() {
        try {
            artists = RmiService.getInstance().albumInterface.song_artists(song);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_artist_remove_post() {
        try {
            RmiService.getInstance().albumInterface.song_artist_delete(song_id, artist_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    // Song Genre Actions
    public String song_genres() {
        return requestSong();
    }

    public String song_genre_add() {
        try {
            genres = RmiService.getInstance().albumInterface.genres_all();
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_genre_add_post() {
        try {
            RmiService.getInstance().albumInterface.song_genre_add(song_id, genre_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_genre_remove() {
        try {
            genres = RmiService.getInstance().albumInterface.song_genres(song);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_genre_remove_post() {
        try {
            RmiService.getInstance().albumInterface.song_genre_delete(song_id, genre_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String song_genre_create() {
        return SUCCESS;
    }

    public String song_genre_create_post() {
        try {
            RmiService.getInstance().albumInterface.song_genre_create(genre);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    // Accessors
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<Critic> getCritics() {
        return critics;
    }

    public void setCritic(Critic critic) {
        this.critic = critic;
    }

    public Critic getCritic() {
        return critic;
    }

    public void setCritic_id(int critic_id) {
        this.critic_id = critic_id;
    }

    public int getCritic_id() {
        return critic_id;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public String getQuery() {
        return query;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public void setCritics(ArrayList<Critic> critics) {
        this.critics = critics;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public String getArtistsString() {
        return artistsString;
    }

    public void setArtistsString(String artistsString) {
        this.artistsString = artistsString;
    }

    public String getGenresString() {
        return genresString;
    }

    public void setGenresString(String genresString) {
        this.genresString = genresString;
    }

    // Requests
    public String requestSong() {
        try {
            song = RmiService.getInstance().albumInterface.song(song_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String requestAlbum() {
        try {
            album = RmiService.getInstance().albumInterface.read(album_id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String requestAlbums() {
        try {
            albums = RmiService.getInstance().albumInterface.index();
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }
}
