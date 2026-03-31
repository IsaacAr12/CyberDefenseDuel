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

    
     //Actualiza el motor: mueve ataques, genera spawns y procesa colisiones.
     //@param delta tiempo en segundos desde el último frame (ej. 0.016 para 60 FPS)
     
    public void update(double delta) {
        // Nota: AttackManager mantiene su propio frameCounter; lo usamos para logs.
        attackManager.update(delta, level);

        // Procesa colisiones y aplica daño/score
        checkCollisions();

        // Progresión de nivel basada en score
        if (score >= level * config.getDifficultyStepScore()) {
            level++;
            System.out.printf("[LevelUp] frame=%d newLevel=%d score=%d%n",
                    attackManager.getFrameCounter(), level, score);
        }

        // Estado final del frame (log) usando getters del AttackManager
        System.out.printf("[State] frame=%d time=%d hp=%d score=%d level=%d active=%d spawnTimer=%.3f spawnRate=%.3f%n",
                attackManager.getFrameCounter(),
                System.currentTimeMillis(),
                player.getHp(),
                score,
                level,
                attackManager.activeCount(),
                attackManager.getSpawnTimer(),
                config.getBaseSpawnRate()
        );
    }

    
     //Recorre la cola de ataques y procesa colisiones.
     //- Si colisiona: aplica daño o suma score si defendido.
     //- Si no colisiona: re-enqueuea la misma instancia (enqueueExisting).
     
    private void checkCollisions() {
        double thresholdX = 20.0;
        double thresholdY = 20.0;

        int iterations = attackManager.activeCount();
        for (int i = 0; i < iterations; i++) {
            Attack a = attackManager.dequeueAttack();
            if (a == null) break;

            boolean inRange = a.isInRange(player, thresholdX, thresholdY);
            System.out.printf("[CollisionCheck] frame=%d attack=%s ax=%.2f ay=%.2f px=%.2f py=%.2f inRange=%b%n",
                    attackManager.getFrameCounter(), a.getId(), a.getX(), a.getY(), player.getX(), player.getY(), inRange);

            if (inRange) {
                boolean defended = player.defend(player.getDefenseType(), a);
                System.out.printf("[Collision] frame=%d attack=%s defended=%b%n",
                        attackManager.getFrameCounter(), a.getId(), defended);

                if (!defended) {
                    player.applyDamage(a.getDamage());
                    System.out.printf("[Damage] frame=%d attack=%s damage=%d newHp=%d%n",
                            attackManager.getFrameCounter(), a.getId(), a.getDamage(), player.getHp());
                } else {
                    score += config.getScorePerKill();
                    System.out.printf("[Score] frame=%d defended attack=%s score=%d%n",
                            attackManager.getFrameCounter(), a.getId(), score);
                }
                // ataque consumido: no re-enqueue
            } else {
                // No colisionó: re-enqueue la misma instancia (no crear una nueva)
                attackManager.enqueueExisting(a);
            }
        }
    }

    
     // Devuelve un snapshot del estado del juego para serializar / enviar al cliente.
     
    public GameState getGameState() {
        GameState gs = new GameState(player.getPlayerId(), player.getHp(), score, level, System.currentTimeMillis());
        gs.setActiveAttacksList(attackManager.exportSnapshots());
        return gs;
    }

    
     //Aplicar estado remoto (placeholder).
    
    public void applyRemoteState(GameState gs) {
        if (gs == null) return;
        // Sincronizar HP/score/level si procede
        this.player.setHp(gs.getHp());
        this.score = gs.getScore();
        this.level = gs.getLevel();
    }

    /* Getters útiles */

    public Player getPlayer() { return player; }

    public AttackManager getAttackManager() { return attackManager; }

    public int getScore() { return score; }

    public int getLevel() { return level; }
}