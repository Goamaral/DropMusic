package controllers;

import core.Artist;
import core.CustomException;
import services.RmiService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ArtistController extends Controller {
    ArrayList<Artist> artists = new ArrayList<>();
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
        return requestArtists();
    }

    public String search_post() {
        String result = requestArtists();

        if (result == ERROR) return ERROR;

        ArrayList<Artist> _artists = new ArrayList<>();

        for (Artist artist : artists) {
            if (artist.name.contains(query)) {
                _artists.add(artist);
            }
        }

        artists = _artists;
        return SUCCESS;
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

