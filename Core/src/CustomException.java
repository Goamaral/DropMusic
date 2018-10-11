import java.util.ArrayList;

public class CustomException extends Exception {
    ArrayList<String> errors;
    String error;

    CustomException() { }

    CustomException(ArrayList<String> errors) {
        super();
        this.errors = errors;
    }

    CustomException(String error) {
        super();
        this.error = error;
    }

    void printErrors() {
        if (this.errors == null && this.error == null) return;

        System.out.println("Errors:");

        if (this.error != null) {
            System.out.println("-> " + this.error);
        }

        if (this.errors != null) {
            for (String error : this.errors) {
                System.out.println("-> " + error);
            }
        }
    }
}
