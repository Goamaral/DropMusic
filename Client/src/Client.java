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

    User current_user;
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
    static final int ALBUM_SONG_ARTISTS = 18;
    static final int ALBUM_SONG_GENRES = 19;
    static final int ARTISTS = 20;
    static final int ARTIST = 21;
    static final int ARTIST_CREATE = 22;
    static final int ARTIST_UPDATE = 23;
    static final int ARTIST_DELETE = 24;

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
                    this.current_user = userInterface.login((User)resource);
                    break;
                case Client.REGISTER:
                    userInterface.register((User)resource);
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
                case Client.ALBUM_SONGS:
                    return this.albumInterface.songs((int)resource);
                case Client.ALBUM_SONG_CREATE:
                    this.albumInterface.song_create(this.current_album_id, (Song)resource);
                    break;
                case Client.ALBUM_SONG:
                    return this.albumInterface.song(this.current_album_id, (int)resource);
                case Client.ALBUM_SONG_UPDATE:
                    this.albumInterface.song_update(this.current_album_id, (Song)resource);
                    break;
                case Client.ALBUM_SONG_DELETE:
                    this.albumInterface.song_delete(this.current_album_id, (int)resource);
                    break;
                case Client.ARTISTS:
                    return this.artistInterface.index();
                case Client.ARTIST_CREATE:
                    this.artistInterface.create((Artist)resource);
                    break;
                case Client.ARTIST:
                    return this.artistInterface.read((int)resource);
                case Client.ARTIST_UPDATE:
                    this.artistInterface.update((Artist)resource);
                    break;
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
        }
    }

    // Start
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
            this.current_user = userInterface.login(user);
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
        System.out.println("[" + Client.ALBUMS + "] Albums");
        System.out.println("[" + Client.ARTISTS + "] Artists");
        System.out.println("[" + Client.START + "] Logout");
        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (!option.matches("(" + Client.ALBUMS + "|" + Client.START + "|" + Client.ARTISTS + ")")) {
            this.clearScreen();
            System.out.println("Errors:");
            System.out.println("-> Invalid option");
            return this.displayDashboard();
        }

        return Integer.parseInt(option);
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

        if (this.current_user.isEditor) {
            System.out.println("[C] Create album");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");
        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.DASHBOARD;

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
            this.albumInterface.create(album);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_CREATE, album);
        }

        return Client.ALBUMS;
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
        // TODO: Show artists
        // TODO: Show genres
        System.out.println("Release date: " + this.current_album.releaseDateString);
        System.out.println("[" + Client.ALBUM_CRITICS + "] Critics");
        System.out.println("[" + Client.ALBUM_SONGS + "] Songs");

        if (this.current_user.isEditor) {
            System.out.println("[" + Client.ALBUM_UPDATE + "] Edit album");
            System.out.println("[" + Client.ALBUM_DELETE + "] Delete album");
        }

        System.out.println("[B] Back");
        System.out.print("Option: ");

        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUMS;

        if (option.equals(Client.ALBUM_CRITICS)) return Client.ALBUM_CRITICS;
        if (option.equals(Client.ALBUM_SONGS)) return Client.ALBUM_SONGS;

        if (this.current_user.isEditor) {
            if (option.equals(Client.ALBUM_UPDATE)) return Client.ALBUM_UPDATE;
            if (option.equals(Client.ALBUM_DELETE)) return Client.ALBUM_DELETE;
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
            this.albumInterface.update(new_album);
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
            this.albumInterface.critic_create(critic);
        } catch(RemoteException re) {
            this.retry(Client.ALBUM_CRITIC_CREATE, critic);
        }

        return Client.ALBUM_CRITICS;
    }

    int displayAlbumCritic() throws InterruptedException, CustomException, NoSuchAlgorithmException {
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

                System.out.println("[" + song.id + "] " + song.name + " by " /*+ Show song artists*/);
            }
        }

        System.out.println("[B] Back");

        if (this.current_user.isEditor) System.out.println("[C] Add song");

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
            this.current_song = this.albumInterface.song(this.current_album_id, this.current_song_id);
        } catch (RemoteException re) {
            this.current_song = (Song) this.retry(Client.ALBUM_SONG, this.current_song_id);
        }

        System.out.println("Song");
        System.out.println("Name: " + this.current_song.name);
        System.out.println("Info: " + this.current_song.info);
        // TODO: Show song artists
        // TODO: Show song genres

        if (this.current_user.isEditor) {
            // TODO: System.out.println("[" + Client.ALBUM_SONG_ARTISTS + "] Artists");
            // TODO: System.out.println("[" + Client.ALBUM_SONG_GENRES + "] Genres");
            System.out.println("[U] Edit song");
            System.out.println("[D] Delete song");
        }

        System.out.println("[B] Back");

        System.out.print("Option: ");

        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ALBUM_SONGS;

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
            this.albumInterface.song_update(this.current_album_id, new_song);
        } catch (RemoteException re) {
            this.retry(Client.ALBUM_SONG_UPDATE, new_song);
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
            System.out.println("[" + artist.id + "] " + artist.name);
        }

        if (this.current_user.isEditor) {
            System.out.println("[C] Add artist");
        }

        System.out.println("[B] Back");
        System.out.print("Option: ");

        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.DASHBOARD;

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

        System.out.println("Create artist");

        System.out.print("Name: ");
        artist = new Artist(this.scanner.nextLine());

        try {
            this.artistInterface.create(artist);
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

        if (this.current_user.isEditor) {
            System.out.println("[" + Client.ARTIST_UPDATE + "] Edit artist");
            // TODO: System.out.println("[" + Client.ARTIST_DELETE + "] Delete artist");
        }

        System.out.println("[B] Back");
        System.out.print("Option: ");

        option = this.scanner.nextLine();

        if (option.equals("B")) return Client.ARTISTS;

        if (this.current_user.isEditor) {
            if (option.equals(Integer.toString(Client.ARTIST_UPDATE))) return Client.ARTIST_UPDATE;
            if (option.equals(Integer.toString(Client.ARTIST_DELETE))) return Client.ARTIST_DELETE;
        }

        this.clearScreen();
        System.out.println("Errors:");
        System.out.println("-> Invalid option");
        return this.displayArtist();
    }

    int displayArtistUpdate() throws InterruptedException, CustomException, NoSuchAlgorithmException {
        Artist artist;

        System.out.println("Edit artist");
        System.out.print("Name(" + this.current_artist.name + "): ");

        artist = new Artist(this.scanner.nextLine());
        artist.id = this.current_artist_id;

        try {
            this.artistInterface.update(artist);
        } catch (RemoteException re) {
            this.retry(Client.ARTIST_UPDATE, artist);
        }

        return Client.ARTIST;
    }

    // View helper
    void clearScreen() {
        System.out.print("\033[H\033[2J");
    }
}
