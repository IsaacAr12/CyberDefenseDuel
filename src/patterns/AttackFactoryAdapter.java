package patterns;

import game.Attack;
import game.Config;


 //Adapter que convierte AttackPrototype (de AttackFactoryImpl) en game.Attack.
 //Asume que AttackFactoryImpl tiene un método:
 // AttackPrototype createAttack(String type);
 //y que AttackPrototype expone getters: getType(), getSpeed(), getDamage().
 
public class AttackFactoryAdapter {

    private final AttackFactoryImpl protoFactory;
    private final Config config;

    public AttackFactoryAdapter(AttackFactoryImpl protoFactory, Config config) {
        this.protoFactory = protoFactory;
        this.config = config;
    }

    
     //Crea una instancia de game.Attack a partir del prototipo.
    
    public Attack createAttack(String type, int level, double startX, double startY) {
        AttackPrototype proto = protoFactory.createAttack(type);
        if (proto == null) {
            // fallback si el prototipo no existe
            proto = new AttackPrototype() {
                @Override public AttackPrototype clonePrototype() { return this; }
                @Override public String getType() { return type.toUpperCase(); }
                @Override public double getSpeed() { return 1.0; }
                @Override public int getDamage() { return 5; }
            };
        }

        // Base values from prototype
        int baseDamage = proto.getDamage();
        double protoSpeed = proto.getSpeed();

        // Escalado por nivel (ajusta fórmula si quieres otra)
        int damage = baseDamage + Math.max(0, level - 1) * 2;
        double baseSpeed = protoSpeed + Math.max(0, level - 1) * config.getSpeedAddPerLevel();

        return new Attack(proto.getType(), damage, baseSpeed, startX, startY);
    }
}