import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Server implements UserInterface {
    ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Server server = new Server();
        int port = 8000;

        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException nfe) {}

        try {
            Registry registry = LocateRegistry.createRegistry(port);
            UserInterface userInterface = (UserInterface) UnicastRemoteObject.exportObject(server, port);

            registry.rebind("UserInterface", userInterface);
            System.out.println("Server online at port " + port);
            while (true);
        } catch (RemoteException re) {
            System.out.println("Couldnt create registry or export interface");
        }
    }

    // SIMULATION
    public Server() throws NoSuchAlgorithmException {
        // BEGIN SIMULATION
        this.users.add(new User("goamaral", "rooted"));
        // END SIMULATION
    }

    // UserController
    public void login(User user) throws CustomException {
        User fetched_user;

        System.out.println("Action login: " + user.toString());

        try {
            fetched_user = this.user_findByUsername(user.username);
        } catch (CustomException ce) {
            throw new CustomException("Invalid credentials");
        }


        if (!fetched_user.password.equals(user.password)) {
            System.out.println("Invalid password");
            throw new CustomException("Invalid credentials");
        }

        System.out.println("Login successful");
    }

    public void register(User user) throws CustomException {
        this.user_save(user);
    }

    // ORM
    // SIMULATION
    private User user_findByUsername(String username) throws CustomException {
        User user = null;

        System.out.print("Find user by username (" + username + ")");

        // START SIMULATION
        for (User user_i : this.users) {
            user = user_i;
            if (user_i.username.equals(username)) break;
        }
        if (user == null) {
            System.out.println(" not found");
            throw new CustomException();
        }
        // END SIMULATION

        System.out.println(" found");
        return user;
    }

    private void user_save(User user) throws CustomException {
        // Failed to save
        ArrayList<String> errors = new ArrayList<>(1);
        errors.add("Username not found");
        throw new CustomException(errors);
    }
}