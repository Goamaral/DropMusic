package controllers;
import core.*;
import services.Service;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumController extends Controller {
    int album_id;
    int critic_id;
    int song_id;
    int genre_id;
    int user_id;
    String query;

    Album album = new Album();
    Critic critic = new Critic();
    Song song = new Song();
    Genre genre = new Genre();
    ArrayList<Album> albums = new ArrayList<>();
    ArrayList<Critic> critics = new ArrayList<>();
    ArrayList<Song> songs = new ArrayList<>();
    ArrayList<Artist> artists = new ArrayList<>();

    // Actions
    public String index() {
        try {
            Object response_object = Service.request("album_all", true);
            Service.catchException(response_object);
            albums = (ArrayList<Album>) response_object;
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String show() {
        try {
            Object response_object = Service.request("album_find", album_id);
            Service.catchException(response_object);
            album = (Album) response_object;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }

        return SUCCESS;
    }

    public String create() {
        return SUCCESS;
    }

    public String create_post() {
        ArrayList<Object> args = new ArrayList<>();
        args.add(current_user.id);
        args.add(album);

        try {
            album.validate();
            Object response_object = Service.request("album_create", args);
            Service.catchException(response_object);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String search() {
        return index();
    }

    public String search_post() {
        try {
            Object response_object = Service.request("album_all", true);
            Service.catchException(response_object);
            ArrayList<Album> _albums = (ArrayList<Album>) response_object;
            albums = new ArrayList<>();

            for (Album album : _albums) {
                if (album.name.contains(query)) {
                    albums.add(album);
                }
            }

            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String edit() {
        try {
            Object response_object = Service.request("album_find", album_id);
            Service.catchException(response_object);
            album = (Album) response_object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            errors = e.errors;
        }

        return SUCCESS;
    }

    // TODO SEND NOTIFICATIONS
    public String edit_post() {
        try {
            album.validate();
            ArrayList<Object> args = new ArrayList<>();
            args.add(current_user.id);
            args.add(album);

            Object response_object = Service.request("album_update", args);
            Service.catchException(response_object);

             /*
            for (int editor_id : (ArrayList<Integer>) response_object) {
                this.server.send_notifications(new Job(editor_id, "Album " + new_album.id + " was edited"));
            }
            */
             return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String delete() {
        try {
            Service.request("album_delete", album_id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    // Critic Actions
    public String critics() {
        try {
            Object response_object = Service.request("album_critics", album_id);
            Service.catchException(response_object);
            critics = (ArrayList<Critic>) response_object;
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String critic_create() {
        return SUCCESS;
    }

    public String critic_create_post() {
        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(critic);

        try {
            critic.validate();
            Object response_object = Service.request("album_critic_create", args);
            Service.catchException(response_object);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String critic_show() {
        try {
            Object response_object = Service.request("album_critic_find", critic_id);
            Service.catchException(response_object);
            critic = (Critic) response_object;
            response_object = Service.request("album_find", album_id);
            Service.catchException(response_object);
            critic.album = (Album) response_object;
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    // Song Actions
    public String songs() {
        try {
            Object response_object = Service.request("album_song_all", album_id);
            Service.catchException(response_object);
            songs = (ArrayList<Song>) response_object;
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String song_show() {
        try {
            Object response_object = Service.request("song_find", song_id);
            Service.catchException(response_object);
            song = (Song) response_object;
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String song_create() {
        return SUCCESS;
    }

    public String song_create_post() {
        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(song);

        try {
            song.validate();
            Object response_object = Service.request("album_song_create", args);
            Service.catchException(response_object);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String song_edit() {
        return song_show();
    }

    public String song_edit_post() {
        try {
            song_id = song.id;
            song.validate();
            Object response_object = Service.request("album_song_update", song);
            Service.catchException(response_object);
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String song_delete() {
        ArrayList<Object> args = new ArrayList<>();
        args.add(album_id);
        args.add(song_id);

        try {
            Service.request("album_song_delete", args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SUCCESS;
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
        artists = new ArrayList<>();

        for (int artist_id : song.artist_ids) {
            try {
                Object response_object = Service.request("artist_find", artist_id);
                Service.catchException(response_object);
                artists.add((Artist) response_object);
            } catch (IOException e) {
                e.printStackTrace();
                return SUCCESS;
            } catch (CustomException e) {
                errors = e.errors;
                return ERROR;
            }
        }

        return SUCCESS;
    }

    // Song Genre Actions
    public String song_genres() {
        return SUCCESS;
    }

    public String song_genre_add() {
        return SUCCESS;
    }

    public String song_genre_add_post() {
        return SUCCESS;
    }

    public String song_genre_remove() {
        return SUCCESS;
    }

    public String song_genre_remove_post() {
        return SUCCESS;
    }

    public String song_genre_create() {
        return SUCCESS;
    }

    public String song_genre_create_post() {
        return SUCCESS;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }
}
