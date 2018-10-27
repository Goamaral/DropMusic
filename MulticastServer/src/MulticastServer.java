import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.ArrayList;
import java.net.*;

public class MulticastServer {
    Database database;

    public static void main(String[] args) {
        MulticastServer multicastServer = new MulticastServer();
        multicastServer.database = new Database();

        RecieverSocketHandler recieverSocket = new RecieverSocketHandler(multicastServer);
        recieverSocket.start();

        System.out.println("Running");
    }
}

class RecieverSocketHandler extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int MULTICAST_SOURCE_PORT = 30000;
    private int MULTICAST_TARGET_PORT = 20000;
    Database database;

    RecieverSocketHandler(MulticastServer multicast){
        this.database = multicast.database;
    }

    public void run() {
        MulticastSocket receiver_socket = null;

        try {
            // Receive
            receiver_socket = new MulticastSocket(MULTICAST_SOURCE_PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            receiver_socket.joinGroup(group);

            while (true) {
                byte[] request_buffer = new byte[5000];
                DatagramPacket request_packet = new DatagramPacket(request_buffer, request_buffer.length);
                receiver_socket.receive(request_packet);

                String request_string = new String(request_packet.getData(), 0, request_packet.getLength());
                byte request_bytes[] = Base64.decode(request_string);
                ByteArrayInputStream bAIS = new ByteArrayInputStream(request_bytes);
                ObjectInputStream oIS = new ObjectInputStream(bAIS);
                Request request = (Request)oIS.readObject();

                System.out.print("Action " + request.type + " ");

                switch (request.type) {
                    case "user_findByUsername": {
                        // Throws Custom Exception

                        User fetched_user = this.database.user_findByUsername((String) request.data);
                        sendPacket(Serializer.serialize(fetched_user));

                        System.out.println("success");
                        break;
                    }
                    case "user_create": {
                        // Throws Custom Exception
                        Boolean result = this.database.user_create((User) request.data);
                        sendPacket(Serializer.serialize(result));

                        System.out.println("success");
                        break;
                    }
                    case "normal_users": {
                        // Does not throw Custom Exception
                        ArrayList<User> result = this.database.normal_users();
                        sendPacket(Serializer.serialize(result));

                        System.out.println("success");
                        break;
                    }
                    case "user_promote": {
                        // Throws Custom Exception
                        this.database.user_promote((Integer) request.data);
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "user_find":
                        // Throws Custom Exception
                        User user = this.database.user_find((int) request.data);
                        sendPacket(Serializer.serialize(user));

                        System.out.println("success");
                        break;
                    case "user_all":
                        // Does not throw Custom Exception
                        ArrayList<User> users = this.database.user_all();
                        sendPacket(Serializer.serialize(users));

                        System.out.println("success");
                        break;
                    case "artist_all": {
                        // Does not throw Custom Exception
                        ArrayList<Artist> artists = this.database.artist_all();
                        sendPacket(Serializer.serialize(artists));

                        System.out.println("success");
                        break;
                    }
                    case "artist_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.artist_create((int) args.get(0), (Artist) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "artist_find": {
                        // Throws Custom Exception
                        Artist artist = this.database.artist_find((Integer) request.data);
                        sendPacket(Serializer.serialize(artist));

                        System.out.println("success");
                        break;
                    }
                    case "artist_update": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        ArrayList<Integer> editor_ids = this.database.artist_update((int) args.get(0), (Artist) args.get(1));
                        sendPacket(Serializer.serialize(editor_ids));

                        System.out.println("success");
                        break;
                    }
                    case "artist_delete": {
                        // Does not throw Custom Exception
                        this.database.artist_delete((Integer) request.data);
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "artist_songs": {
                        // Throws Custom Exception
                        ArrayList<Song> songs = this.database.artist_songs((Integer) request.data);
                        sendPacket(Serializer.serialize(songs));

                        System.out.println("success");
                        break;
                    }
                    case "album_all": {
                        // Throws Custom Exception
                        ArrayList<Album> albums = this.database.album_all();
                        sendPacket(Serializer.serialize(albums));

                        System.out.println("success");
                        break;
                    }
                    case "album_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        Album new_album = this.database.album_create((int) args.get(0), (Album) args.get(1));
                        sendPacket(Serializer.serialize(new_album));

                        System.out.println("success");
                        break;
                    }
                    case "album_find": {
                        // Throws Custom Exception
                        Album album = this.database.album_find((int) request.data);
                        sendPacket(Serializer.serialize(album));

                        System.out.println("success");
                        break;
                    }
                    case "album_update": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        ArrayList<Integer> editor_ids = this.database.album_update((int) args.get(0), (Album) args.get(1));
                        sendPacket(Serializer.serialize(editor_ids));

                        System.out.println("success");
                        break;
                    }
                    case "album_delete": {
                        // Does not throw Custom Exception
                        this.database.album_delete((Integer) request.data);
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "album_artists": {
                        // Throws Custom Exception
                        String new_string = this.database.album_artists((int) request.data);
                        sendPacket(Serializer.serialize(new_string));

                        System.out.println("success");
                        break;
                    }
                    case "album_genres": {
                        // Throws Custom Exception
                        String new_string = this.database.album_genres((int) request.data);
                        sendPacket(Serializer.serialize(new_string));

                        System.out.println("success");
                        break;
                    }
                    case "album_critics": {
                        // Throws Custom Exception
                        ArrayList<Critic> critics = this.database.album_critics((int) request.data);
                        sendPacket(Serializer.serialize(critics));

                        System.out.println("success");
                        break;
                    }
                    case "album_critic_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.album_critic_create((int) args.get(0), (Critic) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "album_critic_find": {
                        // Throws Custom Exception
                        Critic critic = this.database.album_critic_find((int) request.data);
                        sendPacket(Serializer.serialize(critic));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_all": {
                        // Throws Custom Exception
                        ArrayList<Song> album_songs = this.database.album_song_all((int) request.data);
                        sendPacket(Serializer.serialize(album_songs));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_create": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.album_song_create((int) args.get(0), (Song) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "user_uploads": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        ArrayList<StoredSong> uploads = this.database.user_uploads((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(uploads));

                        System.out.println("success");
                        break;
                    }
                    case "song_find": {
                        // Throws Custom Exception
                        Song song = this.database.song_find((Integer) request.data);
                        sendPacket(Serializer.serialize(song));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_update": {
                        // Throws Custom Exception
                        this.database.album_song_update((Song) request.data);
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_delete": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;
                        this.database.album_song_delete((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "request_song_upload": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        int port = this.database.requestSongUpload((int) args.get(0), (int) args.get(1), (String) args.get(2));
                        sendPacket(Serializer.serialize(new IpPort(InetAddress.getLocalHost(), port)));

                        System.out.println("success");
                        break;
                    }
                    case "song_downloads": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        ArrayList<StoredSong> songs = this.database.song_downloads((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(songs));

                        System.out.println("success");
                        break;
                    }
                    case "request_song_download": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        int port = this.database.requestSongDownload((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(new IpPort(InetAddress.getLocalHost(), port)));

                        System.out.println("success");
                        break;
                    }
                    case "song_share": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.song_share((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "genre_all": {
                        // Throws Custom Exception
                        ArrayList<Genre> genres = this.database.genre_all();
                        sendPacket(Serializer.serialize(genres));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_genre_add": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_genre_add((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "genre_create": {
                        // Throws Custom Exception
                        this.database.genre_create((Genre) request.data);
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "genre_find": {
                        // Throws Custom Exception
                        Genre genre = this.database.genre_find((Integer) request.data);
                        sendPacket(Serializer.serialize(genre));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_genre_remove": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_genre_remove((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(true));
                        break;
                    }
                    case "album_song_artist_add": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_artist_add((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(true));

                        System.out.println("success");
                        break;
                    }
                    case "album_song_artist_remove": {
                        // Throws Custom Exception
                        ArrayList<Object> args = (ArrayList<Object>) request.data;

                        this.database.album_song_artist_remove((int) args.get(0), (int) args.get(1));
                        sendPacket(Serializer.serialize(true));

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
                    this.sendPacket(response);
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

    public static String serialize(Object obj) throws IOException {
        ObjectOutputStream oOS;
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        oOS = new ObjectOutputStream(bAOS);
        oOS.writeObject(obj);
        oOS.flush();
        String stringData = Base64.encode(bAOS.toByteArray());
        return stringData;
    }

    public void sendPacket(String stringData){
        MulticastSocket sendingSocket = null;
        try {
            sendingSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            byte[] buffer = stringData.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_TARGET_PORT);
            sendingSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendingSocket.close();
        }
    }

}