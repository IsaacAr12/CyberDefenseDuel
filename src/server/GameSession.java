package server;

import com.google.gson.Gson;
import network.Message;
import network.MessageType;

import java.util.Random;

public class GameSession {

    private final ClientHandler playerA;
    private final ClientHandler playerB;
    private final DatabaseManager databaseManager;

    private String mapA;
    private String mapB;

    private boolean gameOverA;
    private boolean gameOverB;
    private boolean sessionClosed;

    private EndStats statsA;
    private EndStats statsB;

    private final Gson gson = new Gson();

    public GameSession(ClientHandler playerA, ClientHandler playerB, DatabaseManager databaseManager) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.databaseManager = databaseManager;

        this.gameOverA = false;
        this.gameOverB = false;
        this.sessionClosed = false;

        this.statsA = new EndStats();
        this.statsB = new EndStats();
    }

    public void start() {
        sendToBoth(new Message(MessageType.MATCH_FOUND, "SERVER", ""));
    }

    public synchronized void receiveMapChoice(ClientHandler sender, String mapName) {
        if (sessionClosed) {
            return;
        }

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

    public synchronized void relayGameState(ClientHandler sender, String payload) {
        if (sessionClosed) {
            return;
        }

        ClientHandler target = (sender == playerA) ? playerB : playerA;
        target.sendMessage(new Message(MessageType.GAME_STATE, sender.getUsername(), payload));
    }

    public synchronized void handleGameOver(ClientHandler sender, String payload) {
        if (sessionClosed) {
            return;
        }

        EndStats endStats;
        try {
            endStats = gson.fromJson(payload, EndStats.class);
            if (endStats == null) {
                endStats = new EndStats();
            }
        } catch (Exception e) {
            endStats = new EndStats();
        }

        if (sender == playerA) {
            gameOverA = true;
            statsA = endStats;
        } else if (sender == playerB) {
            gameOverB = true;
            statsB = endStats;
        }

        ClientHandler target = (sender == playerA) ? playerB : playerA;
        target.sendMessage(new Message(MessageType.GAME_OVER, sender.getUsername(), payload));

        if (gameOverA && gameOverB) {
            persistSessionResults();
            sessionClosed = true;
            sendToBoth(new Message(MessageType.ERROR, "SERVER", "SESSION_FINISHED"));
        }
    }

    private void persistSessionResults() {
        if (playerA.getUsername() != null) {
            databaseManager.updateEndSessionStats(
                    playerA.getUsername(),
                    statsA.score,
                    statsA.networkXp,
                    statsA.malwareXp,
                    statsA.cryptoXp
            );
        }

        if (playerB.getUsername() != null) {
            databaseManager.updateEndSessionStats(
                    playerB.getUsername(),
                    statsB.score,
                    statsB.networkXp,
                    statsB.malwareXp,
                    statsB.cryptoXp
            );
        }
    }

    public boolean isSessionClosed() {
        return sessionClosed;
    }

    private void sendToBoth(Message msg) {
        playerA.sendMessage(msg);
        playerB.sendMessage(msg);
    }

    private static class EndStats {
        int hp;
        int score;
        int level;
        int networkXp;
        int malwareXp;
        int cryptoXp;

        EndStats() {
            this.hp = 0;
            this.score = 0;
            this.level = 0;
            this.networkXp = 0;
            this.malwareXp = 0;
            this.cryptoXp = 0;
        }
    }
}