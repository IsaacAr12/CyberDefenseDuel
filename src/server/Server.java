package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;
    private final DatabaseManager databaseManager;
    private final MatchManager matchManager;

    public Server(int port) {
        this.port = port;
        this.databaseManager = new DatabaseManager();
        this.matchManager = new MatchManager(databaseManager);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, databaseManager, matchManager);
                handler.start();
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 5000;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Puerto inválido. Se usará el puerto 5000.");
            }
        }

        Server server = new Server(port);
        server.start();
    }
}