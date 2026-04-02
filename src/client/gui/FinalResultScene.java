package client.gui;

import client.PlayerSetupData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FinalResultScene {

    private final GUIManager guiManager;

    public FinalResultScene(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public Scene createScene() {
        PlayerSetupData data = guiManager.getSetupData();

        Label title = new Label("Game Over");
        title.setStyle(GUIStyles.TITLE);

        Label mapLabel = new Label("Mapa jugado: " + data.getFinalMap());
        mapLabel.setStyle(GUIStyles.LABEL);

        Label infoLabel = new Label("La sesión terminó porque ambos jugadores finalizaron.");
        infoLabel.setStyle(GUIStyles.LABEL);

        Button backButton = new Button("Volver al Login");
        backButton.setStyle(GUIStyles.BUTTON);

        backButton.setOnAction(e -> guiManager.showLoginScene());

        VBox root = new VBox(18, title, mapLabel, infoLabel, backButton);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(GUIStyles.ROOT);

        return new Scene(root, 900, 600);
    }
}