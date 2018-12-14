package controllers;
import core.*;
import services.Service;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumController extends Controller {
    int id;
    int critic_id;
    int song_id;
    int genre_id;
    String query;

    Album album = new Album();
    Critic critic = new Critic();
    Song song;
    Genre genre;
    ArrayList<Album> albums = new ArrayList<>();
    ArrayList<Critic> critics = new ArrayList<>();

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
            Object response_object = Service.request("album_find", id);
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
            Object response_object = Service.request("album_find", id);
            Service.catchException(response_object);
            album = (Album) response_object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            errors = e.errors;
        }

        return SUCCESS;
    }

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
            Service.request("album_delete", id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    // Critic Actions
    public String critics() {
        try {
            Object response_object = Service.request("album_critics", id);
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
        return SUCCESS;
    }

    public String critic_show() {
        return SUCCESS;
    }

    // Song Actions
    public String songs() {
        return SUCCESS;
    }

    public String song_show() {
        return SUCCESS;
    }

    public String song_create() {
        return SUCCESS;
    }

    public String song_create_post() {
        return SUCCESS;
    }

    public String song_edit() {
        return SUCCESS;
    }

    public String song_edit_post() {
        return SUCCESS;
    }

    public String song_delete() {
        return SUCCESS;
    }

    public String song_upload() {
        return SUCCESS;
    }

    public String song_download() {
        return SUCCESS;
    }

    public String song_share() {
        return SUCCESS;
    }

    // Song Artist Actions
    public String song_artists() {
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
}
