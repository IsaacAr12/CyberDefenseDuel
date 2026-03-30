package game;

//Este asunto sencillamente es un ataque
//Tiene inf de daño, tipo, velocidad, y posición
// Al tener valores en x e y, podemos hacer cosas chistosas
public class Attack {
    private final String type;
    private final int damage;
    private final double speed;
    private double x, y;

    public Attack(String type, int damage, double speed, double startX, double startY) {
        this.type = type;
        this.damage = damage;
        this.speed = speed;
        this.x = startX;
        this.y = startY;
    }
    //Actualiza la posición del ataque en función del tiempo transcurrido.
    //Se asume que el ataque se mueve verticalmente en el eje Y, aunque podría 
    //cambiar esto mas adelante
    public void update(double delta) {
        this.y += speed * delta;
    }
    //Determina si el ataque está en el rango efectivo de
    //impacto de jugador.
    public boolean isInRange(Player p) {
        return Math.abs(this.x - p.getX()) < 10 && this.y >= p.getY();
    }

    public String getType() { return type; }
    public int getDamage() { return damage; }
    public double getSpeed() { return speed; }
    public double getX() { return x; }
    public double getY() { return y; }
}