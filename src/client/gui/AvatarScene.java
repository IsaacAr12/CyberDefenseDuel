package client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;

public class AvatarScene {

    private final GUIManager guiManager;

    private AvatarCard selectedCard;
    private Label statusLabel;

    public AvatarScene(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #14a3dc;");

        Label title = new Label("SELECCIONA A TU PERSONAJE");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #a64ac9;" +
                "-fx-padding: 12 24 12 24;"
        );

        StackPane topPane = new StackPane(title);
        topPane.setPadding(new Insets(20, 0, 10, 0));
        root.setTop(topPane);

        FlowPane cardsPane = new FlowPane();
        cardsPane.setHgap(50);
        cardsPane.setVgap(35);
        cardsPane.setPadding(new Insets(20, 20, 20, 20));
        cardsPane.setAlignment(Pos.TOP_CENTER);

        AvatarCard firewall = createCard("Capitan Firewall", "/images/personajes/Capitan Firewall marco.png");
        AvatarCard ninja = createCard("Byte Ninja", "/images/personajes/Byte Ninja marco.png");
        AvatarCard pirate = createCard("Packet Pirate", "/images/personajes/Packet Pirate marco.png");
        AvatarCard paladin = createCard("Null Pointer Paladin", "/images/personajes/Null Pointer Paladin marco.png");
        AvatarCard muncher = createCard("Malware Muncher", "/images/personajes/Malware Muncher marco.png");

        cardsPane.getChildren().addAll(
                firewall.container,
                ninja.container,
                pirate.container,
                paladin.container,
                muncher.container
        );

        root.setCenter(cardsPane);

        statusLabel = new Label("Jugador: " + guiManager.getSetupData().getUsername());
        statusLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Button continueButton = new Button("CONTINUAR");
        continueButton.setStyle(
                "-fx-background-color: #a64ac9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 22 10 22;"
        );

        continueButton.setOnAction(e -> {
            if (selectedCard == null) {
                statusLabel.setText("Debes escoger un personaje.");
                return;
            }

            String avatar = selectedCard.avatarName;
            guiManager.getSetupData().setSelectedAvatar(avatar);
            guiManager.getController().sendAvatarSelection(avatar);
            guiManager.showMatchmakingScene();
        });

        VBox bottomBox = new VBox(12, statusLabel, continueButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 0, 20, 0));
        root.setBottom(bottomBox);

        return new Scene(root);
    }

    private AvatarCard createCard(String avatarName, String imagePath) {
        Image image = loadImage(imagePath);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(265);
        imageView.setFitHeight(265);
        imageView.setPreserveRatio(true);

        if (image != null) {
            imageView.setImage(image);
        }

        Rectangle selectionBorder = new Rectangle(280, 280);
        selectionBorder.setArcWidth(18);
        selectionBorder.setArcHeight(18);
        selectionBorder.setFill(Color.TRANSPARENT);
        selectionBorder.setStroke(Color.TRANSPARENT);
        selectionBorder.setStrokeWidth(6);

        StackPane imageBox = new StackPane(selectionBorder, imageView);
        imageBox.setMinSize(280, 280);
        imageBox.setMaxSize(280, 280);
        imageBox.setStyle("-fx-background-color: transparent;");

        Label nameLabel = new Label(avatarName);
        nameLabel.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-background-radius: 22;"
        );

        VBox container = new VBox(12, imageBox, nameLabel);
        container.setAlignment(Pos.CENTER);

        AvatarCard card = new AvatarCard(avatarName, container, selectionBorder);

        container.setOnMouseClicked(e -> selectCard(card));

        return card;
    }

    private void selectCard(AvatarCard card) {
        if (selectedCard != null) {
            selectedCard.selectionBorder.setStroke(Color.TRANSPARENT);
        }

        selectedCard = card;
        selectedCard.selectionBorder.setStroke(Color.web("#ffe600"));
        statusLabel.setText("Seleccionado: " + selectedCard.avatarName);
    }

    private Image loadImage(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.out.println("No se encontró la imagen: " + resourcePath);
                return null;
            }
            return new Image(is);
        } catch (Exception e) {
            System.out.println("Error cargando imagen " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }

    private static class AvatarCard {
        String avatarName;
        VBox container;
        Rectangle selectionBorder;

        AvatarCard(String avatarName, VBox container, Rectangle selectionBorder) {
            this.avatarName = avatarName;
            this.container = container;
            this.selectionBorder = selectionBorder;
        }
    }
}