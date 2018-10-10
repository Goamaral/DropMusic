import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server implements UserInterface {
    public static void main(String[] args) {
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
        } catch (RemoteException re) {
            System.out.println("Couldnt create registry or export interface");
        }
    }

    public Server() {}

    // UserController
    public void login(User user) throws CustomException {
        System.out.println("HIT");
        User fetched_user = this.user_findByUsername(user.username);

        if (!fetched_user.password.equals(user.password)) {
            ArrayList<String> errors = new ArrayList<>(1);
            errors.add("Invalid credentials");
            throw new CustomException(errors);
        }
    }

    public void register(User user) throws CustomException {
        this.user_save(user);
    }

    // ORM
    private User user_findByUsername(String username) throws CustomException {
        // Not found
        ArrayList<String> errors = new ArrayList<>(1);
        errors.add("Username not found");
        throw new CustomException(errors);
    }

    private void user_save(User user) throws CustomException {
        // Failed to save
        ArrayList<String> errors = new ArrayList<>(1);
        errors.add("Username not found");
        throw new CustomException(errors);
    }
}