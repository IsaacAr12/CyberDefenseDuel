package client.gui;

import client.PlayerSetupData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FinalMapScene {

    private final GUIManager guiManager;

    public FinalMapScene(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public Scene createScene() {
        PlayerSetupData data = guiManager.getSetupData();

        Label title = new Label("Mapa seleccionado");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label userChoice = new Label("Tu mapa: " + data.getSelectedMap());
        Label opponentChoice = new Label("Mapa del oponente: " + data.getOpponentMap());
        Label finalChoice = new Label("Mapa final aleatorio: " + data.getFinalMap());

        userChoice.setStyle("-fx-font-size: 16px;");
        opponentChoice.setStyle("-fx-font-size: 16px;");
        finalChoice.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: lightgreen;");

        Button continueButton = new Button("Continuar");
        Button backButton = new Button("Volver a mapas");

        continueButton.setOnAction(e -> {
            // Aquí luego puedes mandar a GameScene
            System.out.println("Continuar al juego con mapa: " + data.getFinalMap());
        });

        backButton.setOnAction(e -> guiManager.showMapScene());

        VBox root = new VBox(16, title, userChoice, opponentChoice, finalChoice, continueButton, backButton);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #263238, #37474f);");

        return new Scene(root, 900, 600);
    }
}