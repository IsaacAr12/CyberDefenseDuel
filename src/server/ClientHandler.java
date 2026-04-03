package server;

import com.google.gson.Gson;
import game.Config;
import network.Message;
import network.MessageType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final DatabaseManager databaseManager;
    private final MatchManager matchManager;

    private String username;
    private GameSession session;

    private final Gson gson = new Gson();

    public ClientHandler(Socket socket, DatabaseManager databaseManager, MatchManager matchManager) throws IOException {
        this.socket = socket;
        this.databaseManager = databaseManager;
        this.matchManager = matchManager;

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            String line;

            while ((line = in.readLine()) != null) {
                Message msg = Message.fromJson(line);
                handleMessage(msg);
            }

        } catch (Exception e) {
            System.out.println("Cliente desconectado: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.getType()) {

            case LOGIN: {
                Credentials creds = gson.fromJson(msg.getPayload(), Credentials.class);
                boolean ok = databaseManager.loginUser(creds.username, creds.password);

                if (ok) {
                    this.username = creds.username;
                    sendMessage(new Message(MessageType.LOGIN_OK, "SERVER", ""));
                    sendConfig();
                } else {
                    sendMessage(new Message(MessageType.LOGIN_FAIL, "SERVER", ""));
                }
                break;
            }

            case REGISTER: {
                Credentials creds = gson.fromJson(msg.getPayload(), Credentials.class);
                boolean ok = databaseManager.registerUser(creds.username, creds.password);

                if (ok) {
                    this.username = creds.username;
                    sendMessage(new Message(MessageType.LOGIN_OK, "SERVER", ""));
                    sendConfig();
                } else {
                    sendMessage(new Message(MessageType.LOGIN_FAIL, "SERVER", ""));
                }
                break;
            }

            case MATCH_REQUEST: {
                matchManager.requestMatch(this);
                break;
            }

            case MAP_CHOICE: {
                if (session != null) {
                    session.receiveMapChoice(this, msg.getPayload());
                }
                break;
            }

            case GAME_STATE: {
                if (session != null) {
                    session.relayGameState(this, msg.getPayload());
                }
                break;
            }

            case GAME_OVER: {
                if (session != null) {
                    session.handleGameOver(this, msg.getPayload());
                }
                break;
            }

            default: {
                sendMessage(new Message(MessageType.ERROR, "SERVER", "Tipo de mensaje no soportado"));
                break;
            }
        }
    }

    private void sendConfig() {
        Config config = buildDefaultConfig();
        sendMessage(new Message(
                MessageType.CONFIG,
                "SERVER",
                gson.toJson(config)
        ));
    }

    private Config buildDefaultConfig() {
        Config config = new Config();
        config.setInitialHp(100);
        config.setBaseSpawnRate(1.0);
        config.setBaseAttackSpeed(160.0);
        config.setScorePerKill(10);
        config.setDifficultyStepScore(100);
        config.setSpawnMultiplierPerLevel(1.15);
        config.setSpeedAddPerLevel(20.0);

        config.setDdosDamage(5);
        config.setMalwareDamage(8);
        config.setCredDamage(10);

        config.setScreenWidth(1280);
        config.setScreenHeight(720);

        return config;
    }

    public void sendMessage(Message msg) {
        try {
            out.write(msg.toJson());
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.out.println("Error enviando mensaje: " + e.getMessage());
        }
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public GameSession getSession() {
        return session;
    }

    public String getUsername() {
        return username;
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
        } catch (Exception ignored) {}

        try {
            if (out != null) out.close();
        } catch (Exception ignored) {}

        try {
            if (socket != null) socket.close();
        } catch (Exception ignored) {}
    }

    private static class Credentials {
        String username;
        String password;
    }
}