import java.net.*;
import java.io.IOException;

public class MulticastServer {

    public static void main(String[] args) {
        RecieverSocketHandler recieverSocket = new RecieverSocketHandler();
        recieverSocket.start();
        SendingSocketHandler sendingSocket = new SendingSocketHandler();
        sendingSocket.start();
    }
}

class RecieverSocketHandler extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int SERVERPORT = 3000;

    public void run() {
        MulticastSocket recieverSocket = null;
        try {
            recieverSocket = new MulticastSocket(SERVERPORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
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
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int RMIPORT = 4040;

    public void run() {
        MulticastSocket sendingSocket = null;
        try {
            sendingSocket = new MulticastSocket();
            while (true) {
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, RMIPORT);
                sendingSocket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendingSocket.close();
        }
    }
}


