package core;

import java.util.ArrayList;

public class CustomException extends Exception {
    public ArrayList<String> errors = new ArrayList<>();

    CustomException(ArrayList<String> errors) {
        super();
        this.errors = errors;
    }

    CustomException(String error) {
        super();
        this.errors.add(error);
    }
}
