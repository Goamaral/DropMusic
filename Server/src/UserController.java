import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserController implements UserInterface {
    Server server;

    public UserController(Server server) { this.server = server; }

    // Controller
    public User login(User user) throws CustomException, NoSuchAlgorithmException {
        User fetched_user = null;

        user.encrypt_password();

        System.out.println("Action user(" + user.username + ") login: ");

        String stringRecieved = this.server.dbRequest("user_findByUsername", user);

        System.out.print(stringRecieved);

        byte stringByteRecieved [] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            fetched_user = (User)oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.print("fsadf");

        if (!fetched_user.password.equals(user.password)) {
            System.out.println("Invalid password");
            throw new CustomException("Invalid credentials");
        }

        System.out.print("Successful");

        return fetched_user;
    }

    public void register(User user) throws CustomException, NoSuchAlgorithmException {
        String stringRecieved;
        System.out.print("Action user(" + user.username + ") register: ");

        try {
            user.validate();
            stringRecieved = this.server.dbRequest("user_create", user);
        } catch(CustomException ce) {
            System.out.println("failed");
            throw ce;
        }
        System.out.println("Successful");
    }

    public ArrayList<User> normal_users() {
        ArrayList<User> normal_users = null;
        Object obj = null;
        String stringRecieved = this.server.dbRequest("normal_users", obj);

        byte stringByteRecieved[] = Base64.decode(stringRecieved);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(stringByteRecieved);
        ObjectInputStream oIS;
        try {
            oIS = new ObjectInputStream(bAIS);
            normal_users = (ArrayList<User>) oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return normal_users;
    }

    public void promote(int user_id) throws CustomException {
        String stringRecieved = this.server.dbRequest("user_promote", user_id);
    }

}
