package controllers;

import core.*;
import services.Service;

import java.io.IOException;
import java.util.ArrayList;

public class ArtistController extends Controller {
    ArrayList<Artist> artists = new ArrayList<>();
    int artist_id;
    String query;

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

    public String create() {
        return SUCCESS;
    }

    public String create_post() {
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



    public String search() {
        return index();
    }

    public String search_post() {
        try {
            Object response_object = Service.request("artist_all", true);
            Service.catchException(response_object);
            ArrayList<Artist> _artists = (ArrayList<Artist>) response_object;
            artists = new ArrayList<>();

            for (Artist artist : _artists) {
                if (artist.name.contains(query)) {
                    artists.add(artist);
                }
            }

            return SUCCESS;
        } catch (CustomException | IOException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit() {
        try {
            Object response_object = Service.request("artist_find", artist_id);
            Service.catchException(response_object);
            artist = (Artist) response_object;
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
            artist.validate();
            ArrayList<Object> args = new ArrayList<>();
            args.add(current_user.id);
            args.add(artist);

            Object response_object = Service.request("artist_update", args);
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
            Service.request("artist_delete", artist_id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public String show() {
        try {
            Object response_object = Service.request("artist_find", artist_id);
            Service.catchException(response_object);
            artist = (Artist) response_object;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }

        return SUCCESS;
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

}

