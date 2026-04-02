package client.gui;

import client.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    public LoginScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        Label title = new Label("Cyber Defense Duel");
        title.setStyle(GUIStyles.TITLE);

        Label subtitle = new Label("Login / Registro");
        subtitle.setStyle(GUIStyles.SUBTITLE);

        TextField hostField = new TextField("127.0.0.1");
        hostField.setPromptText("IP del servidor");
        hostField.setStyle(GUIStyles.FIELD);

        TextField portField = new TextField("5000");
        portField.setPromptText("Puerto");
        portField.setStyle(GUIStyles.FIELD);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nombre de usuario");
        usernameField.setStyle(GUIStyles.FIELD);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.setStyle(GUIStyles.FIELD);

        Label statusLabel = new Label();
        statusLabel.setStyle(GUIStyles.ERROR);

        Button connectButton = new Button("Conectar");
        connectButton.setStyle(GUIStyles.BUTTON);

        Button loginButton = new Button("Iniciar Sesión");
        loginButton.setStyle(GUIStyles.BUTTON);

        Button registerButton = new Button("Registrarse");
        registerButton.setStyle(GUIStyles.BUTTON);

        connectButton.setOnAction(e -> {
            try {
                controller.connect(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()));
                statusLabel.setStyle(GUIStyles.SUCCESS);
                statusLabel.setText("Conectado al servidor.");
            } catch (Exception ex) {
                statusLabel.setStyle(GUIStyles.ERROR);
                statusLabel.setText("No se pudo conectar al servidor.");
            }
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setStyle(GUIStyles.ERROR);
                statusLabel.setText("Debes completar usuario y contraseña.");
                return;
            }

            guiManager.getSetupData().setUsername(username);
            controller.login(username, password);
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setStyle(GUIStyles.ERROR);
                statusLabel.setText("Debes completar usuario y contraseña.");
                return;
            }

            guiManager.getSetupData().setUsername(username);
            controller.register(username, password);
        });

        guiManager.setLoginStatusLabel(statusLabel);

        VBox root = new VBox(
                12,
                title,
                subtitle,
                hostField,
                portField,
                usernameField,
                passwordField,
                connectButton,
                loginButton,
                registerButton,
                statusLabel
        );

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(GUIStyles.ROOT);

        return new Scene(root, 900, 600);
    }
}