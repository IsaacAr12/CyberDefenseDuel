package game;

import structures.CustomQueue;
import patterns.AttackFactoryImpl;

//Gestiona ataques activos
public class AttackManager {
    //Cola personalizada, almacena ataques activos
    private CustomQueue<Attack> activeAttacks;
    //Crea ataques según tipo
    private AttackFactoryImpl factory;
    //Temporizador para controlar generación de ataques
    private double spawnTimer;
    //Config
    private Config config;

    //Constructor 
    public AttackManager(Config cfg) {
        this.activeAttacks = new CustomQueue<>(32);
        this.factory = new AttackFactoryImpl(cfg);
        this.spawnTimer = 0;
        this.config = cfg;
    }
    //Actualiza el estado del gestor
    public void update(double delta, int level) {
        spawnTimer += delta;
        double spawnRate = config.getBaseSpawnRate() * Math.pow(config.getSpawnMultiplierPerLevel(), level);

        if (spawnTimer >= spawnRate) {
            spawn("DDOS"); // esto es un ejemplo, podria variar segun tipo
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