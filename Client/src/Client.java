import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
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
    ArtistInterface artistInterface;

    User current_user = null;
    int current_album_id;
    Album current_album;
    int current_critic_id;
    Critic current_critic;
    int current_song_id;
    Song current_song;
    int current_artist_id;
    Artist current_artist;

    int connectAttemps = 0;
    int maxAttemps = 30;
    int port1;
    int port2;
    TcpHandler tcpHandler;

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
    static final int ALBUM_SONGS = 9;
    static final int ALBUM_UPDATE = 10;
    static final int ALBUM_DELETE = 11;
    static final int ALBUM_CRITIC = 12;
    static final int ALBUM_CRITIC_CREATE = 13;
    static final int ALBUM_SONG_CREATE = 14;
    static final int ALBUM_SONG = 15;
    static final int ALBUM_SONG_UPDATE = 16;
    static final int ALBUM_SONG_DELETE = 17;
    static final int ALBUM_SONG_GENRES = 18;
    static final int ALBUM_SONG_GENRE_ADD = 19;
    static final int ALBUM_SONG_GENRE_CREATE = 20;
    static final int ALBUM_SONG_GENRE_REMOVE = 21;
    static final int ALBUM_SONG_ARTISTS = 22;
    static final int ARTISTS = 23;
    static final int ARTIST = 24;
    static final int ARTIST_CREATE = 25;
    static final int ARTIST_UPDATE = 26;
    static final int ARTIST_DELETE = 27;
    static final int GENRES = 28;
    static final int ALBUM_SONG_ARTIST_ADD = 29;
    static final int ALBUM_SONG_ARTIST_REMOVE = 30;
    static final int ALBUM_SONG_ARTIST_CREATE = 31;
    static final int ALBUM_ARTISTS = 32;
    static final int ALBUM_GENRES = 33;
    static final int PROMOTE_USER = 34;
    static final int NORMAL_USERS = 35;
    static final int SEARCH_ALBUM = 36;
    static final int SEARCH_ARTIST = 37;
    static final int ARTIST_SONGS = 38;

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        Client client = new Client();

        try {
            System.out.print("Server 1(<IP>:<PORT>): ");
            client.port1 = Integer.parseInt(client.scanner.nextLine());

            System.out.print("Server 2(<IP>:<PORT>): ");
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
            this.artistInterface = (ArtistInterface) registry.lookup("ArtistInterface");
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
                    this.current_user = userInterface.login((User)resource, this.tcpHandler.port);
                    break;
                case Client.REGISTER:
                    userInterface.register((User)resource);
                    break;
                case Client.ALBUMS:
                    return albumInterface.index();
                case Client.ALBUM_CREATE:
                    this.albumInterface.create(this.current_user.id, (Album)resource);
                    break;
                case Client.ALBUM:
                    return this.albumInterface.read((int)resource);
                case Client.ALBUM_UPDATE:
                    this.albumInterface.update(this.current_user.id, (Album)resource);
                    break;
                case Client.ALBUM_DELETE:
                    this.albumInterface.delete((int)resource);
                    break;
                case Client.ALBUM_CRITICS:
                    return albumInterface.critics((int)resource);
                case Client.ALBUM_CRITIC_CREATE:
                    this.albumInterface.critic_create(this.current_album_id, (Critic)resource);
                    break;
                case Client.ALBUM_CRITIC:
                    return this.albumInterface.critic((int)resource);
                case Client.ALBUM_SONGS:
                    return this.albumInterface.songs((int)resource);
                case Client.ALBUM_SONG_CREATE:
                    this.albumInterface.song_create(this.current_album_id, (Song)resource);
                    break;
                case Client.ALBUM_SONG:
                    return this.albumInterface.song((int)resource);
                case Client.ALBUM_SONG_UPDATE:
                    this.albumInterface.song_update((Song)resource);
                    break;
                case Client.ALBUM_SONG_DELETE:
                    this.albumInterface.song_delete(this.current_album_id, (int)resource);
                    break;
                case Client.ARTISTS:
                    return this.artistInterface.index();
                case Client.ARTIST_CREATE:
                    this.artistInterface.create(this.current_user.id, (Artist)resource);
                    break;
                case Client.ARTIST:
                    return this.artistInterface.read((int)resource);
                case Client.ARTIST_UPDATE:
                    this.artistInterface.update(this.current_user.id, (Artist)resource);
                    break;
                case Client.GENRES:
                    return this.albumInterface.genres_all();
                case Client.ALBUM_SONG_GENRE_ADD:
                    this.albumInterface.song_genre_add(this.current_song_id, (int)resource);
                    break;
                case Client.ALBUM_SONG_GENRE_CREATE:
                    this.albumInterface.song_genre_create((Genre)resource);
                    break;
                case Client.ALBUM_SONG_GENRES:
                    return this.albumInterface.song_genres(this.current_song);
                case Client.ALBUM_SONG_GENRE_REMOVE:
                    this.albumInterface.song_genre_delete(this.current_song_id, (int)resource);
                    break;
                case Client.ALBUM_SONG_ARTIST_ADD:
                    this.albumInterface.song_artist_add(this.current_song_id, (int)resource);
                    break;
                case Client.ALBUM_SONG_ARTISTS:
                    return this.albumInterface.song_artists(this.current_song);
                case Client.ALBUM_SONG_ARTIST_REMOVE:
                    this.albumInterface.song_artist_delete(this.current_song_id, (int)resource);
                    break;
                case Client.ALBUM_ARTISTS:
                    return this.albumInterface.artists((int)resource);
                case Client.ALBUM_GENRES:
                    return this.albumInterface.genres((int)resource);
                case Client.NORMAL_USERS:
                    return this.userInterface.normal_users();
                case Client.PROMOTE_USER:
                    this.userInterface.promote((int)resource);
                    break;
                case Client.SEARCH_ALBUM:
                    return this.albumInterface.search((String)resource);
                case Client.SEARCH_ARTIST:
                    return this.artistInterface.search((String)resource);
                case Client.ARTIST_SONGS:
                    return this.artistInterface.songs((int)resource);

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
                    this.redirect(this.displayRegister(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.START, ce);
                }
                break;
            case Client.LOGIN:
                try {
                    this.redirect(this.displayLogin(), null);
                } catch (CustomException ce) {
                    try {
                        this.tcpHandler.socket.close();
                    } catch (IOException e) {}
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
                    this.redirect(this.displayAlbumCreate(), null);
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
                    redirect(this.displayAlbumUpdate(), null);
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
                    this.redirect(this.displayAlbumCriticCreate(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUMS, ce);
                }
                break;
            case Client.ALBUM_CRITIC:
                try {
                    this.redirect(this.displayAlbumCritic(), null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUMS, ce);
                    } else {
                        this.redirect(Client.ALBUM_CRITICS, ce);
                    }
                }
                break;
            case Client.ALBUM_SONGS:
                try {
                    this.redirect(this.displayAlbumSongs(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONGS, ce);
                }
                break;
            case Client.ALBUM_SONG_CREATE:
                try {
                    this.redirect(this.displayAlbumSongCreate(), null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUMS, ce);
                    } else {
                        this.redirect(Client.ALBUM_SONGS, ce);
                    }
                }
                break;
            case Client.ALBUM_SONG:
                try {
                    this.redirect(this.displayAlbumSong(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONGS, ce);
                }
                break;
            case Client.ALBUM_SONG_UPDATE:
                try {
                    this.redirect(this.displayAlbumSongUpdate(), null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUMS, ce);
                    } else {
                        this.redirect(Client.ALBUM_SONG, ce);
                    }
                }
                break;
            case Client.ALBUM_SONG_DELETE:
                try {
                    try {
                        this.albumInterface.song_delete(this.current_album_id, this.current_song_id);
                    } catch (RemoteException re) {
                        this.retry(Client.ALBUM_SONG_DELETE, this.current_song_id);
                    }

                    this.redirect(Client.ALBUM_SONGS, null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONGS, ce);
                }
                break;
            case Client.ARTISTS:
                try {
                    this.redirect(this.displayArtists(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ARTISTS, ce);
                }
                break;
            case Client.ARTIST_CREATE:
                try {
                    this.redirect(this.displayArtistCreate(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ARTISTS, ce);
                }
                break;
            case Client.ARTIST:
                try {
                    this.redirect(this.displayArtist(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ARTISTS, ce);
                }
                break;
            case Client.ARTIST_UPDATE:
                try {
                    this.redirect(this.displayArtistUpdate(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ARTISTS, ce);
                }
                break;
            case Client.ARTIST_DELETE:
                try {
                    try {
                        this.artistInterface.delete(this.current_artist_id);
                    } catch (RemoteException re) {
                        this.retry(Client.ARTIST_DELETE, this.current_artist_id);
                    }

                    this.redirect(Client.ARTISTS, null);
                } catch (CustomException ce) {
                    this.redirect(Client.ARTISTS, ce);
                }
                break;
            case Client.ALBUM_SONG_GENRES:
                this.redirect(this.displayAlbumSongGenres(), null);
                break;
            case Client.ALBUM_SONG_GENRE_ADD:
                try {
                    this.redirect(this.displayAlbumSongGenreAdd(), null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 2) {
                        this.redirect(Client.ALBUMS, ce);
                    } else if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUM_SONGS, ce);
                    } else {
                        this.redirect(Client.ALBUM_SONG, ce);
                    }
                }
                break;
            case Client.ALBUM_SONG_GENRE_CREATE:
                try {
                    this.redirect(this.displayAlbumSongGenreCreate(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONG_GENRE_ADD, ce);
                }
                break;
            case Client.ALBUM_SONG_GENRE_REMOVE:
                try {
                    this.redirect(this.displayAlbumSongGenreRemove(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONG_GENRES, ce);
                }
                break;
            case Client.ALBUM_SONG_ARTISTS:
                this.redirect(this.displayAlbumSongArtists(), null);
                break;
            case Client.ALBUM_SONG_ARTIST_ADD:
                try {
                    this.redirect(this.displayAlbumSongArtistAdd(), null);
                } catch (CustomException ce) {
                    if (ce.extraFlag == 2) {
                        this.redirect(Client.ALBUMS, ce);
                    } else if (ce.extraFlag == 1) {
                        this.redirect(Client.ALBUM_SONGS, ce);
                    } else {
                        this.redirect(Client.ALBUM_SONG, ce);
                    }
                }
                break;
            case Client.ALBUM_SONG_ARTIST_CREATE:
                try {
                    this.redirect(this.displayAlbumSongArtistCreate(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONG_ARTIST_ADD, ce);
                }
                break;
            case Client.ALBUM_SONG_ARTIST_REMOVE:
                try {
                    this.redirect(this.displayAlbumSongArtistRemove(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.ALBUM_SONG_ARTISTS, ce);
                }
                break;
            case Client.PROMOTE_USER:
                try {
                    this.redirect(this.displayPromoteUser(), null);
                } catch (CustomException ce) {
                    this.redirect(Client.DASHBOARD, ce);
                }
                break;
            case Client.SEARCH_ALBUM:
                try {
                    this.redirect(this.displaySearchAlbum(), null);
                } catch (CustomException ce) {
                    this.redirect(this.ALBUMS, ce);
                }
                break;
            case Client.SEARCH_ARTIST:
                try {
                    this.redirect(this.displaySearchArtist(), null);
                } catch (CustomException ce) {
                    this.redirect(this.ARTISTS, ce);
                }
                break;
        }
    }

    // Start
    int displayStart() {
        String option;

        System.out.println("Start");
        System.out.println("[R] Create account");
        System.out.println("[L] Log in");
        System.out.println("[B] Exit");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("R")) return Client.REGISTER;
        if (option.equals("L")) return Client.LOGIN;
        if (option.equals("B")) return Client.EXIT;

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayStart();
    }

    // Auth
    int displayLogin() throws NoSuchAlgorithmException, InterruptedException, CustomException {
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
            this.tcpHandler = new TcpHandler(this);
            this.current_user = userInterface.login(user, this.tcpHandler.port);
            this.tcpHandler.start();
        } catch (RemoteException re) {
            this.retry(Client.LOGIN, user);
        }

        return Client.DASHBOARD;
    }

    int displayRegister() throws NoSuchAlgorithmException, CustomException, InterruptedException {
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

        return Client.START;
    }

    // Dashboard
    int displayDashboard() {
        String option;

        System.out.println("Dashboard");
        System.out.println("[AL] Albums");
        System.out.println("[AR] Artists");

        if (this.current_user.isEditor) {
            System.out.println("[P] Promote user to editor");
        }

        System.out.println("[B] Logout");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("AL")) return Client.ALBUMS;
        if (option.equals("AR")) return Client.ARTISTS;
        if (option.equals("B")) {
            try {
                this.tcpHandler.server.close();
                this.tcpHandler.socket.close();
                this.tcpHandler.join(1000);
            } catch (InterruptedException | IOException e) {}

            return Client.START;
        }

        if (this.current_user.isEditor) {
            if (option.equals("P")) return Client.PROMOTE_USER;
        }

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayDashboard();
    }

    // Albums
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

        if (this.current_user.isEditor) System.out.println("[C] Create album");

        System.out.println("[S] Search songs");
        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.DASHBOARD;
        if (option.equals("S")) return Client.SEARCH_ALBUM;

        if (this.current_user.isEditor) {
            if (option.equals("C")) return Client.ALBUM_CREATE;
        }

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

    int displayAlbumCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
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
            this.albumInterface.create(this.current_user.id, album);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_CREATE, album);
        }

        return Client.ALBUMS;
    }

    int displayAlbum() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String option;
        String artists;
        String genres;

        try {
            this.current_album = this.albumInterface.read(this.current_album_id);
        } catch (RemoteException re) {
            this.current_album = (Album)this.retry(Client.ALBUM, this.current_album_id);
        }

        try {
            artists = this.albumInterface.artists(this.current_album_id);
        } catch (RemoteException re) {
            artists = (String)this.retry(Client.ALBUM_ARTISTS, this.current_album_id);
        }

        try {
            genres = this.albumInterface.genres(this.current_album_id);
        } catch (RemoteException re) {
            genres = (String)this.retry(Client.ALBUM_GENRES, this.current_album_id);
        }

        System.out.println("Album");
        System.out.println("Name: " + this.current_album.name);
        System.out.println("Rating: " + this.current_album.getRating() + "/5");
        System.out.println("Info: " + this.current_album.info);
        System.out.println("Artists: " + artists);
        System.out.println("Genres: " + genres);
        System.out.println("Release date: " + this.current_album.releaseDateString);
        System.out.println("[C] Critics");
        System.out.println("[S] Songs");

        if (this.current_user.isEditor) {
            System.out.println("[U] Edit album");
            System.out.println("[D] Delete album");
        }

        System.out.println("[B] Back");
        System.out.print("Option: ");

        option = this.scanner.nextLine();

        if (option.equals("C")) return Client.ALBUM_CRITICS;
        if (option.equals("S")) return Client.ALBUM_SONGS;
        if (option.equals("B")) return Client.ALBUMS;

        if (this.current_user.isEditor) {
            if (option.equals("U")) return Client.ALBUM_UPDATE;
            if (option.equals("D")) return Client.ALBUM_DELETE;
        }

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayAlbum();
    }

    int displayAlbumUpdate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Album new_album;
        String name, info, releaseDate;

        System.out.println("Edit album");
        System.out.println("Leave fields empty if you don't want to change them");

        System.out.print("Name(" + this.current_album.name + "): ");
        name = this.scanner.nextLine();
        if (name.length() == 0) name = this.current_album.name;

        System.out.print("Info(" + this.current_album.info + "): ");
        info = this.scanner.nextLine();
        if (info.length() == 0) info = this.current_album.info;

        System.out.print("Release date(" + this.current_album.releaseDateString + "): ");
        releaseDate = this.scanner.nextLine();
        if (releaseDate.length() == 0) releaseDate = this.current_album.releaseDateString;

        new_album = new Album(name, info, releaseDate);
        new_album.id = this.current_album_id;

        try {
            this.albumInterface.update(this.current_user.id, new_album);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_UPDATE, new_album);
        }

        return Client.ALBUM;
    }

    // Album critics
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
            for (Critic critic : critics) {
                System.out.println("[" + critic.id + "] By " + critic.author.username + " with " + critic.rating + "/5 rating");
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

    int displayAlbumCriticCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
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
            this.albumInterface.critic_create(this.current_album_id, critic);
        } catch(RemoteException re) {
            this.retry(Client.ALBUM_CRITIC_CREATE, critic);
        }

        return Client.ALBUM_CRITICS;
    }

    int displayAlbumCritic() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        try {
            this.current_critic = this.albumInterface.critic(this.current_critic_id);
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

        return Client.ALBUM_CRITICS;
    }

    // Album songs
    int displayAlbumSongs() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Song> songs;
        String option;

        try {
            songs = this.albumInterface.songs(this.current_album_id);
        } catch (RemoteException re) {
            songs = (ArrayList<Song>) this.retry(Client.ALBUM_SONGS, null);
        }

        System.out.println("Album songs");
        if (songs.size() == 0) {
            System.out.println("No songs available");
        } else {
            for (Song song : songs) {
                if (song == null) continue;

                System.out.println("[" + song.id + "] " + song.name + " by " + song.artists);
            }
        }

        if (this.current_user.isEditor) System.out.println("[C] Add song");

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM;

        if (this.current_user.isEditor && option.equals("C")) return Client.ALBUM_SONG_CREATE;

        try {
            this.current_song_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbumSongs();
        }

        return Client.ALBUM_SONG;
    }

    int displayAlbumSongCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Song song;
        String name, info;

        System.out.println("Add song");

        System.out.print("Name: ");
        name = this.scanner.nextLine();

        System.out.print("Info: ");
        info = this.scanner.nextLine();

        song = new Song(name, info);

        try {
            this.albumInterface.song_create(this.current_album_id, song);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_CREATE, song);
        }

        return Client.ALBUM_SONGS;
    }

    int displayAlbumSong() throws CustomException, NoSuchAlgorithmException, InterruptedException {
        String option;

        try {
            this.current_song = this.albumInterface.song(this.current_song_id);
        } catch (RemoteException re) {
            this.current_song = (Song) this.retry(Client.ALBUM_SONG, this.current_song_id);
        }

        System.out.println("Song");
        System.out.println("Name: " + this.current_song.name);
        System.out.println("Info: " + this.current_song.info);
        System.out.println("Artists: " + this.current_song.artists);
        System.out.println("Genres: " + this.current_song.genres);

        if (this.current_user.isEditor) {
            System.out.println("[A] Artists");
            System.out.println("[G] Genres");
            System.out.println("[U] Edit song");
            System.out.println("[D] Delete song");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONGS;
        if (option.equals("G")) return Client.ALBUM_SONG_GENRES;
        if (option.equals("A")) return Client.ALBUM_SONG_ARTISTS;

        if (this.current_user.isEditor) {
            if (option.equals("U")) return Client.ALBUM_SONG_UPDATE;
            if (option.equals("D")) return Client.ALBUM_SONG_DELETE;
        }

        throw new CustomException("Invalid option");
    }

    int displayAlbumSongUpdate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Song new_song;
        String name, info;

        System.out.println("Edit song");
        System.out.println("Leave fields empty if you don't want to change them");

        System.out.print("Name(" + this.current_song.name + "): ");
        name = this.scanner.nextLine();
        if (name.length() == 0) name = this.current_song.name;

        System.out.print("Info(" + this.current_song.info + "): ");
        info = this.scanner.nextLine();
        if (info.length() == 0) info = this.current_song.info;

        new_song = new Song(name, info);
        new_song.id = this.current_song.id;

        try {
            this.albumInterface.song_update(new_song);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_UPDATE, new_song);
        }

        return Client.ALBUM_SONG;
    }

    // Album song genres
    int displayAlbumSongGenres() {
        String option;

        System.out.println("Song genres");
        System.out.println("Genres: " + this.current_song.genres);

        if (this.current_user.isEditor) {
            System.out.println("[C] Add genre");
            System.out.println("[D] Remove genre");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONG;

        if (this.current_user.isEditor) {
            if (option.equals("C")) return Client.ALBUM_SONG_GENRE_ADD;
            if (option.equals("D")) return Client.ALBUM_SONG_GENRE_REMOVE;
        }

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayAlbumSongGenres();
    }

    int displayAlbumSongGenreAdd() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Genre> genres;
        String option;
        int genre_id;

        try {
            genres = this.albumInterface.genres_all();
        } catch (RemoteException re) {
            genres = (ArrayList<Genre>)this.retry(Client.GENRES, null);
        }

        System.out.println("Add genre:");

        if (genres.size() == 0) {
            System.out.println("No genres available");
        } else {
            for (Genre genre : genres) {
                if (genre == null) continue;

                System.out.println("[" + genre.id + "] " + genre.name);
            }
        }


        if (this.current_user.isEditor) System.out.println("[C] Create genre");

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONG_GENRES;
        if (this.current_user.isEditor && option.equals("C")) return Client.ALBUM_SONG_GENRE_CREATE;

        try {
            genre_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbumSongGenreAdd();
        }

        try {
            this.albumInterface.song_genre_add(this.current_song_id, genre_id);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_GENRE_ADD, genre_id);
        }

        return Client.ALBUM_SONG;
    }

    int displayAlbumSongGenreCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Genre genre;

        System.out.println("Create genre");
        System.out.print("Name: ");

        genre = new Genre(this.scanner.nextLine());

        try {
            this.albumInterface.song_genre_create(genre);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_GENRE_CREATE, genre);
        }

        return Client.ALBUM_SONG_GENRE_ADD;
    }

    int displayAlbumSongArtistRemove() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Artist> artists;
        String option;
        int artist_id;

        try {
            artists = this.albumInterface.song_artists(this.current_song);
        } catch(RemoteException re) {
            artists = (ArrayList<Artist>) this.retry(Client.ALBUM_SONG_ARTISTS, null);
        }

        System.out.println("Remove artist");

        if (artists.size() == 0) {
            System.out.println("No artists available");
        } else {
            for (Artist artist : artists) {
                System.out.println("[" + artist.id + "] " + artist.name);
            }
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONG_ARTISTS;

        try {
            artist_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbumSongArtistRemove();
        }

        try {
            this.albumInterface.song_artist_delete(this.current_song_id, artist_id);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_ARTIST_REMOVE, artist_id);
        }

        return Client.ALBUM_SONG;
    }

    // Album song artists
    int displayAlbumSongArtists() {
        String option;

        System.out.println("Song artists");
        System.out.println("Artists: " + this.current_song.artists);

        if (this.current_user.isEditor) {
            System.out.println("[C] Add artist");
            System.out.println("[D] Remove artist");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONG;

        if (this.current_user.isEditor) {
            if (option.equals("C")) return Client.ALBUM_SONG_ARTIST_ADD;
            if (option.equals("D")) return Client.ALBUM_SONG_ARTIST_REMOVE;
        }

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayAlbumSongArtists();
    }

    int displayAlbumSongArtistAdd() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Artist> artists;
        String option;
        int artist_id;

        try {
            artists = this.artistInterface.index();
        } catch (RemoteException re) {
            artists = (ArrayList<Artist>)this.retry(Client.ARTISTS, null);
        }

        System.out.println("Add artist:");

        if (artists.size() == 0) {
            System.out.println("No artists available");
        } else {
            for (Artist artist : artists) {
                if (artist == null) continue;

                System.out.println("[" + artist.id + "] " + artist.name);
            }
        }


        if (this.current_user.isEditor) System.out.println("[C] Create artist");

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONG_ARTISTS;
        if (this.current_user.isEditor && option.equals("C")) return Client.ALBUM_SONG_ARTIST_CREATE;

        try {
            artist_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbumSongGenreAdd();
        }

        try {
            this.albumInterface.song_artist_add(this.current_song_id, artist_id);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_ARTIST_ADD, artist_id);
        }

        return Client.ALBUM_SONG;
    }

    int displayAlbumSongArtistCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Artist artist;
        String name, info;

        System.out.println("Create artist");

        System.out.print("Name: ");
        name = this.scanner.nextLine();

        System.out.print("Info: ");
        info = this.scanner.nextLine();

        artist = new Artist(name, info);

        try {
            this.artistInterface.create(this.current_user.id, artist);
        } catch (RemoteException re) {
            this.retry(Client.ARTIST_CREATE, artist);
        }

        return Client.ALBUM_SONG_ARTIST_ADD;
    }

    int displayAlbumSongGenreRemove() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Genre> genres;
        String option;
        int genre_id;

        try {
            genres = this.albumInterface.song_genres(this.current_song);
        } catch(RemoteException re) {
            genres = (ArrayList<Genre>) this.retry(Client.ALBUM_SONG_GENRES, null);
        }

        System.out.println("Remove genre");

        if (genres.size() == 0) {
            System.out.println("No genres available");
        } else {
            for (Genre genre : genres) {
                System.out.println("[" + genre.id + "] " + genre.name);
            }
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONG_GENRES;

        try {
            genre_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayAlbumSongGenreRemove();
        }

        try {
            this.albumInterface.song_genre_delete(this.current_song_id, genre_id);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_GENRE_REMOVE, genre_id);
        }

        return Client.ALBUM_SONG;
    }

    // Artists
    int displayArtists() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<Artist> artists;
        String option;

        try {
            artists = this.artistInterface.index();
        } catch (RemoteException re) {
            artists = (ArrayList<Artist>) this.retry(Client.ARTISTS, null);
        }

        System.out.println("Artists");

        for(Artist artist : artists) {
            if (artist == null) continue;

            System.out.println("[" + artist.id + "] " + artist.name);
        }

        if (this.current_user.isEditor) {
            System.out.println("[C] Add artist");
        }

        System.out.println("[S] Search songs");
        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.DASHBOARD;
        if (option.equals("S")) return Client.SEARCH_ARTIST;

        if (this.current_user.isEditor && option.equals("C")) return Client.ARTIST_CREATE;

        try {
            this.current_artist_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayArtists();
        }

        return Client.ARTIST;
    }

    int displayArtistCreate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Artist artist;
        String name, info;

        System.out.println("Create artist");

        System.out.print("Name: ");
        name = this.scanner.nextLine();

        System.out.print("Info: ");
        info = this.scanner.nextLine();

        artist = new Artist(name, info);

        try {
            this.artistInterface.create(this.current_user.id, artist);
        } catch(RemoteException re) {
            this.retry(Client.ARTIST_CREATE, artist);
        }

        return Client.ARTISTS;
    }

    int displayArtist() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String option;

        try {
            this.current_artist = this.artistInterface.read(this.current_artist_id);
        } catch (RemoteException re) {
            this.current_artist = (Artist)this.retry(Client.ARTIST, this.current_artist_id);
        }

        System.out.println("Artist");
        System.out.println("Name: " + this.current_artist.name);
        System.out.println("Info: " + this.current_artist.info);

        if (this.current_user.isEditor) {
            System.out.println("[U] Edit artist");
            System.out.println("[D] Delete artist");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ARTISTS;

        if (this.current_user.isEditor) {
            if (option.equals("U")) return Client.ARTIST_UPDATE;
            if (option.equals("D")) return Client.ARTIST_DELETE;
        }

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayArtist();
    }

    int displayArtistUpdate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Artist artist;
        String name;
        String info;

        System.out.println("Edit artist");
        System.out.println("Leave fields empty if you don't want to change them");

        System.out.print("Name(" + this.current_artist.name + "): ");
        name = this.scanner.nextLine();
        if (name.length() == 0) name = this.current_artist.name;

        System.out.print("Info(" + this.current_artist.info + "): ");
        info = this.scanner.nextLine();
        if (info.length() == 0) info = this.current_artist.info;

        artist = new Artist(name, info);
        artist.id = this.current_artist_id;

        try {
            this.artistInterface.update(this.current_user.id, artist);
        } catch (RemoteException re) {
            this.retry(Client.ARTIST_UPDATE, artist);
        }

        return Client.ARTIST;
    }

    // Promote User
    int displayPromoteUser() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        ArrayList<User> normal_users;
        String option;
        int user_id;

        try {
            normal_users = this.userInterface.normal_users();
        } catch (RemoteException re) {
            normal_users = (ArrayList<User>)this.retry(Client.NORMAL_USERS, null);
        }

        System.out.println("Promote user to editor");

        if (normal_users.size() == 0) {
            System.out.println("No normal users available");
        } else {
            for (User user : normal_users) {
                System.out.println("[" + user.id + "] " + user.username);
            }
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.DASHBOARD;

        try {
            user_id = Integer.parseInt(option);
        } catch (NumberFormatException nfe) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayPromoteUser();
        }

        try {
            this.userInterface.promote(user_id);
        } catch (RemoteException re) {
            this.retry(Client.PROMOTE_USER, user_id);
        }

        return Client.DASHBOARD;
    }

    // Search album
    int displaySearchAlbum() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String query;
        ArrayList<Song> songs;

        System.out.println("Search song in ablum");
        System.out.print("Album name: ");
        query = this.scanner.nextLine();

        try {
            this.current_album = this.albumInterface.search(query);
        } catch (RemoteException re) {
            this.current_album = (Album) this.retry(Client.SEARCH_ALBUM, query);
        }

        this.current_album_id = this.current_album.id;

        try {
            songs = this.albumInterface.songs(this.current_album_id);
        } catch (RemoteException re) {
            songs = (ArrayList<Song>)this.retry(Client.ALBUM_SONGS, this.current_album_id);
        }

        while (true) {
            if (songs.size() == 0) {
                System.out.println("No songs available");
            } else {
                for (Song song : songs) {
                    System.out.println("[" + song.id + "] " + song.name);
                }
            }

            System.out.println("[B] Back");

            System.out.print("Option: ");
            query = this.scanner.nextLine();

            if (query.equals("B")) return Client.ALBUMS;

            try {
                this.current_song_id = Integer.parseInt(query);
                break;
            } catch (NumberFormatException nfe) {
                this.clearScreen();
                System.out.println("Errors:");
                System.out.println("-> Invalid option");
            }
        }

        return Client.ALBUM_SONG;
    }

    // Search artist
    int displaySearchArtist() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        String query;
        ArrayList<Song> songs;

        System.out.println("Artist songs");
        System.out.print("Artist name: ");
        query = this.scanner.nextLine();

        try {
            this.current_artist = this.artistInterface.search(query);
        } catch (RemoteException re) {
            this.current_artist = (Artist) this.retry(Client.SEARCH_ARTIST, query);
        }

        this.current_artist_id = this.current_artist.id;

        try {
            songs = this.artistInterface.songs(this.current_artist_id);
        } catch (RemoteException re) {
            songs = (ArrayList<Song>)this.retry(Client.ARTIST_SONGS, this.current_artist_id);
        }

        while (true) {
            if (songs.size() == 0) {
                System.out.println("No songs available");
            } else {
                for (Song song : songs) {
                    System.out.println("[" + song.id + "] " + song.name);
                }
            }

            System.out.println("[B] Back");

            System.out.print("Option: ");
            query = this.scanner.nextLine();

            if (query.equals("B")) return Client.ARTISTS;

            try {
                this.current_song_id = Integer.parseInt(query);

                try {
                    this.current_song = this.albumInterface.song(this.current_song_id);
                } catch(RemoteException re) {
                    this.current_song = (Song)this.retry(Client.ALBUM_SONG, this.current_song_id);
                }

                this.current_album_id = this.current_song.album_id;
                break;
            } catch (NumberFormatException nfe) {
                this.clearScreen();
                System.out.println("Errors:");
                System.out.println("-> Invalid option");
            }
        }

        return Client.ALBUM_SONG;
    }

    // View helper
    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}

class TcpHandler extends Thread {
    ServerSocket socket;
    int port = 10000;
    Socket server;
    Client client;

    TcpHandler(Client client) {
        this.client = client;

        while (true) {
            try {
                this.socket = new ServerSocket(this.port);
                break;
            } catch (IOException e) {
                this.port += 1;
            }
        }
    }

    public void run() {
            try {
                while (true) {
                    this.server = this.socket.accept();

                    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    String data;

                    while ((data = in.readLine()) != null) {
                        if (data.contains("You have been promoted")) client.current_user.becomeEditor();
                        System.out.println("\n" + data);
                    }

                    this.server.close();
                }
            } catch (IOException e) {
                /* Wait for server to reconnect */
            }
    }
}
