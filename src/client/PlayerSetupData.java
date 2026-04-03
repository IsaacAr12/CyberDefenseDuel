package client;

import game.Config;

public class PlayerSetupData {

    private String username;
    private String selectedAvatar;
    private String selectedMap;
    private String finalMap;

    private int finalScore;
    private int finalLevel;
    private int finalHp;

    private int opponentFinalScore;
    private int opponentFinalLevel;
    private int opponentFinalHp;

    private int networkXp;
    private int malwareXp;
    private int cryptoXp;

    private int opponentNetworkXp;
    private int opponentMalwareXp;
    private int opponentCryptoXp;

    private Config gameConfig;

    public PlayerSetupData() {
        this.gameConfig = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSelectedAvatar() {
        return selectedAvatar;
    }

    public void setSelectedAvatar(String selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
    }

    public String getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(String selectedMap) {
        this.selectedMap = selectedMap;
    }

    public String getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(String finalMap) {
        this.finalMap = finalMap;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public int getFinalLevel() {
        return finalLevel;
    }

    public void setFinalLevel(int finalLevel) {
        this.finalLevel = finalLevel;
    }

    public int getFinalHp() {
        return finalHp;
    }

    public void setFinalHp(int finalHp) {
        this.finalHp = finalHp;
    }

    public int getOpponentFinalScore() {
        return opponentFinalScore;
    }

    public void setOpponentFinalScore(int opponentFinalScore) {
        this.opponentFinalScore = opponentFinalScore;
    }

    public int getOpponentFinalLevel() {
        return opponentFinalLevel;
    }

    public void setOpponentFinalLevel(int opponentFinalLevel) {
        this.opponentFinalLevel = opponentFinalLevel;
    }

    public int getOpponentFinalHp() {
        return opponentFinalHp;
    }

    public void setOpponentFinalHp(int opponentFinalHp) {
        this.opponentFinalHp = opponentFinalHp;
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

    public int getOpponentNetworkXp() {
        return opponentNetworkXp;
    }

    public void setOpponentNetworkXp(int opponentNetworkXp) {
        this.opponentNetworkXp = opponentNetworkXp;
    }

    public int getOpponentMalwareXp() {
        return opponentMalwareXp;
    }

    public void setOpponentMalwareXp(int opponentMalwareXp) {
        this.opponentMalwareXp = opponentMalwareXp;
    }

    public int getOpponentCryptoXp() {
        return opponentCryptoXp;
    }

    public void setOpponentCryptoXp(int opponentCryptoXp) {
        this.opponentCryptoXp = opponentCryptoXp;
    }

    public Config getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(Config gameConfig) {
        this.gameConfig = gameConfig;
    }
}