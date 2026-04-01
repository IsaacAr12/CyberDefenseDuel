package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginScene {

    private final GUIManager guiManager;

    public LoginScene(GUIManager guiManager) {
        this.guiManager = guiManager;
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
        statusLabel.setStyle("-fx-text-fill: red;");

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

            guiManager.getSetupData().setUsername(username);
            statusLabel.setText("Login exitoso (simulado).");
            guiManager.showAvatarScene();
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Debes completar usuario y contraseña.");
                return;
            }

            guiManager.getSetupData().setUsername(username);
            statusLabel.setText("Registro exitoso (simulado).");
            guiManager.showAvatarScene();
        });

        VBox root = new VBox(12, title, subtitle, usernameField, passwordField, loginButton, registerButton, statusLabel);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #101820, #1f2f46);");

        Scene scene = new Scene(root, 900, 600);
        return scene;
    }
}