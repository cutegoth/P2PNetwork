import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<WebSocket> sockets = new ArrayList<WebSocket>();

    public List<WebSocket> getSockets() {
        return sockets;
    }

    public void initP2PServer(int port) {
        final WebSocketServer socketServer = new WebSocketServer(new InetSocketAddress(port)) {
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                write (webSocket, "Connecting to the server successfully");
                sockets.add(webSocket);
            }

            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                System.out.println("connection failed to peer:" + webSocket.getRemoteSocketAddress());
                sockets.remove(webSocket);
            }

            public void onMessage(WebSocket webSocket, String msg) {
                System.out.println ("Client message received:" + msg);
                write (webSocket, "The server received a message");
                // broatcast ("The server received a message:" + msg);
            }

            public void onError(WebSocket webSocket, Exception e) {
                System.out.println("Connection failed to peer:" + webSocket.getRemoteSocketAddress());
                sockets.remove(webSocket);
            }

            public void onStart() {

            }
        };
        socketServer.start();
        System.out.println("listening websocket p2p port on: " + port);
    }

    public void write(WebSocket ws, String message) {
        System.out.println ("P2P message sent to" + ws.getRemoteSocketAddress().getPort() + ":" + message);
        ws.send(message);
    }

    public void broatcast(String message) {
        if (sockets.size() == 0) {
            return;
        }
        System.out.println ("====== Start broadcast message:");
        for (WebSocket socket : sockets) {
            this.write(socket, message);
        }
        System.out.println ("====== End broadcast message");
    }

}
