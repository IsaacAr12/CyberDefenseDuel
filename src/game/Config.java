package game;

public class Config {

    private int initialHp;
    private double baseSpawnRate;
    private double baseAttackSpeed;
    private int scorePerKill;
    private int difficultyStepScore;
    private double spawnMultiplierPerLevel;
    private double speedAddPerLevel;

    private int ddosDamage;
    private int malwareDamage;
    private int credDamage;

    private double screenWidth;
    private double screenHeight;

    public Config() {
        this.initialHp = 100;
        this.baseSpawnRate = 1.0;
        this.baseAttackSpeed = 160.0;
        this.scorePerKill = 10;
        this.difficultyStepScore = 100;
        this.spawnMultiplierPerLevel = 1.15;
        this.speedAddPerLevel = 20.0;

        this.ddosDamage = 5;
        this.malwareDamage = 8;
        this.credDamage = 10;

        this.screenWidth = 1280;
        this.screenHeight = 720;
    }

    public int getInitialHp() {
        return initialHp;
    }

    public void setInitialHp(int initialHp) {
        this.initialHp = initialHp;
    }

    public double getBaseSpawnRate() {
        return baseSpawnRate;
    }

    public void setBaseSpawnRate(double baseSpawnRate) {
        this.baseSpawnRate = baseSpawnRate;
    }

    public double getBaseAttackSpeed() {
        return baseAttackSpeed;
    }

    public void setBaseAttackSpeed(double baseAttackSpeed) {
        this.baseAttackSpeed = baseAttackSpeed;
    }

    public int getScorePerKill() {
        return scorePerKill;
    }

    public void setScorePerKill(int scorePerKill) {
        this.scorePerKill = scorePerKill;
    }

    public int getDifficultyStepScore() {
        return difficultyStepScore;
    }

    public void setDifficultyStepScore(int difficultyStepScore) {
        this.difficultyStepScore = difficultyStepScore;
    }

    public double getSpawnMultiplierPerLevel() {
        return spawnMultiplierPerLevel;
    }

    public void setSpawnMultiplierPerLevel(double spawnMultiplierPerLevel) {
        this.spawnMultiplierPerLevel = spawnMultiplierPerLevel;
    }

    public double getSpeedAddPerLevel() {
        return speedAddPerLevel;
    }

    public void setSpeedAddPerLevel(double speedAddPerLevel) {
        this.speedAddPerLevel = speedAddPerLevel;
    }

    public int getDdosDamage() {
        return ddosDamage;
    }

    public void setDdosDamage(int ddosDamage) {
        this.ddosDamage = ddosDamage;
    }

    public int getMalwareDamage() {
        return malwareDamage;
    }

    public void setMalwareDamage(int malwareDamage) {
        this.malwareDamage = malwareDamage;
    }

    public int getCredDamage() {
        return credDamage;
    }

    public void setCredDamage(int credDamage) {
        this.credDamage = credDamage;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getDamageForType(String type) {
        if (type == null) {
            return 0;
        }

        switch (type.toUpperCase()) {
            case "DDOS":
                return ddosDamage;
            case "MALWARE":
                return malwareDamage;
            case "CRED":
                return credDamage;
            default:
                return 0;
        }
    }
}