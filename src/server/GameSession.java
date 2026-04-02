package server;

import network.Message;
import network.MessageType;

import java.util.Random;

public class GameSession {

    private final ClientHandler playerA;
    private final ClientHandler playerB;

    private String mapA;
    private String mapB;

    private boolean gameOverA;
    private boolean gameOverB;

    public GameSession(ClientHandler playerA, ClientHandler playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.gameOverA = false;
        this.gameOverB = false;
    }

    public void start() {
        sendToBoth(new Message(MessageType.MATCH_FOUND, "SERVER", ""));
    }

    public synchronized void receiveMapChoice(ClientHandler sender, String mapName) {
        if (sender == playerA) {
            mapA = mapName;
        } else if (sender == playerB) {
            mapB = mapName;
        }

        if (mapA != null && mapB != null) {
            String finalMap = new Random().nextBoolean() ? mapA : mapB;
            sendToBoth(new Message(MessageType.MAP_SELECTED, "SERVER", finalMap));
        }
    }

    public void relayGameState(ClientHandler sender, String payload) {
        ClientHandler target = (sender == playerA) ? playerB : playerA;
        target.sendMessage(new Message(MessageType.GAME_STATE, sender.getUsername(), payload));
    }

    public synchronized void handleGameOver(ClientHandler sender, String payload) {
        if (sender == playerA) {
            gameOverA = true;
        } else if (sender == playerB) {
            gameOverB = true;
        }

        ClientHandler target = (sender == playerA) ? playerB : playerA;
        target.sendMessage(new Message(MessageType.GAME_OVER, sender.getUsername(), payload));

        if (gameOverA && gameOverB) {
            sendToBoth(new Message(MessageType.ERROR, "SERVER", "SESSION_FINISHED"));
        }
    }

    private void sendToBoth(Message msg) {
        playerA.sendMessage(msg);
        playerB.sendMessage(msg);
    }
}