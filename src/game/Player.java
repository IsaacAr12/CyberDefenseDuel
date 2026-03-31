package game;

public class Player {

    private final String playerId;
    private int hp;
    private double x;
    private double y;          
    private final String avatar;
    private String defenseType; // defensa activa (null si ninguna)

    public Player(String playerId, int initialHp, String avatar) {
        this.playerId = playerId;
        this.hp = initialHp;
        this.avatar = avatar;
        this.x = 0.0;
        this.y = 0.0;          
        this.defenseType = null;
    }

    public String getPlayerId() { return playerId; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }         
    public void setY(double y) { this.y = y; }  

    public String getAvatar() { return avatar; }

    public void moveLeft(double delta, double speed) { this.x -= speed * delta; }
    public void moveRight(double delta, double speed) { this.x += speed * delta; }

    public void applyDamage(int d) {
        this.hp -= d;
        if (this.hp < 0) this.hp = 0;
    }

    public boolean defend(String defenseType, Attack attack) {
        if (defenseType == null || attack == null) return false;
        return defenseType.equalsIgnoreCase(attack.getType());
    }

    // métodos para manejar defensa desde GUI/Controller
    public void setDefense(String defenseType) { this.defenseType = defenseType; }
    public void clearDefense() { this.defenseType = null; }
    public String getDefenseType() { return defenseType; }
}