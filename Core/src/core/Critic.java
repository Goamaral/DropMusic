package core;

import java.io.Serializable;
import java.util.ArrayList;

public class Critic implements Serializable {
    public int id;
    public int rating;
    public String justification;

    // Relationships
    public Album album;
    public User author;

    public Critic() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Critic(int rating, String justification, Album album, User author) {
        this.rating = rating;
        this.justification = justification;
        this.album = album;
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public String getJustification() {
        return justification;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void validate() throws CustomException {
        ArrayList<String> errors = new ArrayList();

        try {
            this.ratingValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        try {
            this.justificationValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        if (errors.size() > 0) throw new CustomException(errors);
    }

    private void ratingValidator() throws CustomException {
        if (this.rating < 0 || this.rating > 5) throw new CustomException("Rating has to be between 0 and 5");
    }

    private void justificationValidator() throws CustomException {
        this.justification.replaceAll("\\s","");

        if (this.justification.length() > 300)
            throw  new CustomException("Justification can't be bigger than 300 characters");

        if (this.justification.length() == 0)
            throw new CustomException("Justification can't be empty");
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getAlbum_name() {
        return album.name;
    }
}
