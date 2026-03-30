package patterns;

/**
 * Interfaz para prototipo de Ataque.
 * Implementa clone() para duplicar ataques con pequeñas variaciones.
 */
public interface AttackPrototype extends Cloneable {
    AttackPrototype clonePrototype();
    String getType();
    double getSpeed();
    int getDamage();
}