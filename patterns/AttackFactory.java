package patterns;

/**
 * Esto es un Factory method para crear ataques por tipo, DDOS o los otros que ya no recuerdo como se llaman.
 */
public interface AttackFactory {
    AttackPrototype createAttack(String type);
}