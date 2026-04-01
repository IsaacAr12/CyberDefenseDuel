package client.gui;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MatchmakingScene {

    private final GUIManager guiManager;

    public MatchmakingScene(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public Scene createScene() {
        Label title = new Label("Buscando otro jugador...");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label info = new Label("Avatar escogido: " + guiManager.getSetupData().getSelectedAvatar());
        info.setStyle("-fx-font-size: 16px;");

        Label status = new Label("Esperando emparejamiento...");
        status.setStyle("-fx-font-size: 14px; -fx-text-fill: yellow;");

        Button simulateMatchButton = new Button("Simular jugador encontrado");
        Button backButton = new Button("Volver");

        simulateMatchButton.setOnAction(e -> {
            status.setText("¡Jugador encontrado!");
            simulateMatchButton.setDisable(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> guiManager.showMapScene());
            pause.play();
        });

        backButton.setOnAction(e -> guiManager.showAvatarScene());

        VBox root = new VBox(16, title, info, status, simulateMatchButton, backButton);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b262c, #0f4c75);");

        return new Scene(root, 900, 600);
    }
}