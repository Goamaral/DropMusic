import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ArtistController implements ArtistInterface {
    Server server;

    ArtistController(Server server) {
        this.server = server;
    }

    // Album
    public ArrayList<Artist> index() {
        Object obj = null;
        ArrayList<Artist> artists = null;
        String stringRecieved = this.server.dbRequest("artist_all", obj);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            artists = (ArrayList<Artist>) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Action artist index: " + artists.size() + " artists");

        return artists;
    }

    public void create(Artist artist) throws CustomException {
        System.out.print("Action artist create: " + artist.name);

        try {
            artist.validate();
            String stringRecieved = this.server.dbRequest("artist_create", artist);
        } catch (CustomException ce) {
            System.out.println(" failed");
            throw ce;
        }

        System.out.println(" success");
    }

    public Artist read(int id) throws CustomException {
        Artist artist = null;
        System.out.print("Action artist(" + id + ") read: ");

        String stringRecieved = this.server.dbRequest("artist_find", id);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            artist = (Artist) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("success");
        return artist;
    }

    public void update(Artist new_artist) throws CustomException {
        System.out.print("Action artist(" + new_artist.id + ") update: ");

        try {
            new_artist.validate();
            String stringRecieved = this.server.dbRequest("artist_update", new_artist);
        } catch (CustomException ce) {
            System.out.println("failed");
            throw ce;
        }

        System.out.println("success");
    }

    public void delete(int id) {
        System.out.println("Action artist(" + id + ") delete: ");

        String stringRecieved = this.server.dbRequest("artist_delete", id);

        System.out.println("success");
    }

}