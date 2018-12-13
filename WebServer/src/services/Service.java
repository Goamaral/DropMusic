package services;

import core.CustomException;
import core.Request;
import core.Response;
import core.Serializer;

import java.io.IOException;
import java.net.*;

public class Service {
    static int MULTICAST_SOURCE_PORT = 20000;
    static int MULTICAST_TARGET_PORT = 30000;
    static InetAddress MULTICAST_ADDRESS;
    static MulticastSocket sender_socket;
    static MulticastSocket receiver_socket;
    static Integer request_id = 1;

    public static Object request(String type, Object resource) throws IOException {
        MULTICAST_ADDRESS = InetAddress.getByName("localhost");
        sender_socket = new MulticastSocket();
        receiver_socket = new MulticastSocket(MULTICAST_SOURCE_PORT);  // create socket and bind it
        receiver_socket.joinGroup(Service.MULTICAST_ADDRESS);

        int id;

        synchronized (Service.request_id) {
            id = request_id;
            Service.request_id += 1;
        }

        try {
            String request_string = Serializer.serialize(
                    new Request(id, type, resource)
            );

            byte[] request_buffer = request_string.getBytes();
            DatagramPacket sender_packet = new DatagramPacket(
                    request_buffer,
                    request_buffer.length,
                    Service.MULTICAST_ADDRESS,
                    Service.MULTICAST_TARGET_PORT
            );

            byte[] response_buffer = new byte[50000];
            DatagramPacket response_packet = new DatagramPacket(response_buffer, response_buffer.length);

            while (true) {
                try {
                    receiver_socket.setSoTimeout(5000);
                    sender_socket.send(sender_packet);
                    receiver_socket.receive(response_packet);

                    String response_string = new String(response_packet.getData(), 0, response_packet.getLength());
                    Response response = (Response) Serializer.deserialize(response_string);

                    if (response.id == id) {
                        sender_socket.close();
                        receiver_socket.close();

                        return response.data;
                    } else {
                        throw new SocketTimeoutException();
                    }
                } catch (SocketTimeoutException ste) {
                    System.out.println("Packet was considered lost. Retrying...");
                    response_packet = new DatagramPacket(new byte[request_buffer.length], request_buffer.length);
                }
            }
        } catch (CustomException ce) {
            return ce;
        }
    }

    public static void catchException(Object resource) throws CustomException {
        if (resource instanceof CustomException) {
            throw (CustomException) resource;
        }
    }
}
