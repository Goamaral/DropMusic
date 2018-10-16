import java.security.NoSuchAlgorithmException;

public class UserController implements UserInterface {
    Server server;

    public UserController(Server server) { this.server = server; }

    // Controller
    public User login(User user) throws CustomException, NoSuchAlgorithmException {
        user.encrypt_password();
        User fetched_user;

        System.out.println("Action user login: " + user.toString());

        try {
            fetched_user = this.findByUsername(user.username);
        } catch (CustomException ce) {
            throw new CustomException("Invalid credentials");
        }

        if (!fetched_user.password.equals(user.password)) {
            System.out.println("Invalid password");
            throw new CustomException("Invalid credentials");
        }

        System.out.println("Login successful");

        return fetched_user;
    }

    public void register(User user) throws CustomException, NoSuchAlgorithmException {
        System.out.println("Action user register: " + user.toString());

        this.save(user);
    }

    // ORM
    private User findByUsername(String username) throws CustomException {
        System.out.print("Action user findByUsername: " + username);

        try {
            User user = this.server.database.user_findByUsername(username);
            System.out.println(" found");
            return user;
        } catch (CustomException ce) {
            System.out.println(" not found");
            throw ce;
        }
    }

    private void save(User user) throws CustomException, NoSuchAlgorithmException {
        System.out.print("Action user save: " + user.toString());

        try {
            user.validate();
            this.server.database.user_create(user);
        } catch(CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }
}

