public class User extends Model {
    String name;
    String username;
    String password;
    boolean password_encrypted;
    boolean isEditor = false;

    void create() throws CustomException {
        this.prepare();
    }

    void update() throws CustomException {
        this.prepare();
    }

    // ADD delete exception?
    void delete() /*throws DeleteException*/ {
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
