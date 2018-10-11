import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client {
    UserInterface userInterface;
    int connectAttemps = 0;
    int maxAttemps = 10;
    int serverPort = 8000;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Client client = new Client();

        try {
            if (args.length == 1) {
                client.serverPort = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException nfe) {}

        Boolean successful = client.connect();
        if (!successful) client.reconnect();

        client.clearScreen();
        client.displayLogin();
    }

    public Client() {}

    // Connect to RMI server
    Boolean connect() {
        try {
            Registry registry = LocateRegistry.getRegistry(serverPort);
            this.userInterface = (UserInterface) registry.lookup("UserInterface");
        } catch (RemoteException re) {
           return false;
        } catch (NotBoundException nbe) {
            System.out.println("Cant connect to server");
            System.exit(0);
        }

        return true;
    }

    void reconnect() {
        Boolean successful = false;

        while(!successful) {
            this.connectAttemps += 1;
            if (this.connectAttemps == this.maxAttemps) {
                // Failure disconnect
                System.out.println("Cant connect to server");
                System.exit(0);
            }
            successful = this.connect();
        }

        this.connectAttemps = 0;
    }

    void displayLogin() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        boolean retry = true;

        System.out.println("Login");
        System.out.print("Username: ");
        user.username = scanner.nextLine();
        System.out.print("Password: ");
        user.password = scanner.nextLine();

        try {
            user.encrypt_password();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        while(retry) {
            try {
                userInterface.login(user);
                retry = false;
            } catch (CustomException ce) {
                this.clearScreen();
                ce.printErrors();
                this.displayLogin();
                retry = false;
            } catch (RemoteException re) {
                System.out.println(re.getMessage());
                this.reconnect();
            }
        }

        scanner.close();
    }

    void displayRegister() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        boolean retry = true;

        System.out.println("Register");
        System.out.print("Username: ");
        user.username = scanner.nextLine();
        System.out.print("Password: ");
        user.password = scanner.nextLine();

        try {
            user.encrypt_password();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        while(retry) {
            try {
                userInterface.register(user);
                retry = false;
            } catch (CustomException ce) {
                this.clearScreen();
                ce.printErrors();
                this.displayRegister();
                retry = false;
            } catch (RemoteException re) {
                this.reconnect();
            }
        }

        scanner.close();
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}
