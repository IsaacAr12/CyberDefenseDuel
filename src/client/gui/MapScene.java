package client.gui;

import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MapScene {

    private static final String MAPA_1 = "Habitacion de Programador";
    private static final String MAPA_2 = "Oficina de Trabajo";

    private final GUIManager guiManager;
    private final Random random;

    public MapScene(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.random = new Random();
    }

    public Scene createScene() {
        Label title = new Label("Escoge un mapa");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label instruction = new Label("Cuando ambos jugadores escogen, se selecciona uno al azar.");
        instruction.setStyle("-fx-font-size: 14px;");

        ToggleGroup mapGroup = new ToggleGroup();

        RadioButton map1 = new RadioButton(MAPA_1);
        RadioButton map2 = new RadioButton(MAPA_2);

        map1.setToggleGroup(mapGroup);
        map2.setToggleGroup(mapGroup);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: yellow;");

        Button confirmButton = new Button("Confirmar mapa");
        Button backButton = new Button("Volver");

        confirmButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) mapGroup.getSelectedToggle();

            if (selected == null) {
                statusLabel.setText("Debes escoger un mapa.");
                return;
            }

            String playerMap = selected.getText();
            guiManager.getSetupData().setSelectedMap(playerMap);

            // Simulación del mapa escogido por el oponente
            String opponentMap = random.nextBoolean() ? MAPA_1 : MAPA_2;
            guiManager.getSetupData().setOpponentMap(opponentMap);

            // Escoger uno aleatorio entre ambos mapas elegidos
            String finalMap = random.nextBoolean() ? playerMap : opponentMap;
            guiManager.getSetupData().setFinalMap(finalMap);

            guiManager.showFinalMapMessage();
        });

        backButton.setOnAction(e -> guiManager.showMatchmakingScene());

        VBox root = new VBox(14, title, instruction, map1, map2, confirmButton, backButton, statusLabel);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #222831, #393e46);");

        return new Scene(root, 900, 600);
    }
}