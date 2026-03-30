package game;

import structures.CustomQueue;
import patterns.AttackFactoryImpl;

public class AttackManager {
    private CustomQueue<Attack> activeAttacks;
    private AttackFactoryImpl factory;
    private double spawnTimer;
    private Config config;

    public AttackManager(Config cfg) {
        this.activeAttacks = new CustomQueue<>(32);
        this.factory = new AttackFactoryImpl(cfg);
        this.spawnTimer = 0;
        this.config = cfg;
    }

    public void update(double delta, int level) {
        spawnTimer += delta;
        double spawnRate = config.getBaseSpawnRate() * Math.pow(config.getSpawnMultiplierPerLevel(), level);

        if (spawnTimer >= spawnRate) {
            spawn("DDOS"); // ejemplo, podrías variar tipo
            spawnTimer = 0;
        }

        // Mover ataques
        for (int i = 0; i < activeAttacks.size(); i++) {
            Attack a = activeAttacks.dequeue();
            a.update(delta);
            activeAttacks.enqueue(a);
        }
    }

    public void spawn(String type) {
        Attack attack = factory.createAttack(type);
        activeAttacks.enqueue(attack);
    }

    public Attack peekAttack() {
        return activeAttacks.peek();
    }

    public Attack dequeueAttack() {
        return activeAttacks.dequeue();
    }
}