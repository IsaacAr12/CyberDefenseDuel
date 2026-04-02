package server;

public class MatchManager {

    private ClientHandler waitingPlayer;

    public synchronized void requestMatch(ClientHandler player) {
        if (waitingPlayer == null) {
            waitingPlayer = player;
            player.sendMessage(new network.Message(network.MessageType.ERROR, "SERVER", "WAITING"));
            return;
        }

        if (waitingPlayer == player) {
            return;
        }

        ClientHandler opponent = waitingPlayer;
        waitingPlayer = null;

        GameSession session = new GameSession(opponent, player);

        opponent.setSession(session);
        player.setSession(session);

        session.start();
    }
}