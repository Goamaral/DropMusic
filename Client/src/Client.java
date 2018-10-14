import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client {
    UserInterface userInterface;
    int connectAttemps = 0;
    int maxAttemps = 30;
    int primaryServerPort = 8000;
    int secundaryServerPort = 8001;
    Scanner scanner = new Scanner(System.in);

    static final int EXIT = 0;
    static final int START = 1;
    static final int REGISTER = 2;
    static final int LOGIN = 3;

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        Client client = new Client();
        int option;

        try {
            if (args.length >= 1) {
                client.primaryServerPort = Integer.parseInt(args[0]);
            }
            if (args.length >= 2) {
                client.secundaryServerPort = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException nfe) {}

        client.connect();

        client.redirect(Client.START, null);

        client.scanner.close();
    }

    public Client() {}

    // Connect to RMI server
    void connect() throws InterruptedException {
        int port = this.primaryServerPort;

        if (this.connectAttemps % 2 == 1) {
            port = this.secundaryServerPort;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            this.userInterface = (UserInterface) registry.lookup("UserInterface");
        } catch (RemoteException re) {
            this.connectAttemps += 1;
            if (this.connectAttemps == this.maxAttemps) {
                System.out.println("Cant connect to the server");
                System.exit(0);
            }

            Thread.sleep(1000);
            this.connect();
        } catch (NotBoundException nbe) {
            System.out.println("Cant connect to server");
            System.exit(0);
        }

        this.connectAttemps = 0;
    }

    void retry(int method_id, Object resource) throws InterruptedException, CustomException, NoSuchAlgorithmException {
        this.connect();

        try {
            switch (method_id) {
                case Client.LOGIN:
                    userInterface.login((User)resource);
                    break;
                case Client.REGISTER:
                    userInterface.register((User)resource);
                    break;
            }
        } catch(RemoteException re) {
            retry(method_id, resource);
        }
    }

    // Routes
    void redirect(int route, CustomException errorException) throws InterruptedException, NoSuchAlgorithmException {
        this.clearScreen();

        try {
            errorException.printErrors();
        } catch(NullPointerException npe) {}

        switch (route) {
            case Client.EXIT: return;
            case Client.START:
                redirect(this.displayStart(), null);
                return;
            case Client.REGISTER:
                try {
                    this.displayRegister();
                    redirect(Client.START, null);
                } catch (CustomException ce) {
                    redirect(Client.START, ce);
                }
                return;
            case Client.LOGIN:
                try {
                    this.displayLogin();
                    redirect(Client.EXIT, null);
                } catch (CustomException ce) {
                    redirect(Client.START, ce);
                }
                return;
        }
    }

    // Views
    int displayStart() {
        String option;

        System.out.println("Start");
        System.out.println("[2] Create account");
        System.out.println("[3] Log in");
        System.out.println("[0] Exit");
        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (!option.matches("[023]")) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return displayStart();
        }

        return Integer.parseInt(option);
    }

    void displayLogin() throws NoSuchAlgorithmException, InterruptedException, CustomException {
        User user;
        String username;
        String password;

        System.out.println("Login");
        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("Password: ");
        password = scanner.nextLine();

        user = new User(username, password);

        try {
            userInterface.login(user);
        } catch (RemoteException re) {
            this.retry(Client.LOGIN, user);
        }
    }

    void displayRegister() throws NoSuchAlgorithmException, CustomException, InterruptedException {
        User user;
        String password, username;

        System.out.println("Register");
        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("Password: ");
        password = scanner.nextLine();

        user = new User(username, password);

        try {
            userInterface.register(user);
        } catch (RemoteException re) {
            this.retry(Client.REGISTER, user);
        }
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}
