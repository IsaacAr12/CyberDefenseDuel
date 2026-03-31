package server;

import com.google.gson.Gson;
import network.Message;
import network.MessageType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final DatabaseManager databaseManager;
    private final MatchManager matchManager;
    private final Gson gson;

    private String authenticatedUsername;
    private GameSession gameSession;

    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket, DatabaseManager databaseManager, MatchManager matchManager) {
        this.socket = socket;
        this.databaseManager = databaseManager;
        this.matchManager = matchManager;
        this.gson = new Gson();

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creando flujos del cliente: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String jsonMessage;

            while ((jsonMessage = in.readLine()) != null) {
                System.out.println("JSON recibido: " + jsonMessage);

                Message request = gson.fromJson(jsonMessage, Message.class);

                if (request == null || request.getType() == null) {
                    sendMessage(new Message(MessageType.ERROR, "Mensaje inválido"));
                    continue;
                }

                switch (request.getType()) {
                    case REGISTER:
                        sendMessage(handleRegister(request));
                        break;

                    case LOGIN:
                        sendMessage(handleLogin(request));
                        break;

                    case MATCH_REQUEST:
                        handleMatchRequest();
                        break;

                    case GAME_STATE:
                        handleGameState(jsonMessage);
                        break;

                    case GAME_OVER:
                        handleGameOver(jsonMessage);
                        break;

                    default:
                        sendMessage(new Message(MessageType.ERROR, "Tipo de mensaje no soportado"));
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + socket.getInetAddress());
        } finally {
            close();
        }
    }

    private Message handleRegister(Message request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPasswordHash())) {
            return new Message(MessageType.REGISTER_FAIL, "Username y password son obligatorios");
        }

        boolean registered = databaseManager.registerUser(request.getUsername(), request.getPasswordHash());

        if (registered) {
            return new Message(MessageType.REGISTER_OK, "Usuario registrado correctamente");
        } else {
            return new Message(MessageType.REGISTER_FAIL, "El usuario ya existe");
        }
    }

    private Message handleLogin(Message request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPasswordHash())) {
            return new Message(MessageType.LOGIN_FAIL, "Username y password son obligatorios");
        }

        boolean success = databaseManager.loginUser(request.getUsername(), request.getPasswordHash());

        if (success) {
            this.authenticatedUsername = request.getUsername();

            System.out.println("Usuario autenticado: " + authenticatedUsername);

            return new Message(MessageType.LOGIN_OK, "Login correcto");
        } else {
            return new Message(MessageType.LOGIN_FAIL, "Credenciales inválidas");
        }
    }

    private void handleMatchRequest() {
        if (!isAuthenticated()) {
            sendMessage(new Message(MessageType.ERROR, "Debes iniciar sesión primero"));
            return;
        }

        matchManager.addPlayer(this);
    }

    private void handleGameState(String jsonMessage) {
        if (gameSession != null) {
            gameSession.forwardGameState(this, jsonMessage);
        }
    }

    private void handleGameOver(String jsonMessage) {
        if (gameSession != null) {
            gameSession.handleGameOver(this, jsonMessage);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void sendMessage(Message message) {
        try {
            String json = gson.toJson(message);
            out.write(json);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.out.println("Error enviando mensaje al cliente: " + e.getMessage());
        }
    }

    public void sendRawMessage(String json) {
        try {
            out.write(json);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.out.println("Error enviando mensaje JSON al cliente: " + e.getMessage());
        }
    }

    public boolean isAuthenticated() {
        return authenticatedUsername != null;
    }

    public String getAuthenticatedUsername() {
        return authenticatedUsername;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
