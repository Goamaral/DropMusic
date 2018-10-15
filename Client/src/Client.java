import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    UserInterface userInterface;
    AlbumInterface albumInterface;

    User user;
    int resource_id;

    int connectAttemps = 0;
    int maxAttemps = 30;
    int primaryServerPort = 8000;
    int secundaryServerPort = 8001;

    Scanner scanner = new Scanner(System.in);

    static final int EXIT = 0;
    static final int START = 1;
    static final int REGISTER = 2;
    static final int LOGIN = 3;
    static final int DASHBOARD = 4;
    static final int ALBUMS = 5;
    static final int ALBUM = 6;
    static final int ALBUM_CREATE = 7;

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
            this.albumInterface = (AlbumInterface) registry.lookup("AlbumInterface");
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

    Object retry(int method_id, Object resource) throws InterruptedException, CustomException, NoSuchAlgorithmException {
        this.connect();

        try {
            switch (method_id) {
                case Client.LOGIN:
                    this.user = userInterface.login((User)resource);
                    break;
                case Client.REGISTER:
                    userInterface.register((User)resource);
                    break;
                case Client.ALBUMS:
                    return albumInterface.index();
            }
        } catch(RemoteException re) {
            retry(method_id, resource);
        }

        return null;
    }

    // Routes
    void redirect(int route, CustomException errorException) throws InterruptedException, NoSuchAlgorithmException {
        this.clearScreen();

        try {
            errorException.printErrors();
        } catch(NullPointerException npe) {}

        switch (route) {
            case Client.EXIT: break;
            case Client.START:
                this.redirect(this.displayStart(), null);
                break;
            case Client.REGISTER:
                try {
                    this.displayRegister();
                    this.redirect(Client.START, null);
                } catch (CustomException ce) {
                    this.redirect(Client.START, ce);
                }
                break;
            case Client.LOGIN:
                try {
                    this.displayLogin();
                    this.redirect(Client.DASHBOARD, null);
                } catch (CustomException ce) {
                    this.redirect(Client.START, ce);
                }
                break;
            case Client.DASHBOARD:
                this.redirect(this.displayDashboard(), null);
                break;
            case Client.ALBUMS:
                try {
                    this.redirect(this.displayAlbums(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUMS, ce);
                }
                break;
        }
    }

    // Views
    int displayStart() {
        String option;

        System.out.println("Start");
        System.out.println("[" + Client.REGISTER + "] Create account");
        System.out.println("[" + Client.LOGIN + "] Log in");
        System.out.println("[" + Client.EXIT + "] Exit");
        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (!option.matches("[" + Client.REGISTER + Client.LOGIN + Client.EXIT + "]")) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayStart();
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
            this.user = userInterface.login(user);
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

    int displayDashboard() {
        String option;

        System.out.println("Dashboard");
        System.out.println("[" + Client.ALBUMS + "] Albums");
        System.out.println("[" + Client.START + "] Logout");
        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (!option.matches("[" + Client.ALBUMS + Client.START + "]")) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayDashboard();
        }

        return Integer.parseInt(option);
    }

    int displayAlbums() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String option;
        ArrayList<Album> albums;

        System.out.println("Albums");

        try {
            albums = this.albumInterface.index();
        } catch (RemoteException re) {
            albums = (ArrayList<Album>) this.retry(Client.ALBUMS, null);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayDashboard();
        }

        if (albums.size() == 0) {
            System.out.println("No albums available");
        } else {
            for (Album album : albums) {
                System.out.println("[" + album.id + "] " + album.name);
            }
        }

        if (this.user.isEditor) {
            System.out.println("[C] Create album");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("C")) { return Client.ALBUM_CREATE; }

        if (option.equals("B")) { return Client.DASHBOARD; }

        try {
            this.resource_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbums();
        }

        return Client.ALBUM;
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}
