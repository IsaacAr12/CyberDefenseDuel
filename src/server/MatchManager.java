package server;

import network.Message;
import network.MessageType;

public class MatchManager {

    private final DatabaseManager databaseManager;
    private ClientHandler waitingPlayer;

    public MatchManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public synchronized void requestMatch(ClientHandler player) {
        if (waitingPlayer == null) {
            waitingPlayer = player;
            player.sendMessage(new Message(MessageType.ERROR, "SERVER", "WAITING"));
            return;
        }

        if (waitingPlayer == player) {
            return;
        }

        ClientHandler opponent = waitingPlayer;
        waitingPlayer = null;

        GameSession session = new GameSession(opponent, player, databaseManager);

        opponent.setSession(session);
        player.setSession(session);

        session.start();
    }
}