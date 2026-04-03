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

        Label mapLabel = new Label("Mapa: " + data.getFinalMap());
        mapLabel.setStyle(GUIStyles.LABEL);

        Label yourScore = new Label(
                "Tu puntaje: " + data.getFinalScore() +
                " | Tu nivel: " + data.getFinalLevel() +
                " | Tu HP final: " + data.getFinalHp()
        );
        yourScore.setStyle(GUIStyles.LABEL);

        Label opponentScore = new Label(
                "Puntaje rival: " + data.getOpponentFinalScore() +
                " | Nivel rival: " + data.getOpponentFinalLevel() +
                " | HP rival final: " + data.getOpponentFinalHp()
        );
        opponentScore.setStyle(GUIStyles.LABEL);

        Label yourSpec = new Label("Tu especialización: " + getSpecialization(
                data.getNetworkXp(),
                data.getMalwareXp(),
                data.getCryptoXp()
        ));
        yourSpec.setStyle(GUIStyles.LABEL);

        Label opponentSpec = new Label("Especialización rival: " + getSpecialization(
                data.getOpponentNetworkXp(),
                data.getOpponentMalwareXp(),
                data.getOpponentCryptoXp()
        ));
        opponentSpec.setStyle(GUIStyles.LABEL);

        Button backButton = new Button("Jugar otra vez");
        backButton.setStyle(GUIStyles.BUTTON);
        backButton.setOnAction(e -> guiManager.showAvatarScene());

        VBox root = new VBox(
                16,
                title,
                mapLabel,
                yourScore,
                opponentScore,
                yourSpec,
                opponentSpec,
                backButton
        );
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(GUIStyles.ROOT);

        return new Scene(root);
    }

    private String getSpecialization(int networkXp, int malwareXp, int cryptoXp) {
        if (networkXp >= malwareXp && networkXp >= cryptoXp) {
            return "Network / DDoS Defense";
        } else if (malwareXp >= networkXp && malwareXp >= cryptoXp) {
            return "Malware Defense";
        } else {
            return "Crypto / Credential Defense";
        }
    }
}