package controllers;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import core.Album;
import core.Critic;
import core.Genre;
import core.Song;
import services.Service;

public class AlbumController extends ActionSupport implements Preparable {
    int id;
    Album album;

    int critic_id;
    Critic critic;

    int song_id;
    Song song;

    int genre_id;
    Genre genre;

    Service service;

    @Override
    public void prepare() throws InterruptedException {
        // Auth service
    }

    // Actions
    public String index() {
        return SUCCESS;
    }

    public String show() {
        return SUCCESS;
    }

    public String create() {
        return SUCCESS;
    }

    public String create_post() {
        return SUCCESS;
    }

    public String search() { return SUCCESS; }

    public String edit() { return SUCCESS; }

    public String edit_post() {
        return SUCCESS;
    }

    public String delete() {
        return SUCCESS;
    }

    // Critic Actions
    public String critics() {
        return SUCCESS;
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
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public Integer getId() {
        return this.id;
    }

    public Album getAlbum() {
        return this.album;
    }

    public Critic getCritic() {
        return this.critic;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return this.song;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }
}
