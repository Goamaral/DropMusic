package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable {
    public int id = -1;

    public String username;
    public String password;
    public String uid;
    boolean password_encrypted;
    public boolean isEditor = false;
    public ArrayList<Integer> stored_song_ids = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {}

    // https://stackoverflow.com/questions/6592010/encrypt-and-decrypt-a-password-in-java
    public void encrypt_password() throws NoSuchAlgorithmException {
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

    public void validate() throws CustomException, NoSuchAlgorithmException {
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

    public void becomeEditor() {
        isEditor = true;
    }

    public void addStoredSong(int id) {
        this.stored_song_ids.add(id);
    }

    public String toString() {
        return "User: { username: " + this.username + ", isEditor: " + this.isEditor + " }";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
