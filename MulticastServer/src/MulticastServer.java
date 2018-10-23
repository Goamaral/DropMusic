import java.net.*;
import java.io.IOException;

public class MulticastServer {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int SERVERPORT = 3000;
    private int RMIPORT = 4040;

    public static void main(String[] args) throws IOException {
        RecieverSocketHandler recieverSocket = new RecieverSocketHandler();
        recieverSocket.start();
        SendingSocketHandler sendingSocket = new SendingSocketHandler();
        recieverSocket.start();
    }
}

class RecieverSocketHandler extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int SERVERPORT = 3000;

    public void run() {
        MulticastSocket recieverSocket = null;
        try {
            recieverSocket = new MulticastSocket(SERVERPORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS)
            recieverSocket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                recieverSocket.receive(packet);
                //handle packet when it recieves one
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            recieverSocket.close();
        }
    }
}


class SendingSocketHandler extends Thread {
    /*private String MULTICAST_ADDRESS = "224.0.224.0";
    private int SERVERPORT = 4040;

    public void run() {
        MulticastSocket sendingSocket = null;
        try {
            sendingSocket = new MulticastSocket();
            while (true) {
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendingSocket.close();
        }
    }*/
}


