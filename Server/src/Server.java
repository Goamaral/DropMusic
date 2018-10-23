import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Server implements ServerInterface {
    int currentPort;
    int alternativePort;
    ServerInterface primaryServerInterface;

    int maxAttemps = 5;
    int connectAttemps = 0;

    Database database = new Database();

    public Server() {}

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Current server(<IP>:<PORT>): ");
            server.currentPort = Integer.parseInt(scanner.nextLine());

            System.out.print("Alternative port(<IP>:<PORT>): ");
            server.alternativePort = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid port");
            System.exit(0);
        }

        // Check if alternative is online
        try {
            System.out.println("Checking the alternative server at " + server.alternativePort);
            server.connect();
            // Online -> Become secundary and ping primary
            try {
                System.out.println("Secundary server online at " + server.currentPort);
                System.out.println("Start pinging");
                while(server.primaryServerInterface.ping()) {
                    System.out.println("PING");
                    Thread.sleep(1000);
                }
            } catch (RemoteException re) {
                // Primary server is offline -> Become primary
                System.out.println("Primary server went offline");
                System.out.println("Becoming primary server");
            }

        } catch(RemoteException | NotBoundException e) {
            // Offline -> Become primary
        }

        try {
            server.setup();
        } catch (RemoteException re) {
            System.out.println("Failed to become primary or secundary");
            System.out.println("Verify the current usage of the input ports");
            System.exit(0);
        }

        System.out.println("Primary server online at " + server.currentPort);



        while (true);
    }

    void setup() throws InterruptedException, RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(this.currentPort);

            // Controllers
            ServerInterface serverInterface = (ServerInterface) UnicastRemoteObject.exportObject(this, this.currentPort);
            registry.rebind("ServerInterface", serverInterface);

            UserController userController = new UserController(this);
            UserInterface userInterface = (UserInterface) UnicastRemoteObject.exportObject(userController, this.currentPort);
            registry.rebind("UserInterface", userInterface);

            AlbumController albumController = new AlbumController(this);
            AlbumInterface albumInterface = (AlbumInterface) UnicastRemoteObject.exportObject(albumController, this.currentPort);
            registry.rebind("AlbumInterface", albumInterface);

            ArtistController artistController = new ArtistController(this);
            ArtistInterface artistInterface = (ArtistInterface) UnicastRemoteObject.exportObject(artistController, this.currentPort);
            registry.rebind("ArtistInterface", artistInterface);

        } catch(RemoteException re) {
            this.connectAttemps += 1;

            if (this.connectAttemps == this.maxAttemps) {
                this.connectAttemps = 0;
                throw re;
            }

            Thread.sleep(1000);
            this.setup();
        }
    }

    void connect() throws RemoteException, InterruptedException, NotBoundException {
        try {
            Registry registry = LocateRegistry.getRegistry(this.alternativePort);
            this.primaryServerInterface = (ServerInterface) registry.lookup("ServerInterface");
        } catch (RemoteException | NotBoundException e) {
            this.connectAttemps += 1;
            if (this.connectAttemps == this.maxAttemps) {
                this.connectAttemps = 0;
                throw e;
            }

            Thread.sleep(1000);
            this.connect();
        }
    }

    public boolean ping() { return true; }
}

class RmiMulticastSender extends Thread {
    private String type;
    private Object resource;
    private Boolean running;
    private Object lock;
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 3000;

    public RmiMulticastSender(String type, Object resource) {
        this.type = type;
        this.resource = resource;
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();
            while (true) {
                lock.wait();
                if(!running) break;
                //buffer sera depois de fazer o formatMessage para json
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                byte[] buffer = new byte[256];;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public void formatMessage() {
        JSONObject obj = new JSONObject();
        obj.put("type", this.type);
        obj.put("data", );
        return;
    }

}


interface ServerInterface extends Remote {
    boolean ping() throws RemoteException;
}