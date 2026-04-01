package patterns;

import game.Attack;
import game.Config;

import java.util.HashMap;
import java.util.Map;


 //Factory que usa prototipos internos para construir instancias de game.Attack.
 
public class AttackFactoryImpl {

    private static class Prototype {
        String type;
        double baseSpeed;
        int baseDamage;
        Prototype(String type, double baseSpeed, int baseDamage) {
            this.type = type;
            this.baseSpeed = baseSpeed;
            this.baseDamage = baseDamage;
        }
    }

    private final Map<String, Prototype> prototypes = new HashMap<>();
    private final Config config; // opcional: para leer ajustes globales

    public AttackFactoryImpl(Config config) {
        this.config = config;
        // valores por defecto; puedes leerlos desde config si lo prefieres
        prototypes.put("DDOS", new Prototype("DDOS", 1.5, 5));
        prototypes.put("MALWARE", new Prototype("MALWARE", 1.0, 8));
        prototypes.put("CRED", new Prototype("CRED", 1.2, 10));
    }

    /**
     * Crea un game.Attack listo para usarse.
     * @param type tipo de ataque (DDOS, MALWARE, CRED)
     * @param level nivel del juego (para escalar daño/velocidad)
     * @param startX posición inicial X
     * @param startY posición inicial Y
     */
    public Attack createAttack(String type, int level, double startX, double startY) {
        Prototype p = prototypes.get(type.toUpperCase());
        if (p == null) {
            // fallback: tipo desconocido
            p = new Prototype(type.toUpperCase(), 1.0, 5);
        }

        // Escalado simple por nivel (ajusta fórmula según tu Config)
        int damage = p.baseDamage + Math.max(0, level - 1) * 2;
        double baseSpeed = p.baseSpeed + Math.max(0, level - 1) * (config != null ? config.getSpeedAddPerLevel() : 0.1);

        return new Attack(p.type, damage, baseSpeed, startX, startY);
    }

    // permitir actualizar prototipos en runtime. OPC
    public void setPrototype(String type, double baseSpeed, int baseDamage) {
        prototypes.put(type.toUpperCase(), new Prototype(type.toUpperCase(), baseSpeed, baseDamage));
    }
}