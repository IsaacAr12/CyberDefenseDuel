package model;

import structures.SinglyLinkedList;

 //GameState representa el estado actual de un jugador dentro del juego.
 //Contiene info básica como su identificador, vida, puntuación, nivel
 //y el momento en el que se registró.
 
public class GameState {

    private String playerId;
    private int hp;
    private int score;
    private int level;
    private long timestamp;
    private SinglyLinkedList<AttackSnapshot> activeAttacks;

    public GameState() {
        this.activeAttacks = new SinglyLinkedList<>();
        this.timestamp = System.currentTimeMillis();
    }

    public GameState(String playerId, int hp, int score, int level, long timestamp) {
        this.playerId = playerId;
        this.hp = hp;
        this.score = score;
        this.level = level;
        this.timestamp = timestamp;
        this.activeAttacks = new SinglyLinkedList<>();
    }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // API con tu estructura propia
    public SinglyLinkedList<AttackSnapshot> getActiveAttacksList() { return activeAttacks; }
    public void setActiveAttacksList(SinglyLinkedList<AttackSnapshot> list) { this.activeAttacks = list; }

    // Método auxiliar para serialización: devuelve un array (evita usar java.util.List en dominio)
    public AttackSnapshot[] getActiveAttacksArray() {
        int n = activeAttacks.size();
        AttackSnapshot[] arr = new AttackSnapshot[n];
        int i = 0;
        for (AttackSnapshot s : activeAttacks) {
            arr[i++] = s;
        }
        return arr;
    }

    public void addAttackSnapshot(AttackSnapshot s) {
        this.activeAttacks.addLast(s);
    }

    public static class AttackSnapshot {
        private String id;
        private String type;
        private double x;
        private double y;
        private double speed;
        private int damage;

        public AttackSnapshot() {}

        public AttackSnapshot(String id, String type, double x, double y, double speed, int damage) {
            this.id = id;
            this.type = type;
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.damage = damage;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        public double getSpeed() { return speed; }
        public void setSpeed(double speed) { this.speed = speed; }
        public int getDamage() { return damage; }
        public void setDamage(int damage) { this.damage = damage; }
    }
}

