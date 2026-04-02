package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import server.DatabaseManager;

public class AvatarScene {

    private final GUIManager guiManager;
    private final DatabaseManager databaseManager;

    public AvatarScene(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.databaseManager = new DatabaseManager();
    }

    public Scene createScene() {
        Label title = new Label("Escoge tu Personaje");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label userLabel = new Label("Jugador: " + guiManager.getSetupData().getUsername());
        userLabel.setStyle("-fx-font-size: 14px;");

        ToggleGroup avatarGroup = new ToggleGroup();

        RadioButton avatar1 = new RadioButton("Captain Firewall");
        RadioButton avatar2 = new RadioButton("Byte Ninja");
        RadioButton avatar3 = new RadioButton("Malware Muncher");
        RadioButton avatar4 = new RadioButton("Crypto Llama");

        avatar1.setToggleGroup(avatarGroup);
        avatar2.setToggleGroup(avatarGroup);
        avatar3.setToggleGroup(avatarGroup);
        avatar4.setToggleGroup(avatarGroup);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        Button continueButton = new Button("Continuar");
        Button backButton = new Button("Volver");

        continueButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) avatarGroup.getSelectedToggle();

            if (selected == null) {
                statusLabel.setText("Debes escoger un personaje.");
                return;
            }

            String avatar = selected.getText();
            String username = guiManager.getSetupData().getUsername();

            guiManager.getSetupData().setSelectedAvatar(avatar);
            databaseManager.updateAvatar(username, avatar);

            guiManager.showMatchmakingScene();
        });

        backButton.setOnAction(e -> guiManager.showLoginScene());

        VBox root = new VBox(
                12,
                title,
                userLabel,
                avatar1,
                avatar2,
                avatar3,
                avatar4,
                continueButton,
                backButton,
                statusLabel
        );

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #16213e, #0f3460);");

        return new Scene(root, 900, 600);
    }
}