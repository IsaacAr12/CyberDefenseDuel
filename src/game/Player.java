package game;

public class Player {
    private String playerId;
    private int hp;
    private int x;
    private String avatar;

    public Player(String id, int hp, String avatar) {
        this.playerId = id;
        this.hp = hp;
        this.avatar = avatar;
        this.x = 0;
    }

    public void moveLeft() { x -= 5; }
    public void moveRight() { x += 5; }
    public void applyDamage(int d) { hp -= d; }
    public boolean defend(String defenseType, Attack attack) {
        return defenseType.equalsIgnoreCase(attack.getType());
    }

    public String getPlayerId() { return playerId; }
    public int getHp() { return hp; }
    public int getX() { return x; }
    public String getAvatar() { return avatar; }
}