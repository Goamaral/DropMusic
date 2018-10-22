import java.io.Serializable;

public class Genre implements Serializable {
    int id;
    String name;

    Genre(String name) { this.name = name; }

    public void validate() throws CustomException { this.nameValidator(); }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }
}
