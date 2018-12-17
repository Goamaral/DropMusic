package controllers;

import core.Album;
import core.Artist;
import core.CustomException;
import services.RmiService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ArtistController extends Controller {
    ArrayList<Artist> artists = new ArrayList<>();
    ArrayList<Album> albums = new ArrayList<>();
    int artist_id;
    String query;

    Artist artist = new Artist();

    public String index() {
        return requestArtists();
    }

    public String create() {
        return SUCCESS;
    }

    public String create_post() {
        try {
            RmiService.getInstance().artistInterface.create(current_user.id, artist);
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
        try {
            albums = RmiService.getInstance().artistInterface.search("");
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

    public String search_post() {
        try {
            albums = RmiService.getInstance().artistInterface.search(query);
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
        return requestArtist();
    }

    // TODO SEND NOTIFICATIONS
    public String edit_post() {
        try {
            RmiService.getInstance().artistInterface.update(current_user.id, artist);
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
            RmiService.getInstance().artistInterface.delete(artist_id);
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

    public String show() {
        return requestArtist();
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
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

    public String getQuery() {
        return query;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String requestArtists() {
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

    public String requestArtist() {
        try {
            artist = RmiService.getInstance().artistInterface.read(artist_id);
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

