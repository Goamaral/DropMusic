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

        String response = this.server.dbRequest("user_findByUsername", user);
        User fetched_user = (User)Serializer.deserialize(response);

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

        try {
            user.validate();
            this.server.dbRequest("user_create", user);
            System.out.println("Successful");
        } catch(CustomException ce) {
            System.out.println("failed");
            throw ce;
        }
    }

    public ArrayList<User> normal_users() throws CustomException {
        String response = this.server.dbRequest("normal_users", new Object());
        return (ArrayList<User>) Serializer.deserialize(response);
    }

    public void promote(int user_id) {
        this.server.dbRequest("user_promote", user_id);
        this.server.send_notifications(new Job(user_id, "You have been promoted.\nYou are now an editor."));
    }

    public User read(int id) throws CustomException {
        String response = this.server.dbRequest("user_find", id);
        return (User)Serializer.deserialize(response);
    }

    public ArrayList<User> index() throws CustomException {
        String response = this.server.dbRequest("user_all", new Object());
        return (ArrayList<User>) Serializer.deserialize(response);
    }
}
