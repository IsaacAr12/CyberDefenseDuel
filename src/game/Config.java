package game;

import java.util.HashMap;
import java.util.Map;


 //Configuración central del juego con valores por defecto.
 //Ajusta los valores según el balance que necesites.
 
public class Config {

    // Parámetros de spawn y velocidad
    private double baseSpawnRate;              // segundos entre spawns en nivel 1
    private double spawnMultiplierPerLevel;    // factor multiplicador por nivel
    private double baseAttackSpeed;            // velocidad base de ataques
    private double speedAddPerLevel;           // incremento de velocidad por nivel

    // Jugador y progresión
    private int initialHp;
    private int difficultyStepScore;           // puntos necesarios para subir de nivel
    private int scorePerKill;

    // Daño por tipo de ataque
    private final Map<String, Integer> damageByType;

    // Tamaño de pantalla ejemplo 
    private int screenWidth;
    private int screenHeight;

    public Config() {
        // Valores por defecto razonables para pruebas
        this.baseSpawnRate = 0.8;              // spawn cada 0.8s en nivel 1
        this.spawnMultiplierPerLevel = 0.95;   // cada nivel reduce ligeramente el intervalo
        this.baseAttackSpeed = 100.0;          // unidades/segundo (ajustar luego)
        this.speedAddPerLevel = 10.0;          // incremento por nivel

        this.initialHp = 100;
        this.difficultyStepScore = 50;
        this.scorePerKill = 10;

        this.screenWidth = 800;
        this.screenHeight = 2000;

        this.damageByType = new HashMap<>();
        // Valores por defecto por tipo; puede modificarse
        damageByType.put("DDOS", 5);
        damageByType.put("MALWARE", 8);
        damageByType.put("CRED", 10);
    }

    // Getters usados por el motor y factories
    public double getBaseSpawnRate() { return baseSpawnRate; }
    public double getSpawnMultiplierPerLevel() { return spawnMultiplierPerLevel; }
    public double getBaseAttackSpeed() { return baseAttackSpeed; }
    public double getSpeedAddPerLevel() { return speedAddPerLevel; }

    public int getInitialHp() { return initialHp; }
    public int getDifficultyStepScore() { return difficultyStepScore; }
    public int getScorePerKill() { return scorePerKill; }

    public Map<String, Integer> getDamageByType() { return damageByType; }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }

    // Setters para ajustar dinámicamente en tests o balance
    public void setBaseSpawnRate(double baseSpawnRate) { this.baseSpawnRate = baseSpawnRate; }
    public void setSpawnMultiplierPerLevel(double spawnMultiplierPerLevel) { this.spawnMultiplierPerLevel = spawnMultiplierPerLevel; }
    public void setBaseAttackSpeed(double baseAttackSpeed) { this.baseAttackSpeed = baseAttackSpeed; }
    public void setSpeedAddPerLevel(double speedAddPerLevel) { this.speedAddPerLevel = speedAddPerLevel; }

    public void setInitialHp(int initialHp) { this.initialHp = initialHp; }
    public void setDifficultyStepScore(int difficultyStepScore) { this.difficultyStepScore = difficultyStepScore; }
    public void setScorePerKill(int scorePerKill) { this.scorePerKill = scorePerKill; }

    public void setScreenWidth(int screenWidth) { this.screenWidth = screenWidth; }
    public void setScreenHeight(int screenHeight) { this.screenHeight = screenHeight; }

    // Utilidades para modificar daño por tipo
    public void setDamageForType(String type, int damage) {
        if (type == null) return;
        damageByType.put(type.toUpperCase(), damage);
    }

    public int getDamageForType(String type) {
        if (type == null) return 0;
        return damageByType.getOrDefault(type.toUpperCase(), 5);
    }
}