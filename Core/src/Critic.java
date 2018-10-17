import java.io.Serializable;
import java.util.ArrayList;

public class Critic implements Serializable {
    int rating;
    String justification;

    // Relationships
    Album album;
    User author;

    Critic(int rating, String justification, Album album, User author) {
        this.rating = rating;
        this.justification = justification;
        this.album = album;
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
        if (this.rating > 5) throw new CustomException("Rating can't be bigger than 5");
    }

    private void justificationValidator() throws CustomException {
        if (this.justification.length() > 300)
            throw  new CustomException("Justification can't be bigger than 200 characters");
    }

    public String toString() {
        return "Critic: { "
                + "rating: " + this.rating + "/5, "
                + "justification: " + this.justification + ", "
                + "album: " + this.album.name + ", "
                + "author_id: " + this.author.username
                + " }";
    }
}
