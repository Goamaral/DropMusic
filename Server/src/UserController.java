import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserController implements UserInterface {
    Server server;

    public UserController(Server server) { this.server = server; }

    // Controller
    public User login(User user, int tcp) throws CustomException, NoSuchAlgorithmException {
        System.out.println("Action user(" + user.username + ") login: ");

        user.encrypt_password();

        Object response_object = this.server.dbRequest("user_findByUsername", user.username);

        this.server.catch_response_exception(response_object);

        User fetched_user = (User) response_object;

        if (!fetched_user.password.equals(user.password)) {
            System.out.println("Invalid password");
            throw new CustomException("Invalid credentials");
        }

        Socket socket = new Socket();

        try {
            String ipString = RemoteServer.getClientHost();
            InetAddress ip = InetAddress.getByName(ipString);
            socket = new Socket(ip, tcp);
        } catch (ServerNotActiveException | IOException e) {}

        synchronized (this.server.clientLock) {
            this.server.clients.add(new Client(socket, fetched_user.id));
        }

        ArrayList<Job> jobs_to_perform = new ArrayList<>();
        synchronized (this.server.jobLock) {
            for (Job job : this.server.jobs) {
                if (job.user_id == fetched_user.id) {
                    jobs_to_perform.add(job);
                }
            }
        }

        for (Job job : jobs_to_perform) {
            this.server.send_notifications(job);
        }

        System.out.println("success");

        return fetched_user;
    }

    public void register(User user) throws CustomException, NoSuchAlgorithmException {
        System.out.print("Action user(" + user.username + ") register: ");

        user.validate();

        Object response_object = this.server.dbRequest("user_create", user);

        this.server.catch_response_exception(response_object);

        System.out.println("success");
    }

    public ArrayList<User> normal_users() throws CustomException {
        System.out.print("Action get normal users ");

        Object response_object = this.server.dbRequest("normal_users", true);

        this.server.catch_response_exception(response_object);

        System.out.println("success");

        return (ArrayList<User>) response_object;
    }

    public void promote(int user_id) throws CustomException {
        System.out.print("Action promote user " + user_id + " ");

        Object response_object = this.server.dbRequest("user_promote", user_id);

        this.server.catch_response_exception(response_object);

        this.server.send_notifications(new Job(user_id, "You have been promoted.\nYou are now an editor."));

        System.out.println("success");
    }

    public User read(int id) throws CustomException {
        System.out.println("Action user read " + id + " ");

        Object response_object = this.server.dbRequest("user_find", id);

        this.server.catch_response_exception(response_object);

        System.out.println("success");

        return (User) response_object;
    }

    public ArrayList<User> index() throws CustomException {
        System.out.println("Action user index ");

        Object response_object = this.server.dbRequest("user_all", true);

        this.server.catch_response_exception(response_object);

        System.out.println("success");

        return (ArrayList<User>) response_object;
    }
}
