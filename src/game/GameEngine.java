package game;

import model.GameState;

public class GameEngine {
    private final Player player;
    private final AttackManager attackManager;
    private int score;
    private int level;
    private final Config config;

    public GameEngine(Config cfg, String playerId) {
        this.config = cfg;
        this.player = new Player(playerId, cfg.getInitialHp(), "default");
        this.attackManager = new AttackManager(cfg);
        this.score = 0;
        this.level = 1;
    }

    
     //Llama cada frame con delta en segundos.

    public void update(double delta) {
        attackManager.update(delta, level);
        checkCollisions();
        // subir nivel si corresponde
        if (score >= level * config.getDifficultyStepScore()) {
            level++;
        }
    }

    private void checkCollisions() {
        // Umbrales de colisión (ajustables)
        double thresholdX = 20.0;
        double thresholdY = 20.0;

        int iterations = attackManager.activeCount();
        for (int i = 0; i < iterations; i++) {
            Attack a = attackManager.peekAttack();
            if (a == null) break;
            if (a.isInRange(player, thresholdX, thresholdY)) {
                // ejemplo: si el jugador defendió correctamente, no recibe daño
                boolean defended = player.defend(currentDefense(), a);
                if (!defended) {
                    player.applyDamage(a.getDamage());
                } else {
                    // si lo defendió, puede sumar score parcial
                    score += config.getScorePerKill();
                }
                attackManager.dequeueAttack();
            } else {
                // si no está en rango, rotamos la cola para seguir procesando
                // dequeue y reenqueue ya lo hace AttackManager.update; aquí solo avanzamos
                // para evitar consumir ataques fuera de orden, simplemente rotamos:
                attackManager.dequeueAttack();
                attackManager.spawn(a.getType(), level); // re-spawn same type at end (opcional)
            }
        }
    }

    // Placeholder: en la práctica esto vendrá de la GUI (tecla actual)
    private String currentDefense() {
        return null; // null = sin defensa activa
    }

    public GameState getGameState() {
        return new GameState(player.getPlayerId(), player.getHp(), score, level, System.currentTimeMillis());
    }

    public void applyRemoteState(GameState gs) {
        // actualizar representación del oponente en GUI 
    }

    public Player getPlayer() { return player; }
    public AttackManager getAttackManager() { return attackManager; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
}