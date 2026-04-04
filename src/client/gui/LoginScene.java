package client.gui;

import client.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    public LoginScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #14a3dc;");

        Label title = new Label("CYBER DEFENSE DUEL");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #a64ac9;" +
                "-fx-padding: 12 28 12 28;"
        );

        Label subtitle = new Label("LOGIN / REGISTRO");
        subtitle.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        VBox topBox = new VBox(14, title, subtitle);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(30, 0, 10, 0));
        root.setTop(topBox);

        TextField hostField = new TextField("127.0.0.1");
        hostField.setPromptText("IP del servidor");
        styleField(hostField);

        TextField portField = new TextField("5000");
        portField.setPromptText("Puerto");
        styleField(portField);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario");
        styleField(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        styleField(passwordField);

        Label statusLabel = new Label("Conéctate al servidor para iniciar");
        statusLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Button connectButton = createPurpleButton("CONECTAR");
        Button loginButton = createPurpleButton("INICIAR SESIÓN");
        Button registerButton = createPurpleButton("REGISTRARSE");

        connectButton.setOnAction(e -> {
            try {
                controller.connect(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()));
                statusLabel.setStyle(
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );
                statusLabel.setText("Conectado al servidor correctamente.");
            } catch (Exception ex) {
                statusLabel.setStyle(
                        "-fx-text-fill: #ffe600;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );
                statusLabel.setText("No se pudo conectar al servidor.");
            }
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setStyle(
                        "-fx-text-fill: #ffe600;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );
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
                statusLabel.setStyle(
                        "-fx-text-fill: #ffe600;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );
                statusLabel.setText("Debes completar usuario y contraseña.");
                return;
            }

            guiManager.getSetupData().setUsername(username);
            controller.register(username, password);
        });

        HBox hostPortBox = new HBox(14, hostField, portField);
        hostPortBox.setAlignment(Pos.CENTER);

        HBox buttonsBox = new HBox(16, loginButton, registerButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(
                18,
                hostPortBox,
                usernameField,
                passwordField,
                connectButton,
                buttonsBox,
                statusLabel
        );
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(30));
        root.setCenter(centerBox);

        guiManager.setLoginStatusLabel(statusLabel);

        return new Scene(root);
    }

    private void styleField(TextField field) {
        field.setMaxWidth(320);
        field.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 10 16 10 16;"
        );
    }

    private Button createPurpleButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #a64ac9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 16;" +
                "-fx-padding: 10 22 10 22;"
        );
        return button;
    }
}