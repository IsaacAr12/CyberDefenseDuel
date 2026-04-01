package patterns;

import game.Attack;
import game.Config;

public class AttackFactoryAdapter {

    private final AttackFactoryImpl prototype;

    public AttackFactoryAdapter() {
        this.prototype = new AttackFactoryImpl();
    }

    public AttackFactoryAdapter(Config config) {
        this.prototype = new AttackFactoryImpl(config);
    }

    public Attack createAttack(String type) {
        return prototype.createAttack(type);
    }

    public Attack createAttack(String type, int damage, double x, double y) {
        return prototype.createAttack(type, damage, x, y);
    }
}