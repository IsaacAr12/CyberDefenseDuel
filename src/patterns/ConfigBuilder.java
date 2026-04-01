package patterns;

/**
 * Esta cuestion es un builder para crear objetos Config de forma fluida.
 * Creo que nos puede servi para construir la configuración que envía el servidor.
 */
public class ConfigBuilder {
    private int initialHp = 100;
    private double baseSpawnRate = 1.0;
    private double baseAttackSpeed = 1.0;
    private int scorePerKill = 10;
    private int difficultyStepScore = 100;
    private double spawnMultiplierPerLevel = 1.15;
    private double speedAddPerLevel = 0.3;

    public ConfigBuilder setInitialHp(int hp) { this.initialHp = hp; return this; }
    public ConfigBuilder setBaseSpawnRate(double r) { this.baseSpawnRate = r; return this; }
    public ConfigBuilder setBaseAttackSpeed(double s) { this.baseAttackSpeed = s; return this; }
    public ConfigBuilder setScorePerKill(int s) { this.scorePerKill = s; return this; }
    public ConfigBuilder setDifficultyStepScore(int s) { this.difficultyStepScore = s; return this; }
    public ConfigBuilder setSpawnMultiplierPerLevel(double m) { this.spawnMultiplierPerLevel = m; return this; }
    public ConfigBuilder setSpeedAddPerLevel(double a) { this.speedAddPerLevel = a; return this; }

    public GameConfig build() {
        return new GameConfig(initialHp, baseSpawnRate, baseAttackSpeed, scorePerKill,
                difficultyStepScore, spawnMultiplierPerLevel, speedAddPerLevel);
    }

    public static class GameConfig {
        public final int initialHp;
        public final double baseSpawnRate;
        public final double baseAttackSpeed;
        public final int scorePerKill;
        public final int difficultyStepScore;
        public final double spawnMultiplierPerLevel;
        public final double speedAddPerLevel;

        public GameConfig(int initialHp, double baseSpawnRate, double baseAttackSpeed,
                          int scorePerKill, int difficultyStepScore,
                          double spawnMultiplierPerLevel, double speedAddPerLevel) {
            this.initialHp = initialHp;
            this.baseSpawnRate = baseSpawnRate;
            this.baseAttackSpeed = baseAttackSpeed;
            this.scorePerKill = scorePerKill;
            this.difficultyStepScore = difficultyStepScore;
            this.spawnMultiplierPerLevel = spawnMultiplierPerLevel;
            this.speedAddPerLevel = speedAddPerLevel;
        }
    }
}
