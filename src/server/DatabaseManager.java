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

    private static class Database {
        private User[] users;

        public Database() {
            this.users = new User[0];
        }

        public User[] getUsers() {
            return users;
        }

        public void setUsers(User[] users) {
            this.users = users;
        }
    }

    private final Path dbPath;
    private final Gson gson;

    public DatabaseManager(String filePath) {
        this.dbPath = Paths.get(filePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            if (!Files.exists(dbPath)) {
                if (dbPath.getParent() != null) {
                    Files.createDirectories(dbPath.getParent());
                }
                saveDatabase(new Database());
            }
        } catch (IOException e) {
            System.out.println("Error inicializando database.json: " + e.getMessage());
        }
    }

    private Database loadDatabase() {
        try (Reader reader = Files.newBufferedReader(dbPath)) {
            Database db = gson.fromJson(reader, Database.class);
            if (db == null || db.getUsers() == null) {
                return new Database();
            }
            return db;
        } catch (IOException e) {
            System.out.println("Error leyendo database.json: " + e.getMessage());
            return new Database();
        }
    }

    private void saveDatabase(Database db) {
        try (Writer writer = Files.newBufferedWriter(dbPath)) {
            gson.toJson(db, writer);
        } catch (IOException e) {
            System.out.println("Error guardando database.json: " + e.getMessage());
        }
    }

    public synchronized boolean registerUser(String username, String password) {
        Database db = loadDatabase();

        for (User user : db.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }

        User newUser = new User(username, password);

        User[] oldUsers = db.getUsers();
        User[] newUsers = new User[oldUsers.length + 1];

        for (int i = 0; i < oldUsers.length; i++) {
            newUsers[i] = oldUsers[i];
        }

        newUsers[oldUsers.length] = newUser;
        db.setUsers(newUsers);
        saveDatabase(db);

        return true;
    }

    public synchronized boolean loginUser(String username, String password) {
        Database db = loadDatabase();

        for (User user : db.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)
                    && user.getPasswordHash().equals(password)) {
                return true;
            }
        }

        return false;
    }
}