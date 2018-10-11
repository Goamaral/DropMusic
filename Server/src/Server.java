import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Server implements UserInterface {
    Database database = new Database();

    public static void main(String[] args)  {
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

    public Server() {}

    // UserController
    public void login(User user) throws CustomException, NoSuchAlgorithmException {
        user.encrypt_password();
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

    public void register(User user) throws CustomException, NoSuchAlgorithmException {
        this.user_save(user);
    }

    // ORM
    private User user_findByUsername(String username) throws CustomException {
        System.out.print("Find user by username (" + username + ")");

        try {
            User user = this.database.user_findByUsername(username);
            System.out.println(" found");
            return user;
        } catch (CustomException ce) {
            System.out.println(" not found");
            throw ce;
        }
    }

    private void user_save(User user) throws CustomException, NoSuchAlgorithmException {
        System.out.print("Save user (" + user.username + ")");

        try {
            user.validate();
            this.database.user_save(user);
        } catch(CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }
        
        System.out.println(" success");
    }
}

class Database {
    ArrayList<User> users = new ArrayList<>();

    User user_findByUsername(String username) throws CustomException {
        for (User user : this.users) {
            if (user.username.equals(username)) {
                return user;
            }
        }

        throw new CustomException("Username not found");
    }

    void user_save(User user) throws CustomException {
        for (User user_i : this.users) {
            if (user_i.username.equals(user.username)) {
                throw new CustomException("Username already exists");
            }
        }

        if  (this.users.size() == 0) user.becomeEditor();
        this.users.add(user);
    } 
}