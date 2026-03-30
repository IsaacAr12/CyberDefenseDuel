package game;
import structures.CustomQueue;
import java.util.Random;

public class AttackManager {
    private final CustomQueue<Attack> activeAttacks;
    private final Config config;
    private double spawnTimer;
    private final Random rng;

    public AttackManager(Config cfg) {
        this.config = cfg;
        this.activeAttacks = new CustomQueue<>(256);
        this.spawnTimer = 0.0;
        this.rng = new Random();
    }

    
     //Actualiza spawn y movimiento de ataques.
     //delta segundos desde último frame
     //level nivel actual (afecta velocidad y spawn)
     
    public void update(double delta, int level) {
        spawnTimer += delta;
        double spawnRate = config.getBaseSpawnRate() * Math.pow(config.getSpawnMultiplierPerLevel(), Math.max(0, level - 1));
        if (spawnTimer >= spawnRate) {
            spawnRandom(level);
            spawnTimer = 0.0;
        }

        int size = activeAttacks.size();
        for (int i = 0; i < size; i++) {
            Attack a = activeAttacks.dequeue();
            if (a == null) break;
            // speedMultiplier por nivel
            double speedMultiplier = (level - 1) * config.getSpeedAddPerLevel();
            a.update(delta, speedMultiplier);
            // si sale de pantalla (ejemplo y > 2000) no reencolar
            if (a.getY() <= 2000) {
                activeAttacks.enqueue(a);
            }
        }
    }

    private void spawnRandom(int level) {
        String[] types = {"DDOS", "MALWARE", "CRED"};
        String type = types[rng.nextInt(types.length)];
        spawn(type, level);
    }

    public void spawn(String type, int level) {
        int baseDamage = config.getDamageByType().getOrDefault(type, 5);
        int damage = baseDamage + (level - 1) * 2;
        double baseSpeed = config.getBaseAttackSpeed() + (level - 1) * config.getSpeedAddPerLevel();
        Attack a = new Attack(type, damage, baseSpeed, randomX(), 0.0);
        activeAttacks.enqueue(a);
    }

    public void spawn(String type) { spawn(type, 1); }

    private double randomX() {
        return rng.nextDouble() * 800; // ejemplo: con ancho de pantalla 800
    }

    public Attack peekAttack() { return activeAttacks.peek(); }
    public Attack dequeueAttack() { return activeAttacks.dequeue(); }
    public int activeCount() { return activeAttacks.size(); }
}