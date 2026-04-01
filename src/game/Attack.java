package game;

public class Attack {
    private String id;
    private String type;
    private int damage;
    private double baseSpeed;
    private double x;
    private double y;

    public Attack(String type, int damage, double baseSpeed, double x, double y) {
        this.id = type + "_" + System.nanoTime();
        this.type = type;
        this.damage = damage;
        this.baseSpeed = baseSpeed;
        this.x = x;
        this.y = y;
    }

    public Attack(String id, String type, double x, double y, double baseSpeed, int damage) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.baseSpeed = baseSpeed;
        this.damage = damage;
    }

    public void update(double delta) {
        y += baseSpeed * delta;
    }

    public void update(double delta, double speedMultiplier) {
        y += (baseSpeed * speedMultiplier) * delta;
    }

    public boolean isInRange(Player player) {
        return isInRange(player, 40.0, 40.0);
    }

    public boolean isInRange(Player player, double thresholdX, double thresholdY) {
        if (player == null) {
            return false;
        }

        double playerX = tryGetPlayerX(player);
        double playerY = tryGetPlayerY(player);

        return Math.abs(this.x - playerX) <= thresholdX
                && Math.abs(this.y - playerY) <= thresholdY;
    }

    private double tryGetPlayerX(Player player) {
        try {
            return ((Number) player.getClass().getMethod("getX").invoke(player)).doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double tryGetPlayerY(Player player) {
        try {
            return ((Number) player.getClass().getMethod("getY").invoke(player)).doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getSpeed() {
        return baseSpeed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void setSpeed(double speed) {
        this.baseSpeed = speed;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}