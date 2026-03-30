package model;

public class GameState {
    private String playerId;
    private int hp;
    private int score;
    private int level;
    private long timestamp;

    public GameState(String playerId, int hp, int score, int level, long timestamp) {
        this.playerId = playerId;
        this.hp = hp;
        this.score = score;
        this.level = level;
        this.timestamp = timestamp;
    }

    // getters
    public String getPlayerId() { return playerId; }
    public int getHp() { return hp; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public long getTimestamp() { return timestamp; }
}