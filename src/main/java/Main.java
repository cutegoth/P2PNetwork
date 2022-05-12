public class Main {
    public static void main(String[] args) {
        Server p2pServer = new Server();
        Client p2pClient = new Client();
        int p2pPort = Integer.valueOf(args[0]);
        // Запускаем p2p-сервер
        p2pServer.initP2PServer(p2pPort);
        if (args.length == 2 && args[1] != null) {
            // Подключаемся к серверу p2p как клиент p2p
            p2pClient.connectToPeer(args[1]);
        }

    }
}
