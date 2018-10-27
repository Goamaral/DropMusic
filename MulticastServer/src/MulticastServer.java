import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.ArrayList;
import java.net.*;
import java.util.Scanner;

public class MulticastServer {
    Database database;
    InetAddress MULTICAST_ADDRESS;
    int MULTICAST_SOURCE_PORT;
    int MULTICAST_TARGET_PORT;

    public static void main(String[] args) {
        MulticastServer multicastServer = new MulticastServer();
        multicastServer.database = new Database();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Running multicast");

        try {
            System.out.print("Multicast address: ");
            multicastServer.MULTICAST_ADDRESS = InetAddress.getByName(scanner.nextLine());

            System.out.print("My multicast port: ");
            multicastServer.MULTICAST_SOURCE_PORT = Integer.parseInt(scanner.nextLine());

            System.out.print("Rmi server multicast port: ");
            multicastServer.MULTICAST_TARGET_PORT = Integer.parseInt(scanner.nextLine());
        } catch (UnknownHostException e) {
            System.out.println("Invalid ports or IPs");
            System.exit(0);
        }

        RecieverSocketHandler recieverSocket = new RecieverSocketHandler(multicastServer);
        recieverSocket.start();
    }
}

class RecieverSocketHandler extends Thread {
    Database database;
    MulticastServer multicastServer;

    RecieverSocketHandler(MulticastServer multicast){
        this.database = multicast.database;
        this.multicastServer = multicast;
    }

    public void run() {
        MulticastSocket receiver_socket = null;
        Request request = null;

        try {
            // Receive
            receiver_socket = new MulticastSocket(multicastServer.MULTICAST_SOURCE_PORT);  // create socket and bind it
            InetAddress group = multicastServer.MULTICAST_ADDRESS;
            receiver_socket.joinGroup(group);

            while (true) {
                byte[] request_buffer = new byte[5000];
                DatagramPacket request_packet = new DatagramPacket(request_buffer, request_buffer.length);
                receiver_socket.receive(request_packet);

                String request_string = new String(request_packet.getData(), 0, request_packet.getLength());
                byte request_bytes[] = Base64.decode(request_string);
                ByteArrayInputStream bAIS = new ByteArrayInputStream(request_bytes);
                ObjectInputStream oIS = new ObjectInputStream(bAIS);
                request = (Request)oIS.readObject();

                System.out.print("Action " + request.type + " ");

                switch (request.type) {
                    case "user_findByUsername": {
                        // Throws Custom Exception

                        User fetched_user = this.database.user_findByUsername((String) request.data);
                        sendPacket(new Response(request.id, fetched_user));

                        System.out.println("success");
                        break;
                    }
                    case "user_create": {
                        // Throws Custom Exception
                        this.database.user_create((User) request.data);
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "normal_users": {
                        // Does not throw Custom Exception
                        ArrayList<User> result = this.database.normal_users();
                        sendPacket(new Response(request.id, result));

                        System.out.println("success");
                        break;
                    }
                    case "user_promote": {
                        // Throws Custom Exception
                        this.database.user_promote((Integer) request.data);
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "user_find":
                        // Throws Custom Exception
                        User user = this.database.user_find((int) request.data);
                        sendPacket(new Response(request.id, user));

                        System.out.println("success");
                        break;
                    case "user_all":
                        // Does not throw Custom Exception
                        ArrayList<User> users = this.database.user_all();
                        sendPacket(new Response(request.id, users));

                        System.out.println("success");
                        break;
                    case "artist_all": {
                        // Does not throw Custom Exception
                        ArrayList<Artist> artists = this.database.artist_all();
                        sendPacket(new Response(request.id, artists));

                        System.out.println("success");
                        break;
                    }
                    case "artist_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.artist_create((int) args.get(0), (Artist) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "artist_find": {
                        // Throws Custom Exception
                        Artist artist = this.database.artist_find((Integer) request.data);
                        sendPacket(new Response(request.id, artist));

                        System.out.println("success");
                        break;
                    }
                    case "artist_update": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        ArrayList<Integer> editor_ids = this.database.artist_update((int) args.get(0), (Artist) args.get(1));
                        sendPacket(new Response(request.id, editor_ids));

                        System.out.println("success");
                        break;
                    }
                    case "artist_delete": {
                        // Does not throw Custom Exception
                        this.database.artist_delete((Integer) request.data);
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "artist_songs": {
                        // Throws Custom Exception
                        ArrayList<Song> songs = this.database.artist_songs((Integer) request.data);
                        sendPacket(new Response(request.id, songs));

                        System.out.println("success");
                        break;
                    }
                    case "album_all": {
                        // Throws Custom Exception
                        ArrayList<Album> albums = this.database.album_all();
                        sendPacket(new Response(request.id, albums));

                        System.out.println("success");
                        break;
                    }
                    case "album_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        Album new_album = this.database.album_create((int) args.get(0), (Album) args.get(1));
                        sendPacket(new Response(request.id, new_album));

                        System.out.println("success");
                        break;
                    }
                    case "album_find": {
                        // Throws Custom Exception
                        Album album = this.database.album_find((int) request.data);
                        sendPacket(new Response(request.id, album));

                        System.out.println("success");
                        break;
                    }
                    case "album_update": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        ArrayList<Integer> editor_ids = this.database.album_update((int) args.get(0), (Album) args.get(1));
                        sendPacket(new Response(request.id, editor_ids));

                        System.out.println("success");
                        break;
                    }
                    case "album_delete": {
                        // Does not throw Custom Exception
                        this.database.album_delete((Integer) request.data);
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "album_artists": {
                        // Throws Custom Exception
                        String new_string = this.database.album_artists((int) request.data);
                        sendPacket(new Response(request.id, new_string));

                        System.out.println("success");
                        break;
                    }
                    case "album_genres": {
                        // Throws Custom Exception
                        String new_string = this.database.album_genres((int) request.data);
                        sendPacket(new Response(request.id, new_string));

                        System.out.println("success");
                        break;
                    }
                    case "album_critics": {
                        // Throws Custom Exception
                        ArrayList<Critic> critics = this.database.album_critics((int) request.data);
                        sendPacket(new Response(request.id, critics));

                        System.out.println("success");
                        break;
                    }
                    case "album_critic_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.album_critic_create((int) args.get(0), (Critic) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "album_critic_find": {
                        // Throws Custom Exception
                        Critic critic = this.database.album_critic_find((int) request.data);
                        sendPacket(new Response(request.id, critic));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_all": {
                        // Throws Custom Exception
                        ArrayList<Song> album_songs = this.database.album_song_all((int) request.data);
                        sendPacket(new Response(request.id, album_songs));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.album_song_create((int) args.get(0), (Song) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "user_uploads": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        ArrayList<StoredSong> uploads = this.database.user_uploads((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, uploads));

                        System.out.println("success");
                        break;
                    }
                    case "song_find": {
                        // Throws Custom Exception
                        Song song = this.database.song_find((Integer) request.data);
                        sendPacket(new Response(request.id, song));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_update": {
                        // Throws Custom Exception
                        this.database.album_song_update((Song) request.data);
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_delete": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.album_song_delete((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "request_song_upload": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        int port = this.database.requestSongUpload((int) args.get(0), (int) args.get(1), (String) args.get(2));
                        IpPort ipPort = new IpPort(InetAddress.getLocalHost(), port);
                        sendPacket(new Response(request.id, ipPort));

                        System.out.println("success");
                        break;
                    }
                    case "song_downloads": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        ArrayList<StoredSong> songs = this.database.song_downloads((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, songs));

                        System.out.println("success");
                        break;
                    }
                    case "request_song_download": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        int port = this.database.requestSongDownload((int) args.get(0), (int) args.get(1));
                        IpPort ipPort =new IpPort(InetAddress.getLocalHost(), port);
                        sendPacket(new Response(request.id, ipPort));

                        System.out.println("success");
                        break;
                    }
                    case "song_share": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.song_share((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "genre_all": {
                        // Throws Custom Exception
                        ArrayList<Genre> genres = this.database.genre_all();
                        sendPacket(new Response(request.id, genres));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_genre_add": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_genre_add((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "genre_create": {
                        // Throws Custom Exception
                        this.database.genre_create((Genre) request.data);
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "genre_find": {
                        // Throws Custom Exception
                        Genre genre = this.database.genre_find((Integer) request.data);
                        sendPacket(new Response(request.id, genre));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_genre_remove": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_genre_remove((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, true));
                        break;
                    }
                    case "album_song_artist_add": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_artist_add((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_artist_remove": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_artist_remove((int) args.get(0), (int) args.get(1));
                        sendPacket(new Response(request.id, true));

                        System.out.println("success");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomException ce) {
            CustomException _ce = ce;

            while(true) {
                try {
                    String response = Serializer.serialize(_ce);
                    this.sendPacket(new Response(request.id, response));
                    break;
                } catch (CustomException ce1) {
                    System.out.println(ce1.getMessage());
                    _ce = ce1;
                }
            }

            System.out.println("failure");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (receiver_socket != null) {
            receiver_socket.close();
        }
    }

    public void sendPacket(Response response) throws CustomException {
        MulticastSocket sendingSocket = null;
        String stringData = Serializer.serialize(response);

        try {
            sendingSocket = new MulticastSocket();
            InetAddress group = multicastServer.MULTICAST_ADDRESS;
            byte[] buffer = stringData.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastServer.MULTICAST_TARGET_PORT);
            sendingSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendingSocket.close();
        }
    }

}