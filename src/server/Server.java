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
        this.databaseManager = new DatabaseManager("resources/database.json");
        this.matchManager = new MatchManager();
    }

    public void start() {
        System.out.println("Iniciando servidor...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                ClientHandler clientHandler =
                        new ClientHandler(clientSocket, databaseManager, matchManager);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(5000);
        server.start();
    }
}