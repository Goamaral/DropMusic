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

    private void encrypt_password() {
        if (!this.password_encrypted) {
            // Encrypt
            this.password_encrypted = true;
        }
    }

    private void becomeEditor() {
        isEditor = true;
    }

    void prepare() throws CustomException {
        this.validator();
        this.encrypt_password();
    }
}
