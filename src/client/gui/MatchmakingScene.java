package client.gui;

import client.ClientController;
import client.audio.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.layout.Region;

public class MatchmakingScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    public MatchmakingScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        SoundManager.playMusic("/sounds/MENU.mp3", 0.45);

        BorderPane root = new BorderPane();
        setBackgroundImage(root, "/images/Busqueda.png");

        Label title = new Label("BUSCAR PARTIDA");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(166,74,201,0.92);" +
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
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(0,0,0,0.35);" +
                "-fx-padding: 8 16 8 16;" +
                "-fx-background-radius: 14;"
        );

        Label avatarLabel = new Label("Avatar: " + guiManager.getSetupData().getSelectedAvatar());
        avatarLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(0,0,0,0.35);" +
                "-fx-padding: 8 16 8 16;" +
                "-fx-background-radius: 14;"
        );

        Label status = new Label("Presiona buscar para entrar a la cola.");
        status.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(0,0,0,0.35);" +
                "-fx-padding: 8 16 8 16;" +
                "-fx-background-radius: 14;"
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

    private void setBackgroundImage(Region region, String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                region.setStyle("-fx-background-color: black;");
                return;
            }

            BackgroundSize size = new BackgroundSize(
                    100, 100,
                    true, true,
                    true, true
            );

            BackgroundImage bg = new BackgroundImage(
                    new Image(url.toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    size
            );

            region.setBackground(new Background(bg));
        } catch (Exception e) {
            region.setStyle("-fx-background-color: black;");
        }
    }

    private Button createPurpleButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: rgba(166,74,201,0.95);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 16;" +
                "-fx-padding: 10 24 10 24;"
        );
        return button;
    }
}