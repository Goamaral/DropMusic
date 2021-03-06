import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable {
    int id = -1;
    String username;
    String password;
    boolean password_encrypted;
    boolean isEditor = false;
    ArrayList<Integer> stored_song_ids = new ArrayList<>();

    public User(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = password;
    }

    // https://stackoverflow.com/questions/6592010/encrypt-and-decrypt-a-password-in-java
    void encrypt_password() throws NoSuchAlgorithmException {
        if (!this.password_encrypted) {
            MessageDigest encrypter = MessageDigest.getInstance("MD5");
            encrypter.reset();
            byte[] digested = encrypter.digest(this.password.getBytes());

            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }

            this.password = sb.toString();
            this.password_encrypted = true;
        }
    }

    void validate() throws CustomException, NoSuchAlgorithmException {
        ArrayList<String> errors = new ArrayList();

        try {
            this.usernameValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        try {
            this.passwordValidator();
        } catch (CustomException ce) {
            errors.add(ce.errors.get(0));
        }

        if (errors.size() > 0) throw new CustomException(errors);

        this.encrypt_password();
    }

    private void usernameValidator() throws CustomException {
        this.username.replaceAll("\\s","");

        // Not null
        if (this.username == null || this.username.length() == 0) {
            throw new CustomException("Username can't be empty");
        }
    }

    private void passwordValidator() throws CustomException {
        this.password.replaceAll("\\s","");

        // Not null
        if (this.password == null || this.password.length() == 0) throw new CustomException("Password can't be empty");

        // Minimum size MIN_SIZE
        int MIN_SIZE = 6;
        if (this.password.length() < MIN_SIZE) throw new CustomException("Password has to be at minimum 6 characters long");
    }

    void becomeEditor() {
        isEditor = true;
    }

    void addStoredSong(int id) {
        this.stored_song_ids.add(id);
    }

    public String toString() {
        return "User: { username: " + this.username + ", password: " + this.password + " }";
    }
}
