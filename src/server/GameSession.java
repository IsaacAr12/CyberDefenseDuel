package server;

import com.google.gson.Gson;
import network.Message;
import network.MessageType;

public class GameSession {

    private final String sessionId;
    private final ClientHandler playerA;
    private final ClientHandler playerB;
    private final Gson gson;

    private boolean playerAFinished;
    private boolean playerBFinished;

    public GameSession(String sessionId, ClientHandler playerA, ClientHandler playerB) {
        this.sessionId = sessionId;
        this.playerA = playerA;
        this.playerB = playerB;
        this.gson = new Gson();
        this.playerAFinished = false;
        this.playerBFinished = false;
    }

    public void start() {
        System.out.println("Iniciando sesión: " + sessionId);

        sendMatchFound(playerA, playerB.getAuthenticatedUsername());
        sendMatchFound(playerB, playerA.getAuthenticatedUsername());

        sendConfig(playerA);
        sendConfig(playerB);
    }

    private void sendMatchFound(ClientHandler player, String opponentUsername) {
        String json = """
        {
          "type":"MATCH_FOUND",
          "opponent":"%s",
          "sessionId":"%s"
        }
        """.formatted(opponentUsername, sessionId).trim();

        player.sendRawMessage(json);
    }

    private void sendConfig(ClientHandler player) {
        String json = """
        {
          "type":"CONFIG",
          "initialHp":100,
          "baseSpawnRate":1.0,
          "baseAttackSpeed":2.0,
          "scorePerKill":10,
          "difficultyStepScore":100,
          "spawnMultiplierPerLevel":1.15,
          "speedAddPerLevel":0.3,
          "damageByType":{"DDOS":5,"MALWARE":8,"CRED":10}
        }
        """.trim();

        player.sendRawMessage(json);
    }

    public synchronized void forwardGameState(ClientHandler sender, String jsonState) {
        if (sender == playerA) {
            playerB.sendRawMessage(jsonState);
        } else if (sender == playerB) {
            playerA.sendRawMessage(jsonState);
        }
    }

    public synchronized void handleGameOver(ClientHandler sender, String jsonGameOver) {
        if (sender == playerA) {
            playerAFinished = true;
            playerB.sendRawMessage(jsonGameOver);
        } else if (sender == playerB) {
            playerBFinished = true;
            playerA.sendRawMessage(jsonGameOver);
        }

        if (playerAFinished && playerBFinished) {
            System.out.println("Sesión finalizada: " + sessionId);
        }
    }

    public String getSessionId() {
        return sessionId;
    }
}