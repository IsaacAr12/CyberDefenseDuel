package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AvatarScene {

    private final GUIManager guiManager;

    public AvatarScene(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public Scene createScene() {
        Label title = new Label("Escoge tu Personaje");
        title.setStyle(GUIStyles.TITLE);

        Label userLabel = new Label("Jugador: " + guiManager.getSetupData().getUsername());
        userLabel.setStyle(GUIStyles.LABEL);

        ToggleGroup avatarGroup = new ToggleGroup();

        RadioButton avatar1 = new RadioButton("Captain Firewall");
        RadioButton avatar2 = new RadioButton("Byte Ninja");
        RadioButton avatar3 = new RadioButton("Malware Muncher");
        RadioButton avatar4 = new RadioButton("Crypto Llama");
        RadioButton avatar5 = new RadioButton("Packet Pirate");
        RadioButton avatar6 = new RadioButton("Null Pointer Paladin");

        avatar1.setStyle(GUIStyles.LABEL);
        avatar2.setStyle(GUIStyles.LABEL);
        avatar3.setStyle(GUIStyles.LABEL);
        avatar4.setStyle(GUIStyles.LABEL);
        avatar5.setStyle(GUIStyles.LABEL);
        avatar6.setStyle(GUIStyles.LABEL);

        avatar1.setToggleGroup(avatarGroup);
        avatar2.setToggleGroup(avatarGroup);
        avatar3.setToggleGroup(avatarGroup);
        avatar4.setToggleGroup(avatarGroup);
        avatar5.setToggleGroup(avatarGroup);
        avatar6.setToggleGroup(avatarGroup);

        Label statusLabel = new Label();
        statusLabel.setStyle(GUIStyles.ERROR);

        Button continueButton = new Button("Continuar");
        continueButton.setStyle(GUIStyles.BUTTON);

        continueButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) avatarGroup.getSelectedToggle();

            if (selected == null) {
                statusLabel.setText("Debes escoger un personaje.");
                return;
            }

            String avatar = selected.getText();
            guiManager.getSetupData().setSelectedAvatar(avatar);
            guiManager.getController().sendAvatarSelection(avatar);
            guiManager.showMatchmakingScene();
        });

        VBox root = new VBox(
                12,
                title,
                userLabel,
                avatar1,
                avatar2,
                avatar3,
                avatar4,
                avatar5,
                avatar6,
                continueButton,
                statusLabel
        );

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(GUIStyles.ROOT);

        return new Scene(root);
    }
}