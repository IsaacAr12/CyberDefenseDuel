package game;

import model.GameState;
import structures.CustomQueue;

public class GameEngine {
    private Player player;
    private AttackManager attackManager;
    private int hp;
    private int score;
    private int level;
    private Config config;

    public GameEngine(Config cfg) {
        this.config = cfg;
        this.hp = cfg.getInitialHp();
        this.score = 0;
        this.level = 1;
        this.player = new Player("localPlayer", cfg.getInitialHp(), "defaultAvatar");
        this.attackManager = new AttackManager(cfg);
    }

    public void update(double delta) {
        // Actualizar ataques
        attackManager.update(delta, level);

        // Verificar colisiones
        checkCollisions();

        // Subir nivel si corresponde
        if (score >= level * config.getDifficultyStepScore()) {
            level++;
        }
    }

    private void checkCollisions() {
        Attack attack = attackManager.peekAttack();
        while (attack != null) {
            if (attack.isInRange(player)) {
                player.applyDamage(attack.getDamage());
                hp = player.getHp();
                attackManager.dequeueAttack();
            }
            attack = attackManager.peekAttack();
        }
    }

    public GameState getGameState() {
        return new GameState(player.getPlayerId(), hp, score, level, System.currentTimeMillis());
    }

    public void applyRemoteState(GameState gs) {
        // Aquí se podría mostrar el estado de oponente ya en GUI
    }
}