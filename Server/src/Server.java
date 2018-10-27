import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;


public class Server implements ServerInterface {
    int port;
    InetAddress alternative_ip;
    ServerInterface primaryServerInterface;
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int MULTICASTPORT = 3000;
    private int RMIPORT = 4040;

    int maxAttemps = 5;
    int connectAttemps = 0;

    ArrayList<Client> clients = new ArrayList<>();
    Object clientLock = new Object();
    ArrayList<Job> jobs = new ArrayList<>();
    Object jobLock = new Object();

    public Server() throws UnknownHostException {}

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        Server server = new Server();

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Port: ");
            server.port = Integer.parseInt(scanner.nextLine());

            System.out.print("Alternative IP: ");
            server.alternative_ip = InetAddress.getByName(scanner.nextLine());

        } catch (NumberFormatException | UnknownHostException nfe) {
            System.out.println("Invalid port or IPs");
            System.exit(0);
        }

        // Check if alternative is online
        try {
            System.out.println("Checking the alternative server at " + server.alternative_ip.getHostAddress() + ":" + server.port);
            server.connect();
            // Online -> Become secundary and ping primary
            try {
                System.out.println("Secundary server online at port " + server.port);
                System.out.println("Start pinging");
                while (server.primaryServerInterface.ping()) {
                    System.out.println("PING");
                    Thread.sleep(1000);
                }
            } catch (RemoteException re) {
                // Primary server is offline -> Become primary
                System.out.println("Primary server went offline");
                System.out.println("Becoming primary server");
            }

        } catch (RemoteException | NotBoundException e) {
            // Offline -> Become primary
        }

        try {
            server.setup();
        } catch (RemoteException re) {
            System.out.println("Failed to become primary or secundary");
            System.out.println("Verify the current usage of the input ports");
            System.exit(0);
        }

        System.out.println("Primary server online at port " + server.port);

        System.out.println("Press any key to exit");
        scanner.nextLine();
    }

    void setup() throws InterruptedException, RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(this.port);

            // Controllers
            ServerInterface serverInterface = (ServerInterface) UnicastRemoteObject.exportObject(this, this.port);
            registry.rebind("ServerInterface", serverInterface);

            UserController userController = new UserController(this);
            UserInterface userInterface = (UserInterface) UnicastRemoteObject.exportObject(userController, this.port);
            registry.rebind("UserInterface", userInterface);

            AlbumController albumController = new AlbumController(this);
            AlbumInterface albumInterface = (AlbumInterface) UnicastRemoteObject.exportObject(albumController, this.port);
            registry.rebind("AlbumInterface", albumInterface);

            ArtistController artistController = new ArtistController(this);
            ArtistInterface artistInterface = (ArtistInterface) UnicastRemoteObject.exportObject(artistController, this.port);
            registry.rebind("ArtistInterface", artistInterface);

        } catch (RemoteException re) {
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
            Registry registry = LocateRegistry.getRegistry(this.port);
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


    public Object dbRequest(String type, Object resource) {
        Request request = new Request(type, resource);

        System.out.println("Prepare");

        try {
            String request_string = Serializer.serialize(request);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

            // Send
            System.out.println(" -> sending");

            MulticastSocket sender_socket = new MulticastSocket();
            // TODO: Join sender_socket to group?
            byte[] request_buffer = request_string.getBytes();
            DatagramPacket sender_packet = new DatagramPacket(request_buffer, request_buffer.length, group, MULTICASTPORT);
            sender_socket.send(sender_packet);
            sender_socket.close();

            System.out.println(" -> sent");

            // Receive
            System.out.println(" -> receiving");

            MulticastSocket receiver_socket = new MulticastSocket(RMIPORT);  // create socket and bind it
            receiver_socket.joinGroup(group);
            byte[] response_buffer = new byte[5000];
            DatagramPacket response_packet = new DatagramPacket(response_buffer, response_buffer.length);
            receiver_socket.receive(response_packet);
            String response_string = new String(response_packet.getData(), 0, response_packet.getLength());

            System.out.println(" -> received");

            return Serializer.serialize(response_string);
        } catch (IOException | CustomException e) {
            System.out.println(" -> failure");
            return new CustomException("internal error");
        }
    }

    public boolean ping() { return true; }

    public void send_notifications(Job job) {
        boolean sent = false;
        ArrayList<Client> clients_to_remove = new ArrayList<>();

        for (Client client : this.clients) {
            if (client.user_id == job.user_id) {
                try {
                    DataOutputStream output = new DataOutputStream(client.socket.getOutputStream());
                    output.writeBytes(job.message);
                    output.flush();
                    output.close();
                    sent = true;
                } catch (IOException e) {
                    clients_to_remove.add(client);
                }
            }
        }

        synchronized (this.clientLock) {
            for (Client client : clients_to_remove) {
                this.clients.remove(client);
            }
        }

        if (!sent) {
            synchronized (jobLock) {
                this.jobs.add(job);
            }
        }
    }

    public void catch_response_exception(Object object) throws CustomException {
        if (object instanceof Exception) {
            System.out.println("failure");
        }

        if (object instanceof CustomException) {
            throw (CustomException) object;
        }
    }
}

interface ServerInterface extends Remote {
    boolean ping() throws RemoteException;
}

class Job {
    int user_id;
    String message;

    Job(int user_id, String message) {
        this.user_id = user_id;
        this.message = message;
    }
}

class Client {
    Socket socket;
    int user_id;

    Client(Socket socket, int user_id) {
        this.socket = socket;
        this.user_id = user_id;
    }
}