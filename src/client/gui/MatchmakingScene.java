package client.gui;

import client.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MatchmakingScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    public MatchmakingScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        Label title = new Label("Matchmaking");
        title.setStyle(GUIStyles.TITLE);

        Label info = new Label("Avatar: " + guiManager.getSetupData().getSelectedAvatar());
        info.setStyle(GUIStyles.LABEL);

        Label status = new Label("Presiona buscar para entrar a la cola.");
        status.setStyle(GUIStyles.SUBTITLE);

        Button searchButton = new Button("Buscar Partida");
        searchButton.setStyle(GUIStyles.BUTTON);

        Button backButton = new Button("Volver");
        backButton.setStyle(GUIStyles.BUTTON);

        searchButton.setOnAction(e -> {
            status.setText("Buscando rival...");
            controller.requestMatch();
        });

        backButton.setOnAction(e -> guiManager.showAvatarScene());

        guiManager.setMatchmakingStatusLabel(status);

        VBox root = new VBox(16, title, info, status, searchButton, backButton);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(GUIStyles.ROOT);

        return new Scene(root);
    }
}