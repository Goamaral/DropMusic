import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserController implements UserInterface {
    Server server;

    public UserController(Server server) { this.server = server; }

    // Controller
    public User login(User user) throws CustomException, NoSuchAlgorithmException {
        user.encrypt_password();
        User fetched_user;

        System.out.print("Action user(" + user.username + ") login: ");

        try {
            fetched_user = this.server.database.user_findByUsername(user.username);
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
        System.out.print("Action user(" + user.username + ") register: ");

        try {
            user.validate();
            this.server.database.user_create(user);
        } catch(CustomException ce) {
            System.out.println("failed");
            throw ce;
        }

        System.out.println("success");
    }

    public ArrayList<User> normal_users() { return this.server.database.normal_users(); }

    public void promote(int user_id) throws CustomException { this.server.database.user_promote(user_id); }

}
