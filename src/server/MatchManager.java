package server;

public class MatchManager {

    private ClientHandler waitingPlayer;
    private int sessionCounter;

    public MatchManager() {
        this.waitingPlayer = null;
        this.sessionCounter = 1;
    }

    public synchronized void addPlayer(ClientHandler player) {
        if (player == null || !player.isAuthenticated()) {
            return;
        }

        if (waitingPlayer == null) {
            waitingPlayer = player;
            player.sendRawMessage("{\"type\":\"MATCH_WAITING\",\"message\":\"Esperando oponente\"}");
            System.out.println("Jugador en espera: " + player.getAuthenticatedUsername());
        } else {
            ClientHandler playerA = waitingPlayer;
            ClientHandler playerB = player;
            waitingPlayer = null;

            String sessionId = "session-" + sessionCounter++;
            GameSession session = new GameSession(sessionId, playerA, playerB);

            playerA.setGameSession(session);
            playerB.setGameSession(session);

            session.start();
        }
    }
}