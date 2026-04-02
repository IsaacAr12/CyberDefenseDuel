package patterns;

import game.Attack;
import game.Config;

public class AttackFactoryImpl implements AttackFactory {

    private final Config config;

    public AttackFactoryImpl() {
        this.config = null;
    }

    public AttackFactoryImpl(Config config) {
        this.config = config;
    }

    @Override
    public Attack createAttack(String type) {
        int damage = resolveDamage(type);
        double baseSpeed = resolveBaseSpeed(type);

        return new Attack(type, damage, baseSpeed, 0.0, 0.0);
    }

    @Override
    public Attack createAttack(String type, int damage, double x, double y) {
        double baseSpeed = resolveBaseSpeed(type);
        return new Attack(type, damage, baseSpeed, x, y);
    }

    private int resolveDamage(String type) {
        if (config != null) {
            int value = config.getDamageForType(type);
            if (value > 0) {
                return value;
            }
        }

        switch (type.toUpperCase()) {
            case "DDOS":
                return 5;
            case "MALWARE":
                return 8;
            case "CRED":
                return 10;
            default:
                return 5;
        }
    }

    private double resolveBaseSpeed(String type) {
        double base = 2.0;

        if (config != null) {
            base = config.getBaseAttackSpeed();
        }

        switch (type.toUpperCase()) {
            case "DDOS":
                return base;
            case "MALWARE":
                return base + 0.5;
            case "CRED":
                return base + 1.0;
            default:
                return base;
        }
    }
}