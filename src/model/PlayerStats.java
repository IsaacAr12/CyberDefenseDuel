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

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getNetworkXp() {
        return networkXp;
    }

    public void setNetworkXp(int networkXp) {
        this.networkXp = networkXp;
    }

    public int getMalwareXp() {
        return malwareXp;
    }

    public void setMalwareXp(int malwareXp) {
        this.malwareXp = malwareXp;
    }

    public int getCryptoXp() {
        return cryptoXp;
    }

    public void setCryptoXp(int cryptoXp) {
        this.cryptoXp = cryptoXp;
    }

    public void addTotalScore(int amount) {
        this.totalScore += amount;
    }

    public void addGamePlayed() {
        this.gamesPlayed++;
    }

    public void addNetworkXp(int amount) {
        this.networkXp += amount;
    }

    public void addMalwareXp(int amount) {
        this.malwareXp += amount;
    }

    public void addCryptoXp(int amount) {
        this.cryptoXp += amount;
    }
}