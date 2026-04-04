package client.gui;

import client.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MatchmakingScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    public MatchmakingScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #14a3dc;");

        Label title = new Label("BUSCAR PARTIDA");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #a64ac9;" +
                "-fx-padding: 12 28 12 28;"
        );

        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(30, 0, 10, 0));
        root.setTop(topBox);

        Label usernameLabel = new Label("Jugador: " + guiManager.getSetupData().getUsername());
        usernameLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        Label avatarLabel = new Label("Avatar: " + guiManager.getSetupData().getSelectedAvatar());
        avatarLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        Label status = new Label("Presiona buscar para entrar a la cola.");
        status.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Button searchButton = createPurpleButton("BUSCAR PARTIDA");
        Button backButton = createPurpleButton("VOLVER");

        searchButton.setOnAction(e -> {
            status.setText("Buscando rival...");
            controller.requestMatch();
        });

        backButton.setOnAction(e -> guiManager.showAvatarScene());

        VBox centerBox = new VBox(
                20,
                usernameLabel,
                avatarLabel,
                status,
                searchButton,
                backButton
        );
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(30));
        root.setCenter(centerBox);

        guiManager.setMatchmakingStatusLabel(status);

        return new Scene(root);
    }

    private Button createPurpleButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #a64ac9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 16;" +
                "-fx-padding: 10 24 10 24;"
        );
        return button;
    }
}