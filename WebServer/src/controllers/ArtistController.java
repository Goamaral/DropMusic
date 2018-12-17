package controllers;

import core.Artist;
import core.CustomException;
import core.Song;
import services.Service;

import java.io.IOException;
import java.util.ArrayList;

public class ArtistController extends Controller {
    ArrayList<Artist> artists = new ArrayList<>();

    Artist artist = new Artist();

    // TODO TEST
    public String index() {
        try {
            Object response_object = Service.request("artist_all", true);
            Service.catchException(response_object);
            artists = (ArrayList<Artist>) response_object;
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    // TODO TEST
    public String artist_create() {
        return SUCCESS;
    }

    // TODO TEST
    public String artist_create_post() {
        ArrayList<Object> args = new ArrayList<>();
        args.add(current_user.id);
        args.add(artist);

        try {
            artist.validate();
            Object response_object = Service.request("artist_create", args);
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

    public String artist_search() {
        return SUCCESS;
    }

    public String artist_search_post() {
        return SUCCESS;
    }
}
