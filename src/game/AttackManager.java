package game;

import structures.CustomQueue;
import structures.SinglyLinkedList;
import patterns.AttackFactoryImpl;


import java.util.Random;

public class AttackManager {

    private CustomQueue<Attack> activeAttacks;
    private final Config config;
    private final AttackFactoryImpl factory;
    private double spawnTimer;
    private final Random rng;

    // contador de frames para correlacionar logs
    private long frameCounter = 0;

    public AttackManager(Config cfg) {
        this.config = cfg;
        this.factory = new AttackFactoryImpl(cfg); //  AttackFactoryImpl adaptado
        this.activeAttacks = new CustomQueue<>(256);
        this.spawnTimer = 0.0;
        this.rng = new Random();
    }

    // Actualiza posiciones y limpia fuera de pantalla sin alterar la cola original durante el frame
    public void update(double delta, int level) {
        frameCounter++;
        spawnTimer += delta;

        double spawnRate = config.getBaseSpawnRate() * Math.pow(config.getSpawnMultiplierPerLevel(), Math.max(0, level - 1));

        // Si delta es mayor que spawnRate o se acumularon varios spawnRate,
        // genera tantos spawns como correspondan y conserva el sobrante.
        while (spawnTimer >= spawnRate) {
            spawnRandom(level);
            spawnTimer -= spawnRate;
        }

        // Rebuild queue: dequeue todos, actualizar, re-enqueue solo los que siguen en pantalla
        int size = activeAttacks.size();
        CustomQueue<Attack> temp = new CustomQueue<>(Math.max(16, size));
        double screenH = config.getScreenHeight();

        for (int i = 0; i < size; i++) {
            Attack a = activeAttacks.dequeue();
            if (a == null) break;

            double speedMultiplier = (level - 1) * config.getSpeedAddPerLevel();
            a.update(delta, speedMultiplier);

            // Mantener la misma instancia si sigue en pantalla
            if (a.getY() < screenH) { // asume y crece hacia abajo; cambia a > si tu eje es inverso
                temp.enqueue(a);
            } else {
                System.out.printf("[Cull] frame=%d attack=%s y=%.2f removed out-of-screen%n",
                        frameCounter, a.getId(), a.getY());
            }
        }

        this.activeAttacks = temp;
    }

    private void spawnRandom(int level) {
        String[] types = {"DDOS", "MALWARE", "CRED"};
        String type = types[rng.nextInt(types.length)];
        spawn(type, level);
    }

    public void spawn(String type, int level) {
        double startX = randomX();
        Attack a = factory.createAttack(type, level, startX, 0.0);
        activeAttacks.enqueue(a);
        System.out.printf("[Spawn] frame=%d time=%d type=%s id=%s x=%.1f level=%d%n",
                frameCounter, System.currentTimeMillis(), type, a.getId(), a.getX(), level);
    }

    public void spawn(String type) { spawn(type, 1); }

    private double randomX() {
        return rng.nextDouble() * config.getScreenWidth();
    }

    public Attack peekAttack() { return activeAttacks.peek(); }
    public Attack dequeueAttack() { return activeAttacks.dequeue(); }
    public int activeCount() { return activeAttacks.size(); }

    public long getFrameCounter() {
    return frameCounter;
    }

    public double getSpawnTimer() {
        return spawnTimer;
    }

    // Re-enqueuea la misma instancia sin crear una nueva
    public void enqueueExisting(Attack a) {
        if (a != null) activeAttacks.enqueue(a);
    }

    // Elimina un ataque por id (reconstruye cola sin ese ataque)
    public boolean removeAttackById(String id) {
        int size = activeAttacks.size();
        boolean removed = false;
        CustomQueue<Attack> temp = new CustomQueue<>(Math.max(16, size));
        for (int i = 0; i < size; i++) {
            Attack a = activeAttacks.dequeue();
            if (a == null) break;
            if (!removed && a.getId().equals(id)) {
                removed = true;
                continue;
            }
            temp.enqueue(a);
        }
        this.activeAttacks = temp;
        return removed;
    }

    // Exportar snapshots sin alterar la cola
    public SinglyLinkedList<model.GameState.AttackSnapshot> exportSnapshots() {
        SinglyLinkedList<model.GameState.AttackSnapshot> list = new SinglyLinkedList<>();
        int size = activeAttacks.size();
        for (int i = 0; i < size; i++) {
            Attack a = activeAttacks.dequeue();
            if (a == null) break;
            model.GameState.AttackSnapshot s = new model.GameState.AttackSnapshot(
                    a.getId(), a.getType(), a.getX(), a.getY(), a.getBaseSpeed(), a.getDamage());
            list.addLast(s);
            activeAttacks.enqueue(a); // re-enqueue la misma instancia
        }
        return list;
    }
}