import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.ArrayList;
import java.net.*;
import java.util.Scanner;
import core.*;

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
        int request_id = -1;

        try {
            // Receive
            receiver_socket = new MulticastSocket(multicastServer.MULTICAST_SOURCE_PORT);  // create socket and bind it
            InetAddress group = multicastServer.MULTICAST_ADDRESS;
            receiver_socket.joinGroup(group);

            while (true) {
                byte[] request_buffer = new byte[50000];
                DatagramPacket request_packet = new DatagramPacket(request_buffer, request_buffer.length);
                System.out.println("Waiting for packets...");
                receiver_socket.receive(request_packet);
                System.out.println("Received packet");

                String request_string = new String(request_packet.getData(), 0, request_packet.getLength());
                byte request_bytes[] = Base64.decode(request_string);
                ByteArrayInputStream bAIS = new ByteArrayInputStream(request_bytes);
                ObjectInputStream oIS = new ObjectInputStream(bAIS);
                Request request = (Request) oIS.readObject();
                request_id = request.id;

                System.out.print("Action " + request.type + " ");

                try {
                    switch (request.type) {
                        case "user_setUid": {
                            // Throws Custom Exception

                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            this.database.user_setUid((int)args.get(0), (String) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "user_findByUid": {
                            // Throws Custom Exception

                            User fetched_user = this.database.user_findByUid((String) request.data);
                            sendPacket(fetched_user, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "user_findByUsername": {
                            // Throws Custom Exception

                            User fetched_user = this.database.user_findByUsername((String) request.data);
                            sendPacket(fetched_user, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "user_create": {
                            // Throws Custom Exception
                            Boolean result = this.database.user_create((User) request.data);
                            sendPacket(result, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "normal_users": {
                            // Does not throw Custom Exception
                            ArrayList<User> result = this.database.normal_users();
                            sendPacket(result, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "user_promote": {
                            // Throws Custom Exception
                            this.database.user_promote((Integer) request.data);
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "user_find":
                            // Throws Custom Exception
                            User user = this.database.user_find((int) request.data);
                            sendPacket(user, request.id);

                            System.out.println("success");
                            break;
                        case "user_all":
                            // Does not throw Custom Exception
                            ArrayList<User> users = this.database.user_all();
                            sendPacket(users, request.id);

                            System.out.println("success");
                            break;
                        case "artist_all": {
                            // Does not throw Custom Exception
                            ArrayList<Artist> artists = this.database.artist_all();
                            sendPacket(artists, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "artist_create": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            this.database.artist_create((int) args.get(0), (Artist) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "artist_find": {
                            // Throws Custom Exception
                            Artist artist = this.database.artist_find((Integer) request.data);
                            sendPacket(artist, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "artist_update": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            ArrayList<Integer> editor_ids = this.database.artist_update((int) args.get(0), (Artist) args.get(1));
                            sendPacket(editor_ids, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "artist_delete": {
                            // Does not throw Custom Exception
                            this.database.artist_delete((Integer) request.data);
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "artist_songs": {
                            // Throws Custom Exception
                            ArrayList<Song> songs = this.database.artist_songs((Integer) request.data);
                            sendPacket(songs, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_all": {
                            // Throws Custom Exception
                            ArrayList<Album> albums = this.database.album_all();
                            sendPacket(albums, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_create": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            Album new_album = this.database.album_create((int) args.get(0), (Album) args.get(1));
                            sendPacket(new_album, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_find": {
                            // Throws Custom Exception
                            Album album = this.database.album_find((int) request.data);
                            sendPacket(album, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_update": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            ArrayList<Integer> editor_ids = this.database.album_update((int) args.get(0), (Album) args.get(1));
                            sendPacket(editor_ids, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_delete": {
                            // Does not throw Custom Exception
                            this.database.album_delete((Integer) request.data);
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_artists": {
                            // Throws Custom Exception
                            String new_string = this.database.album_artists((int) request.data);
                            sendPacket(new_string, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_genres": {
                            // Throws Custom Exception
                            String new_string = this.database.album_genres((int) request.data);
                            sendPacket(new_string, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_critics": {
                            // Throws Custom Exception
                            ArrayList<Critic> critics = this.database.album_critics((int) request.data);
                            sendPacket(critics, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_critic_create": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            this.database.album_critic_create((int) args.get(0), (Critic) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_critic_find": {
                            // Throws Custom Exception
                            Critic critic = this.database.album_critic_find((int) request.data);
                            sendPacket(critic, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_all": {
                            // Throws Custom Exception
                            ArrayList<Song> album_songs = this.database.album_song_all((int) request.data);
                            sendPacket(album_songs, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_create": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            this.database.album_song_create((int) args.get(0), (Song) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "user_uploads": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            ArrayList<StoredSong> uploads = this.database.user_uploads((int) args.get(0), (int) args.get(1));
                            sendPacket(uploads, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "song_find": {
                            // Throws Custom Exception
                            Song song = this.database.song_find((Integer) request.data);
                            sendPacket(song, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_update": {
                            // Throws Custom Exception
                            this.database.album_song_update((Song) request.data);
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_delete": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;
                            this.database.album_song_delete((int) args.get(0), (int) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "request_song_upload": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            int port = this.database.requestSongUpload((int) args.get(0), (int) args.get(1), (String) args.get(2));
                            sendPacket(new IpPort(InetAddress.getLocalHost(), port), request.id);

                            System.out.println("success");
                            break;
                        }
                        case "song_downloads": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            ArrayList<StoredSong> songs = this.database.song_downloads((int) args.get(0), (int) args.get(1));
                            sendPacket(songs, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "request_song_download": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            int port = this.database.requestSongDownload((int) args.get(0), (int) args.get(1));
                            sendPacket(new IpPort(InetAddress.getLocalHost(), port), request.id);

                            System.out.println("success");
                            break;
                        }
                        case "song_share": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            this.database.song_share((int) args.get(0), (int) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "genre_all": {
                            // Throws Custom Exception
                            ArrayList<Genre> genres = this.database.genre_all();
                            sendPacket(genres, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_genre_add": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            this.database.album_song_genre_add((int) args.get(0), (int) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "genre_create": {
                            // Throws Custom Exception
                            this.database.genre_create((Genre) request.data);
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "genre_find": {
                            // Throws Custom Exception
                            Genre genre = this.database.genre_find((Integer) request.data);
                            sendPacket(genre, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_genre_remove": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            this.database.album_song_genre_remove((int) args.get(0), (int) args.get(1));
                            sendPacket(true, request.id);
                            break;
                        }
                        case "album_song_artist_add": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            this.database.album_song_artist_add((int) args.get(0), (int) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                        case "album_song_artist_remove": {
                            // Throws Custom Exception
                            ArrayList<Object> args = (ArrayList<Object>) request.data;

                            this.database.album_song_artist_remove((int) args.get(0), (int) args.get(1));
                            sendPacket(true, request.id);

                            System.out.println("success");
                            break;
                        }
                    }
                } catch (CustomException e){
                    sendPacket(e, request_id);
                    System.out.println("success");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (receiver_socket != null) {
            receiver_socket.close();
        }
    }

    public void sendPacket(Object resource, int request_id){
        try {
            MulticastSocket sendingSocket = new MulticastSocket();
            InetAddress group = multicastServer.MULTICAST_ADDRESS;
            String stringData = Serializer.serialize(new Response(request_id, resource));
            byte[] buffer = stringData.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastServer.MULTICAST_TARGET_PORT);
            sendingSocket.send(packet);
            sendingSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

}