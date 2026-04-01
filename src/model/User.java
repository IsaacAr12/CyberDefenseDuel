package model;

public class User {
    private String username;
    private String password;
    private String avatar;
    private PlayerStats stats;

    public User() {
        this.username = "";
        this.password = "";
        this.avatar = "Captain Firewall";
        this.stats = new PlayerStats();
    }

    public User(String username, String password, String avatar) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.stats = new PlayerStats();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }
}