package client.gui;

import client.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MapScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    public MapScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        Label title = new Label("Escoge un mapa");
        title.setStyle(GUIStyles.TITLE);

        Label instruction = new Label("Cuando ambos jugadores escojan, el servidor elige uno al azar.");
        instruction.setStyle(GUIStyles.SUBTITLE);

        ToggleGroup mapGroup = new ToggleGroup();

        RadioButton map1 = new RadioButton("Habitacion de Programador");
        RadioButton map2 = new RadioButton("Oficina de Trabajo");

        map1.setStyle(GUIStyles.LABEL);
        map2.setStyle(GUIStyles.LABEL);

        map1.setToggleGroup(mapGroup);
        map2.setToggleGroup(mapGroup);

        Label statusLabel = new Label();
        statusLabel.setStyle(GUIStyles.ERROR);

        Button confirmButton = new Button("Confirmar mapa");
        confirmButton.setStyle(GUIStyles.BUTTON);

        confirmButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) mapGroup.getSelectedToggle();

            if (selected == null) {
                statusLabel.setText("Debes escoger un mapa.");
                return;
            }

            String mapName = selected.getText();
            guiManager.getSetupData().setSelectedMap(mapName);
            statusLabel.setStyle(GUIStyles.SUBTITLE);
            statusLabel.setText("Esperando decisión final del servidor...");
            controller.sendMapChoice(mapName);
        });

        VBox root = new VBox(14, title, instruction, map1, map2, confirmButton, statusLabel);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(GUIStyles.ROOT);

        return new Scene(root);
    }
}