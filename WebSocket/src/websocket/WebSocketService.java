package websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.*;

@ServerEndpoint(value = "/ws")
public class WebSocketService {
    public static CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
    static ServerSocket socket;
    public static int port = 10000;
    static Socket server;
    static WebSocketService instance;

    static {
        while (true) {
            try {
                WebSocketService.socket = new ServerSocket(WebSocketService.port);
                break;
            } catch (IOException e) {
                WebSocketService.port += 1;
            }
        }

        WebSocketService.instance = new WebSocketService();
    }

    public WebSocketService() {
        new NotificationHandler().start();
    }

    @OnOpen
    public void start(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void end(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void receiveMessage(String message) {

    }

    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    static void sendNotification(String message) {
        for (Session session : WebSocketService.sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                // clean up once the WebSocket connection is closed
                try {
                    session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

class NotificationHandler extends Thread {
    public NotificationHandler() { }

    public void run() {
        try {
            WebSocketService.server = WebSocketService.socket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(WebSocketService.server.getInputStream()));
            String data;

            while ((data = in.readLine()) != null) {
                System.out.println("NOTIFY " + data);
                WebSocketService.sendNotification(data);
            }

            WebSocketService.server.close();
        } catch (IOException e) {
            /* Wait for server to reconnect */
        }
    }
}