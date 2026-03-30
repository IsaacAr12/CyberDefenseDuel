package game;

public class Player {
    private final String playerId;
    private int hp;
    private double x;
    private final String avatar;

    public Player(String playerId, int initialHp, String avatar) {
        this.playerId = playerId;
        this.hp = initialHp;
        this.avatar = avatar;
        this.x = 0.0;
    }

    public String getPlayerId() { return playerId; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public String getAvatar() { return avatar; }

    public void moveLeft(double delta, double speed) {
        this.x -= speed * delta;
    }

    public void moveRight(double delta, double speed) {
        this.x += speed * delta;
    }

    public void applyDamage(int d) {
        this.hp -= d;
        if (this.hp < 0) this.hp = 0;
    }

    
     //Comprueba si la defensa elegida neutraliza el ataque.
     // Retorna true si la defensa coincide con el tipo de ataque
     
    public boolean defend(String defenseType, Attack attack) {
        if (defenseType == null || attack == null) return false;
        return defenseType.equalsIgnoreCase(attack.getType());
    }
}