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
    private final Gson gson;

    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket, DatabaseManager databaseManager) {
        this.socket = socket;
        this.databaseManager = databaseManager;
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
                Message response = handleMessage(request);

                sendMessage(response);
            }

        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + socket.getInetAddress());
        } finally {
            close();
        }
    }

    private Message handleMessage(Message request) {
        if (request == null || request.getType() == null) {
            return new Message(MessageType.ERROR, "Mensaje inválido");
        }

        switch (request.getType()) {
            case REGISTER:
                return handleRegister(request);

            case LOGIN:
                return handleLogin(request);

            default:
                return new Message(MessageType.ERROR, "Tipo de mensaje no soportado");
        }
    }

    private Message handleRegister(Message request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return new Message(MessageType.REGISTER_FAIL, "Username y password son obligatorios");
        }

        boolean registered = databaseManager.registerUser(request.getUsername(), request.getPassword());

        if (registered) {
            return new Message(MessageType.REGISTER_OK, "Usuario registrado correctamente");
        } else {
            return new Message(MessageType.REGISTER_FAIL, "El usuario ya existe");
        }
    }

    private Message handleLogin(Message request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return new Message(MessageType.LOGIN_FAIL, "Username y password son obligatorios");
        }

        boolean success = databaseManager.loginUser(request.getUsername(), request.getPassword());

        if (success) {
            return new Message(MessageType.LOGIN_OK, "Login correcto");
        } else {
            return new Message(MessageType.LOGIN_FAIL, "Credenciales inválidas");
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