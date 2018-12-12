package core;

import java.util.ArrayList;

public class CustomException extends Exception {
    ArrayList<String> errors;
    int extraFlag = 0;

    CustomException(ArrayList<String> errors) {
        super();
        this.errors = errors;
    }

    CustomException(String error) {
        super();
        this.errors = new ArrayList<>(1);
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
