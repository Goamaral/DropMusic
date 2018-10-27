import org.json.simple.JSONObject;

import java.io.Serializable;

public class Genre extends Model implements Serializable {
    int id;
    String name;

    Genre(String name) { this.name = name; }

    public void validate() throws CustomException { this.nameValidator(); }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("name", this.name);

        return obj;
    }
}
