package patterns;

/**
 * Esto es una implementación simple del factory que devuelve prototipos, si es que lo hice bien.
 * Lo podriamos ampliar para que tome valores desde config si fuese necesario.
 */
public class AttackFactoryImpl implements AttackFactory {
    private AttackPrototype ddosPrototype;
    private AttackPrototype malwarePrototype;
    private AttackPrototype credPrototype;

    public AttackFactoryImpl() {
        // prototipos base
        ddosPrototype = new SimpleAttack("DDOS", 1.5, 5);
        malwarePrototype = new SimpleAttack("MALWARE", 1.0, 8);
        credPrototype = new SimpleAttack("CRED", 1.2, 10);
    }

    @Override
    public AttackPrototype createAttack(String type) {
        switch (type.toUpperCase()) {
            case "DDOS": return ddosPrototype.clonePrototype();
            case "MALWARE": return malwarePrototype.clonePrototype();
            case "CRED": return credPrototype.clonePrototype();
            default: return null;
        }
    }

    // Clase interna simple que implementa AttackPrototype
    private static class SimpleAttack implements AttackPrototype {
        private String type;
        private double speed;
        private int damage;

        public SimpleAttack(String type, double speed, int damage) {
            this.type = type;
            this.speed = speed;
            this.damage = damage;
        }

        @Override
        public AttackPrototype clonePrototype() {
            return new SimpleAttack(type, speed, damage);
        }

        @Override public String getType() { return type; }
        @Override public double getSpeed() { return speed; }
        @Override public int getDamage() { return damage; }
    }
}