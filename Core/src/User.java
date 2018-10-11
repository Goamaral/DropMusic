import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends Model {
    String username;
    String password;
    boolean password_encrypted;
    boolean isEditor = false;

    void create() throws CustomException {
        this.prepare();
    }

    void read() throws CustomException {

    }

    void update() throws CustomException {
        this.prepare();
    }

    void delete() throws CustomException {
    }

    void validator() throws CustomException {
        throw new CustomException(null);
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

    private void becomeEditor() {
        isEditor = true;
    }

    void prepare() throws CustomException {
        this.validator();
        //this.encrypt_password();
    }

    public String toString() {
        return "User: { username: " + this.username + ", password: " + this.password + " }";
    }
}
