import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Client {
    private List<WebSocket> sockets = new ArrayList<WebSocket>();

    public List<WebSocket> getSockets() {
        return sockets;
    }

    public void connectToPeer(String peer) {
        try {
            final WebSocketClient socketClient = new WebSocketClient(new URI(peer)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("Client connection successful");
                    sockets.add(this);
                }

                @Override
                public void onMessage(String msg) {
                    System.out.println("Get the message sent by the server:" + msg);
                }

                @Override
                public void onClose(int i, String msg, boolean b) {
                    System.out.println("connection failed");
                    sockets.remove(this);
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("connection failed");
                    sockets.remove(this);
                }
            };
            socketClient.connect();
        } catch (URISyntaxException e) {
            System.out.println("p2p connect is error:" + e.getMessage());
        }
    }

    public void write(WebSocket ws, String message) {
        System.out.println("P2P message sent to" + ws.getRemoteSocketAddress().getPort() + ":" + message);
        ws.send(message);
    }

    public void broatcast(String message) {
        if (sockets.size() == 0) {
            return;
        }
        System.out.println("\n" +
                "====== Start broadcast message:");
        for (WebSocket socket : sockets) {
            this.write(socket, message);
        }
        System.out.println("\n" +
                "====== End broadcast message");

    }
}
