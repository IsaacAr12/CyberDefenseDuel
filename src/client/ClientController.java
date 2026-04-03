package client;

import com.google.gson.Gson;
import javafx.application.Platform;
import network.Message;
import network.MessageType;

public class ClientController {

    private final GameClient client;
    private final GUIBridge guiBridge;
    private final Gson gson;

    public ClientController(GUIBridge guiBridge) {
        this.guiBridge = guiBridge;
        this.client = new GameClient();
        this.gson = new Gson();

        this.client.setListener(this::handleMessage);
    }

    public void connect(String host, int port) throws Exception {
        client.connect(host, port);
    }

    public void login(String username, String password) {
        Credentials credentials = new Credentials(username, password);
        client.sendMessage(new Message(
                MessageType.LOGIN,
                username,
                gson.toJson(credentials)
        ));
    }

    public void register(String username, String password) {
        Credentials credentials = new Credentials(username, password);
        client.sendMessage(new Message(
                MessageType.REGISTER,
                username,
                gson.toJson(credentials)
        ));
    }

    public void requestMatch() {
        client.sendMessage(new Message(
                MessageType.MATCH_REQUEST,
                "CLIENT",
                ""
        ));
    }

    public void sendMapChoice(String mapName) {
        client.sendMessage(new Message(
                MessageType.MAP_CHOICE,
                "CLIENT",
                mapName
        ));
    }

    public void sendGameState(int hp, int score, int level) {
        OpponentStatePayload payload = new OpponentStatePayload(hp, score, level, 0, 0, 0);
        client.sendMessage(new Message(
                MessageType.GAME_STATE,
                "CLIENT",
                gson.toJson(payload)
        ));
    }

    public void sendGameOver(int hp, int score, int level, int networkXp, int malwareXp, int cryptoXp) {
        OpponentStatePayload payload = new OpponentStatePayload(hp, score, level, networkXp, malwareXp, cryptoXp);
        client.sendMessage(new Message(
                MessageType.GAME_OVER,
                "CLIENT",
                gson.toJson(payload)
        ));
    }

    private void handleMessage(Message msg) {
        Platform.runLater(() -> {
            switch (msg.getType()) {
                case LOGIN_OK -> guiBridge.onLoginSuccess();
                case LOGIN_FAIL -> guiBridge.onLoginFail();
                case MATCH_FOUND -> guiBridge.onMatchFound();
                case MAP_SELECTED -> guiBridge.onMapSelected(msg.getPayload());
                case GAME_STATE -> guiBridge.onOpponentState(msg.getPayload());
                case GAME_OVER -> guiBridge.onOpponentGameOver(msg.getPayload());
                case ERROR -> {
                    if ("WAITING".equalsIgnoreCase(msg.getPayload())) {
                        guiBridge.showWaitingStatus();
                    } else if ("SESSION_FINISHED".equalsIgnoreCase(msg.getPayload())) {
                        guiBridge.onSessionFinished();
                    }
                }
                default -> {
                }
            }
        });
    }

    public interface GUIBridge {
        void onLoginSuccess();
        void onLoginFail();
        void showWaitingStatus();
        void onMatchFound();
        void onMapSelected(String mapName);
        void onOpponentState(String payload);
        void onOpponentGameOver(String payload);
        void onSessionFinished();
    }

    private static class Credentials {
        String username;
        String password;

        Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private static class OpponentStatePayload {
        int hp;
        int score;
        int level;
        int networkXp;
        int malwareXp;
        int cryptoXp;

        OpponentStatePayload(int hp, int score, int level, int networkXp, int malwareXp, int cryptoXp) {
            this.hp = hp;
            this.score = score;
            this.level = level;
            this.networkXp = networkXp;
            this.malwareXp = malwareXp;
            this.cryptoXp = cryptoXp;
        }
    }
}