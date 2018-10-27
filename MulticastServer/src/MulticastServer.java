import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.net.*;
import java.util.ArrayList;

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
    private int BDPORT = 3000;
    private int RMIPORT = 4040;
    Database database;

    RecieverSocketHandler(MulticastServer multicast){
        this.database = multicast.database;
    }

    public void run() {
        MulticastSocket recieverSocket = null;

        try {
            recieverSocket = new MulticastSocket(BDPORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            recieverSocket.joinGroup(group);

            while (true) {
                byte[] buffer = new byte[5000];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                recieverSocket.receive(packet);

                String stringDataRecieved = new String(packet.getData(), 0, packet.getLength());
                byte bytesDataRecieved [] = Base64.decode(stringDataRecieved);
                ByteArrayInputStream bAIS = new ByteArrayInputStream(bytesDataRecieved);
                ObjectInputStream oIS = new ObjectInputStream(bAIS);
                Request res = (Request)oIS.readObject();
                System.out.println(res.type);

                switch (res.type) {
                    case "user_findByUsername": {
                        User user = (User) res.data;
                        User fetched_user = this.database.user_findByUsername(user.username);
                        System.out.println("O encontro do User " + fetched_user.username + " na base de dados foi bem bem sucedida");
                        String packageToSend = serialize(fetched_user);
                        sendPacket(packageToSend);
                        System.out.println("Enviei o package com " + fetched_user.username);
                        break;
                    }
                    case "user_create": {
                        User user = (User) res.data;
                        User fetched_user = this.database.user_create(user);
                        System.out.println("Criação de User " + fetched_user.username + " na base de dados bem sucedida");
                        String packageToSend = serialize(fetched_user);
                        sendPacket(packageToSend);
                        System.out.println("Enviei o package com " + fetched_user.username);
                        break;
                    }
                    case "normal_users": {
                        ArrayList<User> normal_users;
                        normal_users = this.database.normal_users();
                        String packageToSend = serialize(normal_users);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "user_promote": {
                        Integer id = (Integer) res.data;
                        this.database.user_promote(id);
                        String packageToSend = "Success";
                        sendPacket(packageToSend);
                        break;
                    }
                    case "artist_all": {
                        ArrayList<Artist> artists;
                        artists = this.database.artist_all();
                        String packageToSend = serialize(artists);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "artist_create": {
                        Artist artist = (Artist) res.data;
                        Artist final_artist = this.database.artist_create(artist);
                        String packageToSend = serialize(final_artist);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "artist_find": {
                        Integer id = (Integer) res.data;
                        Artist artist = this.database.artist_find(id);
                        String packageToSend = serialize(artist);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "artist_update": {
                        Artist new_artist = (Artist) res.data;
                        Artist artist = this.database.artist_update(new_artist);
                        String packageToSend = serialize(artist);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "artist_delete": {
                        Integer id = (Integer) res.data;
                        Boolean success = this.database.artist_delete(id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_all": {
                        ArrayList<Album> albums = this.database.album_all();
                        String packageToSend = serialize(albums);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_create": {
                        Album album = (Album) res.data;
                        Album new_album = this.database.album_create(album);
                        String packageToSend = serialize(new_album );
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_find": {
                        Integer id = (Integer) res.data;
                        Album album = this.database.album_find(id);
                        String packageToSend = serialize(album);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_update": {
                        Album new_album = (Album) res.data;
                        Album album= this.database.album_update(new_album);
                        String packageToSend = serialize(album);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_delete": {
                        Integer id = (Integer) res.data;
                        Boolean success = this.database.artist_delete(id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_artists": {
                        Integer id = (Integer) res.data;
                        String new_string = this.database.album_artists(id);
                        String packageToSend = serialize(new_string);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_genres": {
                        Integer id = (Integer) res.data;
                        String new_string = this.database.album_genres(id);
                        String packageToSend = serialize(new_string);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_critics": {
                        Integer id = (Integer) res.data;
                        ArrayList<Critic> critics = this.database.album_critics(id);
                        String packageToSend = serialize(critics);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_critic_create": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer id = (Integer) obj.get(0);
                        Critic critic = (Critic) obj.get(1);
                        Boolean success = this.database.album_critic_create(id, critic);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_critic_find": {
                        Integer id = (Integer) res.data;
                        Critic critic = this.database.album_critic_find(id);
                        String packageToSend = serialize(critic);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_all": {
                        Integer id = (Integer) res.data;
                        ArrayList<Song> songs = this.database.album_song_all(id);
                        String packageToSend = serialize(songs);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_create": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer id = (Integer) obj.get(0);
                        Song song = (Song) obj.get(1);
                        Boolean success = this.database.album_song_create(id, song);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "song_find": {
                        Integer id = (Integer) res.data;
                        Song song= this.database.song_find(id);
                        String packageToSend = serialize(song);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_update": {
                        Song song = (Song) res.data;
                        Boolean success = this.database.album_song_update(song);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_delete": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer album_id = (Integer) obj.get(0);
                        Integer song_id = (Integer) obj.get(1);
                        Boolean success = this.database.album_song_delete(album_id, song_id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "genre_all": {
                        ArrayList<Genre> genres= this.database.genre_all();
                        String packageToSend = serialize(genres);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_genre_add": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer song_id = (Integer) obj.get(0);
                        Integer genre_id = (Integer) obj.get(1);
                        Boolean success = this.database.album_song_genre_add(song_id, genre_id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "genre_create": {
                        Genre genre = (Genre) res.data;
                        Boolean success = this.database.genre_create(genre);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "genre_find": {
                        Integer genre_id = (Integer) res.data;
                        Genre genre = this.database.genre_find(genre_id);
                        String packageToSend = serialize(genre);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_genre_remove": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer song_id = (Integer) obj.get(0);
                        Integer genre_id = (Integer) obj.get(1);
                        Boolean success = this.database.album_song_genre_remove(song_id, genre_id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_artist_add": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer song_id = (Integer) obj.get(0);
                        Integer artist_id = (Integer) obj.get(1);
                        Boolean success = this.database.album_song_artist_add(song_id, artist_id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                    case "album_song_artist_remove": {
                        ArrayList<Object> obj = (ArrayList<Object>) res.data;
                        Integer song_id = (Integer) obj.get(0);
                        Integer genre_id = (Integer) obj.get(1);
                        Boolean success = this.database.album_song_artist_remove(song_id, genre_id);
                        String packageToSend = serialize(success);
                        sendPacket(packageToSend);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            recieverSocket.close();
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
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, RMIPORT);
            sendingSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendingSocket.close();
        }
    }

}