package core;

import java.util.ArrayList;

public class CustomException extends Exception {
    public ArrayList<String> errors = new ArrayList<>();
    public int extraFlag = 0;

    CustomException(ArrayList<String> errors) {
        super();
        this.errors = errors;
    }

    public CustomException(String error) {
        super();
        this.errors.add(error);
    }

    void printErrors() {
        if (this.errors == null) return;

        System.out.println("Errors:");

        for (String error : this.errors) {
            System.out.println("-> " + error);
        }
    }
}
