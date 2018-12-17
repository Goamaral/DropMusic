package controllers;

import core.Artist;
import core.CustomException;
import services.RmiService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ArtistController extends Controller {
    ArrayList<Artist> artists = new ArrayList<>();

    Artist artist = new Artist();

    public String index() {
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

    public String artist_create() {
        return SUCCESS;
    }

    public String artist_create_post() {
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

    public String artist_search() {
        return SUCCESS;
    }

    public String artist_search_post() {
        return SUCCESS;
    }
}
