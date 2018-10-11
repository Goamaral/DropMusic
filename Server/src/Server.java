import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public Server() {}

    public static void main(String[] args)  {
        int port = 8000;

        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException nfe) {}

        try {
            Registry registry = LocateRegistry.createRegistry(port);

            // Controllers
            UserController userController = new UserController();
            UserInterface userInterface = (UserInterface) UnicastRemoteObject.exportObject(userController, port);
            registry.rebind("UserInterface", userInterface);

            System.out.println("Server online at port " + port);

            while (true);
        } catch (RemoteException re) {
            System.out.println("Couldnt create registry or export interface");
        }
    }
}