package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.PlayerStats;
import model.User;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseManager {

    private final Path dbPath;
    private final Gson gson;
    private User[] users;

    public DatabaseManager() {
        this("database.json");
    }

    public DatabaseManager(String filePath) {
        this.dbPath = Paths.get(filePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new User[0];
        loadDatabase();
    }

    private void loadDatabase() {
        try {
            if (!Files.exists(dbPath)) {
                saveDatabase();
                return;
            }

            try (Reader reader = Files.newBufferedReader(dbPath)) {
                User[] loadedUsers = gson.fromJson(reader, User[].class);
                if (loadedUsers != null) {
                    users = loadedUsers;
                } else {
                    users = new User[0];
                }
            }

            for (int i = 0; i < users.length; i++) {
                if (users[i] == null) {
                    continue;
                }

                if (users[i].getStats() == null) {
                    users[i].setStats(new PlayerStats());
                }

                if (users[i].getAvatar() == null || users[i].getAvatar().isEmpty()) {
                    users[i].setAvatar("Captain Firewall");
                }
            }

        } catch (IOException e) {
            users = new User[0];
            System.out.println("No se pudo leer database.json: " + e.getMessage());
        }
    }

    private void saveDatabase() {
        try (Writer writer = Files.newBufferedWriter(dbPath)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("No se pudo guardar database.json: " + e.getMessage());
        }
    }

    public synchronized boolean registerUser(String username, String password) {
        if (findUser(username) != null) {
            return false;
        }

        User newUser = new User(username, password, "Captain Firewall");
        addUser(newUser);
        saveDatabase();
        return true;
    }

    public synchronized boolean loginUser(String username, String password) {
        User user = findUser(username);
        return user != null && user.getPassword().equals(password);
    }

    public synchronized User findUser(String username) {
        for (int i = 0; i < users.length; i++) {
            User user = users[i];
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public synchronized boolean updateAvatar(String username, String avatar) {
        User user = findUser(username);
        if (user == null) {
            return false;
        }

        user.setAvatar(avatar);
        saveDatabase();
        return true;
    }

    public synchronized void updateEndSessionStats(String username, int score, int networkXp, int malwareXp, int cryptoXp) {
        User user = findUser(username);
        if (user == null) {
            return;
        }

        PlayerStats stats = user.getStats();
        stats.addTotalScore(score);
        stats.addGamePlayed();
        stats.addNetworkXp(networkXp);
        stats.addMalwareXp(malwareXp);
        stats.addCryptoXp(cryptoXp);

        saveDatabase();
    }

    public synchronized User[] getUsers() {
        return users;
    }

    private void addUser(User user) {
        User[] newUsers = new User[users.length + 1];
        for (int i = 0; i < users.length; i++) {
            newUsers[i] = users[i];
        }
        newUsers[users.length] = user;
        users = newUsers;
    }
}