package model;

import java.util.Objects;

public class User {
    private String username;
    private String passwordHash;
    private String avatar;
    private PlayerStats stats;

    public User() {
        this.stats = new PlayerStats();
    }

    public User(String username, String passwordHash, String avatar) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatar = avatar;
        this.stats = new PlayerStats();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public PlayerStats getStats() { return stats; }
    public void setStats(PlayerStats stats) { this.stats = stats; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}