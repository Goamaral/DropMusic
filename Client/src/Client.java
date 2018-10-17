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

    User current_user;
    int current_album_id;
    Album current_album;
    int current_critic_id;
    Critic current_critic;

    int connectAttemps = 0;
    int maxAttemps = 30;
    int port1;
    int port2;

    Scanner scanner = new Scanner(System.in);

    static final int EXIT = 0;
    static final int START = 1;
    static final int REGISTER = 2;
    static final int LOGIN = 3;
    static final int DASHBOARD = 4;
    static final int ALBUMS = 5;
    static final int ALBUM = 6;
    static final int ALBUM_CREATE = 7;
    static final int ALBUM_CRITICS = 8;
    static final int ALBUM_GENRES = 9;
    static final int ALBUM_ARTISTS = 10;
    static final int ALBUM_SONGS = 11;
    static final int ALBUM_UPDATE = 12;
    static final int ALBUM_DELETE = 13;
    static final int ALBUM_CRITIC = 14;
    static final int ALBUM_CRITIC_CREATE = 15;

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        Client client = new Client();

        try {
            System.out.print("Server port 1: ");
            client.port1 = Integer.parseInt(client.scanner.nextLine());

            System.out.print("Server port 2: ");
            client.port2 = Integer.parseInt(client.scanner.nextLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid port");
            System.exit(0);
        }

        client.connect();

        client.redirect(Client.START, null);

        client.scanner.close();
    }

    public Client() {}

    // Connect to RMI server
    void connect() throws InterruptedException {
        int port = this.port1;

        if (this.connectAttemps % 2 == 1) {
            port = this.port2;
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

    // FailOver
    Object retry(int method_id, Object resource) throws InterruptedException, CustomException, NoSuchAlgorithmException {
        this.connect();

        try {
            switch (method_id) {
                case Client.LOGIN:
                    this.current_user = userInterface.login((User)resource);
                    break;
                case Client.REGISTER:
                    System.out.println("LOG1");
                    userInterface.register((User)resource);
                    System.out.println("LOG2");
                    break;
                case Client.ALBUMS:
                    return albumInterface.index();
                case Client.ALBUM_CREATE:
                    this.albumInterface.create((Album)resource);
                    break;
                case Client.ALBUM:
                    return this.albumInterface.read((int)resource);
                case Client.ALBUM_UPDATE:
                    this.albumInterface.update((Album)resource);
                    break;
                case Client.ALBUM_DELETE:
                    this.albumInterface.delete((int)resource);
                    break;
                case Client.ALBUM_CRITICS:
                    return albumInterface.critics((int)resource);
                case Client.ALBUM_CRITIC_CREATE:
                    this.albumInterface.critic_create((Critic)resource);
                    break;
                case Client.ALBUM_CRITIC:
                    return this.albumInterface.critic(this.current_album_id, (int)resource);
            }
        } catch(RemoteException re) {
            this.retry(method_id, resource);
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
            case Client.ALBUM_CREATE:
                try {
                    this.displayAlbumCreate();
                    this.redirect(Client.ALBUMS, null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUMS, ce);
                }
                break;
            case Client.ALBUM:
                try {
                    this.redirect(this.displayAlbum(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUMS, ce);
                }
                break;
            case Client.ALBUM_UPDATE:
                try {
                    this.displayAlbumUpdate();
                    redirect(Client.ALBUM, null);
                } catch (CustomException ce) {
                    redirect(Client.ALBUMS, ce);
                }
            case Client.ALBUM_DELETE:
                try {
                    try {
                        this.albumInterface.delete(this.current_album_id);
                    } catch (RemoteException re) {
                        this.retry(Client.ALBUM_DELETE, this.current_album_id);
                    }

                    this.redirect(Client.ALBUMS, null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUMS, ce);
                }
                break;
            case Client.ALBUM_CRITICS:
                try {
                    this.redirect(this.displayAlbumCritics(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUMS, ce);
                }
                break;
            case Client.ALBUM_CRITIC_CREATE:
                try {
                    this.displayAlbumCriticCreate();
                    this.redirect(Client.ALBUM_CRITICS, null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUMS, ce);
                    } else {
                        this.redirect(Client.ALBUM_CRITICS, ce);
                    }
                }
                break;
            case Client.ALBUM_CRITIC:
                try {
                    this.displayAlbumCritic();
                    this.redirect(Client.ALBUM_CRITICS, null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUMS, ce);
                    } else {

                        this.redirect(Client.ALBUM_CRITICS, ce);
                    }
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
            this.current_user = userInterface.login(user);
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

        try {
            albums = this.albumInterface.index();
        } catch (RemoteException re) {
            albums = (ArrayList<Album>) this.retry(Client.ALBUMS, null);
        }

        System.out.println("Albums");

        if (albums.size() == 0) {
            System.out.println("No albums available");
        } else {
            for (Album album : albums) {
                System.out.println("[" + album.id + "] " + album.name);
            }
        }

        if (this.current_user.isEditor) {
            System.out.println("[C] Create album");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("C")) return Client.ALBUM_CREATE;

        if (option.equals("B")) return Client.DASHBOARD;

        try {
            this.current_album_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbums();
        }

        return Client.ALBUM;
    }

    void displayAlbumCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String name, info, realeaseDateString;
        Album album;

        System.out.println("Create album");

        System.out.print("Name: ");
        name = scanner.nextLine();

        System.out.print("Info: ");
        info = scanner.nextLine();

        System.out.print("Release date(dd/mm/yyyy): ");
        realeaseDateString = scanner.nextLine();

        album = new Album(name, info, realeaseDateString);

        try {
            this.albumInterface.create(album);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_CREATE, album);
        }
    }

    int displayAlbum() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String option;

        try {
            this.current_album = this.albumInterface.read(this.current_album_id);
        } catch (RemoteException re) {
            this.current_album = (Album)this.retry(Client.ALBUM, this.current_album_id);
        }

        System.out.println("Album");
        System.out.println("Name: " + this.current_album.name);
        System.out.println("Rating: " + this.current_album.getRating() + "/5");
        System.out.println("Info: " + this.current_album.info);
        System.out.println("Release date: " + this.current_album.releaseDateString);
        System.out.println("[" + Client.ALBUM_CRITICS + "] See album critics");
        System.out.println("[" + Client.ALBUM_GENRES + "] See album genres");
        System.out.println("[" + Client.ALBUM_SONGS + "] See album songs");
        System.out.println("[" + Client.ALBUM_ARTISTS + "] See album artists");

        if (this.current_user.isEditor) {
            System.out.println("[" + Client.ALBUM_UPDATE + "] Edit album");
            System.out.println("[" + Client.ALBUM_DELETE + "] Delete album");
        }

        System.out.println("[" + Client.ALBUMS + "] Back");
        System.out.print("Option: ");

        option = this.scanner.nextLine();

        if (!option.matches("(" + Client.ALBUM_CRITICS + "|" + Client.ALBUM_GENRES + "|" + Client.ALBUM_SONGS + "|" + Client.ALBUM_ARTISTS + "|" + Client.ALBUM_UPDATE + "|" + Client.ALBUM_DELETE + "|" + Client.ALBUMS + ")")) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbum();
        }

        return Integer.parseInt(option);
    }

    void displayAlbumUpdate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Album album = this.current_album;
        Album new_album;
        String name;
        String info;
        String releaseDate;

        System.out.println("Edit album");
        System.out.println("Leave fields empty if you don't want to change them");

        System.out.print("Name(" + album.name + "): ");
        name = this.scanner.nextLine();
        if (name.length() == 0) name = album.name;

        System.out.print("Info(" + album.info + "): ");
        info = this.scanner.nextLine();
        if (info.length() == 0) info = album.info;

        System.out.print("Release date(" + album.releaseDateString + "): ");
        releaseDate = this.scanner.nextLine();
        if (releaseDate.length() == 0) releaseDate = album.releaseDateString;

        new_album = new Album(name, info, releaseDate);
        new_album.id = album.id;

        try {
            this.albumInterface.update(new_album);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_UPDATE, new_album);
        }
    }

    int displayAlbumCritics() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Critic> critics;
        String option;

        try {
            critics = this.albumInterface.critics(current_album_id);
        } catch (RemoteException re) {
            critics = (ArrayList<Critic>)this.retry(Client.ALBUM_CRITICS, null);
        }

        System.out.println("Critics");
        if (critics.size() == 0) {
            System.out.println("No critics available");
        } else {
            int i = 0;
            for (Critic critic : critics) {
                System.out.println("[" + i + "] By " + critic.author.username + " with " + critic.rating + "/5 rating");
                i += 1;
            }
        }

        System.out.println("[C] Add critic");
        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("C")) return Client.ALBUM_CRITIC_CREATE;

        if (option.equals("B")) return Client.ALBUM;

        try {
            this.current_critic_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbumCritics();
        }

        return Client.ALBUM_CRITIC;
    }

    void displayAlbumCriticCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Critic critic;
        int rating;
        String ratingString;
        String justification;

        System.out.println("Add critic");

        System.out.print("Rating(x/5): ");
        ratingString = this.scanner.nextLine();

        try {
            rating = Integer.parseInt(ratingString);
        } catch (NumberFormatException nfe) {
            throw new CustomException("Invalid rating");
        }

        System.out.print("Justification: ");
        justification = this.scanner.nextLine();

        critic = new Critic(rating, justification, this.current_album, this.current_user);

        try {
            this.albumInterface.critic_create(critic);
        } catch(RemoteException re) {
            this.retry(Client.ALBUM_CRITIC_CREATE, critic);
        }
    }

    void displayAlbumCritic() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        try {
            this.current_critic = this.albumInterface.critic(this.current_album_id, this.current_critic_id);
        } catch (RemoteException re) {
            this.current_critic = (Critic) this.retry(Client.ALBUM_CRITIC, this.current_critic_id);
        }

        System.out.println("Critic");
        System.out.println("Album: " + this.current_critic.album.name);
        System.out.println("Author: " + this.current_critic.author.username);
        System.out.println("Rating: " + this.current_critic.rating + "/5");
        System.out.println("Justification: " + this.current_critic.justification);
        System.out.println("[B] Back");
        System.out.print("Option: ");

        this.scanner.nextLine();
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}
