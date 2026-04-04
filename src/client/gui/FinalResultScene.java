package client.gui;

import client.PlayerSetupData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FinalResultScene {

    private final GUIManager guiManager;

    public FinalResultScene(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public Scene createScene() {
        PlayerSetupData data = guiManager.getSetupData();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #14a3dc;");

        Label title = new Label("RESUMEN DE LA PARTIDA");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #a64ac9;" +
                "-fx-padding: 12 30 12 30;" +
                "-fx-background-radius: 14;"
        );

        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(25, 0, 15, 0));
        root.setTop(topBox);

        VBox playerPanel = createResultPanel(
                "TU RESULTADO",
                "Puntaje: " + data.getFinalScore(),
                "Nivel: " + data.getFinalLevel(),
                "HP final: " + data.getFinalHp(),
                "Especialización: " + getSpecialization(
                        data.getNetworkXp(),
                        data.getMalwareXp(),
                        data.getCryptoXp()
                ),
                "XP Network: " + data.getNetworkXp(),
                "XP Malware: " + data.getMalwareXp(),
                "XP Crypto: " + data.getCryptoXp()
        );

        VBox rivalPanel = createResultPanel(
                "RIVAL",
                "Puntaje: " + data.getOpponentFinalScore(),
                "Nivel: " + data.getOpponentFinalLevel(),
                "HP final: " + data.getOpponentFinalHp(),
                "Especialización: " + getSpecialization(
                        data.getOpponentNetworkXp(),
                        data.getOpponentMalwareXp(),
                        data.getOpponentCryptoXp()
                ),
                "XP Network: " + data.getOpponentNetworkXp(),
                "XP Malware: " + data.getOpponentMalwareXp(),
                "XP Crypto: " + data.getOpponentCryptoXp()
        );

        HBox centerBox = new HBox(40, playerPanel, rivalPanel);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20, 30, 20, 30));
        root.setCenter(centerBox);

        Label mapLabel = new Label("Mapa jugado: " + data.getFinalMap());
        mapLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Button againButton = new Button("JUGAR OTRA VEZ");
        againButton.setStyle(
                "-fx-background-color: #a64ac9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 16;" +
                "-fx-padding: 10 24 10 24;"
        );
        againButton.setOnAction(e -> guiManager.showAvatarScene());

        VBox bottomBox = new VBox(14, mapLabel, againButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 0, 25, 0));
        root.setBottom(bottomBox);

        return new Scene(root);
    }

    private VBox createResultPanel(
            String titleText,
            String scoreText,
            String levelText,
            String hpText,
            String specText,
            String networkText,
            String malwareText,
            String cryptoText
    ) {
        Label title = new Label(titleText);
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #a64ac9;" +
                "-fx-padding: 10 18 10 18;" +
                "-fx-background-radius: 14;"
        );

        Label score = createInfoLabel(scoreText);
        Label level = createInfoLabel(levelText);
        Label hp = createInfoLabel(hpText);
        Label spec = createInfoLabel(specText);
        Label network = createInfoLabel(networkText);
        Label malware = createInfoLabel(malwareText);
        Label crypto = createInfoLabel(cryptoText);

        VBox panel = new VBox(
                14,
                title,
                score,
                level,
                hp,
                spec,
                network,
                malware,
                crypto
        );
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(26));
        panel.setPrefWidth(390);
        panel.setStyle(
                "-fx-background-color: rgba(255,255,255,0.18);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;"
        );

        return panel;
    }

    private Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(320);
        label.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 18 10 18;" +
                "-fx-background-radius: 18;"
        );
        return label;
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