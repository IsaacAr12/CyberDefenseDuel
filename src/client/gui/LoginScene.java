package client.gui;

import client.PlayerSetupData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import server.DatabaseManager;

public class LoginScene {

    private final GUIManager guiManager;
    private final DatabaseManager databaseManager;

    public LoginScene(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.databaseManager = new DatabaseManager();
    }

    public Scene createScene() {
        Label title = new Label("Cyber Defense Duel");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Login / Registro");
        subtitle.setStyle("-fx-font-size: 16px;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nombre de usuario");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px;");

        Button loginButton = new Button("Iniciar Sesión");
        Button registerButton = new Button("Registrarse");

        loginButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setMaxWidth(Double.MAX_VALUE);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Debes completar usuario y contraseña.");
                return;
            }

            boolean loginOk = databaseManager.loginUser(username, password);

            if (loginOk) {
                PlayerSetupData data = guiManager.getSetupData();
                data.setUsername(username);
                statusLabel.setStyle("-fx-text-fill: lightgreen; -fx-font-size: 13px;");
                statusLabel.setText("Login exitoso.");
                guiManager.showAvatarScene();
            } else {
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px;");
                statusLabel.setText("Usuario o contraseña incorrectos.");
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Debes completar usuario y contraseña.");
                return;
            }

            boolean registered = databaseManager.registerUser(username, password);

            if (registered) {
                PlayerSetupData data = guiManager.getSetupData();
                data.setUsername(username);
                statusLabel.setStyle("-fx-text-fill: lightgreen; -fx-font-size: 13px;");
                statusLabel.setText("Usuario registrado correctamente.");
                guiManager.showAvatarScene();
            } else {
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px;");
                statusLabel.setText("Ese usuario ya existe.");
            }
        });

        VBox root = new VBox(12, title, subtitle, usernameField, passwordField, loginButton, registerButton, statusLabel);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #101820, #1f2f46);");

        return new Scene(root, 900, 600);
    }
}