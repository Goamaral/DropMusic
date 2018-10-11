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
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Client client = new Client();
        int option;

        try {
            if (args.length == 1) {
                client.serverPort = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException nfe) {}

        Boolean successful = client.connect();
        if (!successful) client.reconnect();
        System.out.println("Connected to the server");

        client.clearScreen();
        option = client.displayAuth(false);

        if (option == 0) {
            client.displayRegister();
        } else {
            client.displayLogin();
        }

        client.scanner.close();
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

    // Views
    int displayAuth(boolean repeated) {
        String option;

        System.out.println("Auth");
        System.out.println("[0] Create account");
        System.out.println("[1] Log in");
        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (!option.matches("0|1")) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return displayAuth(true);
        }

        return Integer.parseInt(option);
    }

    void displayLogin() throws NoSuchAlgorithmException {
        User user;
        String username;
        String password;

        System.out.println("Login");
        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("Password: ");
        password = scanner.nextLine();

        user = new User(username, password);

        while(true) {
            try {
                userInterface.login(user);
                break;
            } catch (CustomException ce) {
                this.clearScreen();
                ce.printErrors();
                this.displayLogin();
                break;
            } catch (RemoteException re) {
                System.out.println(re.getMessage());
                this.reconnect();
            }
        }
    }

    void displayRegister() throws NoSuchAlgorithmException {
        User user;
        String password, username;

        System.out.println("Register");
        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("Password: ");
        password = scanner.nextLine();

        user = new User(username, password);

        while(true) {
            try {
                userInterface.register(user);
                break;
            } catch (CustomException ce) {
                this.clearScreen();
                ce.printErrors();
                this.displayRegister();
                break;
            } catch (RemoteException re) {
                this.reconnect();
            }
        }
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}
