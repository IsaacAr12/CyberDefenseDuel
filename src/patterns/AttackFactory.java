package patterns;

import game.Attack;

public interface AttackFactory {
    Attack createAttack(String type);
    Attack createAttack(String type, int damage, double x, double y);
}