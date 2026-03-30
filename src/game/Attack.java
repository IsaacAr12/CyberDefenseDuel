package game;

import java.util.UUID;
//Este asunto sencillamente es un ataque
//Tiene inf de daño, tipo, velocidad, y posición
// Al tener valores en x e y, podemos hacer cosas chistosas
public class Attack {
    private final String id;
    private final String type;
    private final int damage;
    private final double baseSpeed;
    private double x;
    private double y;

    public Attack(String type, int damage, double baseSpeed, double startX, double startY) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.damage = damage;
        this.baseSpeed = baseSpeed;
        this.x = startX;
        this.y = startY;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public int getDamage() { return damage; }
    public double getBaseSpeed() { return baseSpeed; }

    public double getX() { return x; }
    public double getY() { return y; }

    
     //Actualiza la posición vertical del ataque.
     // delta segundos transcurridos desde el último frame
     // speedMultiplier es multiplicador (por nivel) aplicado a la velocidad
    
    public void update(double delta, double speedMultiplier) {
        double speed = baseSpeed + speedMultiplier;
        this.y += speed * delta;
    }

    
     //Verifica colisión simple con el jugador (umbral rectangular).
     
    public boolean isInRange(Player p, double thresholdX, double thresholdY) {
        double dx = Math.abs(this.x - p.getX());
        double dy = Math.abs(this.y - p.getY());
        return dx <= thresholdX && dy <= thresholdY;
    }

    public void setPosition(double x, double y) { this.x = x; this.y = y; }
}


