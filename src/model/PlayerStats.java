package model;

public class PlayerStats {
    private int totalScore;
    private int gamesPlayed;
    private int networkXp;
    private int malwareXp;
    private int cryptoXp;

    public PlayerStats() {
        this.totalScore = 0;
        this.gamesPlayed = 0;
        this.networkXp = 0;
        this.malwareXp = 0;
        this.cryptoXp = 0;
    }

    public int getTotalScore() { return totalScore; }
    public void addTotalScore(int delta) { this.totalScore += delta; }

    public int getGamesPlayed() { return gamesPlayed; }
    public void incrementGamesPlayed() { this.gamesPlayed++; }

    public int getNetworkXp() { return networkXp; }
    public void addNetworkXp(int xp) { this.networkXp += xp; }

    public int getMalwareXp() { return malwareXp; }
    public void addMalwareXp(int xp) { this.malwareXp += xp; }

    public int getCryptoXp() { return cryptoXp; }
    public void addCryptoXp(int xp) { this.cryptoXp += xp; }
}