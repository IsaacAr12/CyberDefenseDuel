package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public boolean registerUser(String username, String password) {
        return registerUser(username, password, "Captain Firewall");
    }

    public boolean registerUser(String username, String password, String avatar) {
        if (findUser(username) != null) {
            return false;
        }

        User newUser = new User(username, password, avatar);
        addUser(newUser);
        saveDatabase();
        return true;
    }

    public boolean loginUser(String username, String password) {
        User user = findUser(username);
        return user != null && user.getPassword().equals(password);
    }

    public User findUser(String username) {
        for (User user : users) {
            if (user != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean updateAvatar(String username, String avatar) {
        User user = findUser(username);
        if (user == null) {
            return false;
        }

        user.setAvatar(avatar);
        saveDatabase();
        return true;
    }

    public User[] getUsers() {
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