package client.gui;

import client.audio.SoundManager;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        SoundManager.playMusic("/sounds/MENU.mp3", 0.45);

        BorderPane root = new BorderPane();
        setBackgroundImage(root, "/images/Avatares.png");

        Label title = new Label("SELECCIONA A TU PERSONAJE");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(166,74,201,0.92);" +
                "-fx-padding: 12 24 12 24;"
        );

        StackPane topPane = new StackPane(title);
        topPane.setPadding(new Insets(20, 0, 10, 0));
        root.setTop(topPane);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(30);

        AvatarCard firewall = createCard("Capitan Firewall", "/images/personajes/Capitan Firewall marco.png");
        AvatarCard ninja = createCard("Byte Ninja", "/images/personajes/Byte Ninja marco.png");
        AvatarCard pirate = createCard("Packet Pirate", "/images/personajes/Packet Pirate marco.png");
        AvatarCard paladin = createCard("Null Pointer Paladin", "/images/personajes/Null Pointer Paladin marco.png");
        AvatarCard muncher = createCard("Malware Muncher", "/images/personajes/Malware Muncher marco.png");

        grid.add(firewall.container, 0, 0);
        grid.add(ninja.container, 1, 0);
        grid.add(pirate.container, 2, 0);
        grid.add(paladin.container, 3, 0);
        grid.add(muncher.container, 1, 1, 2, 1);

        root.setCenter(grid);

        statusLabel = new Label("Jugador: " + guiManager.getSetupData().getUsername());
        statusLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(0,0,0,0.35);" +
                "-fx-padding: 8 16 8 16;" +
                "-fx-background-radius: 14;"
        );

        Button continueButton = new Button("CONTINUAR");
        continueButton.setStyle(
                "-fx-background-color: rgba(166,74,201,0.95);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 22 10 22;" +
                "-fx-background-radius: 16;"
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

        Scene scene = new Scene(root);

        DoubleBinding sizeBinding = scene.widthProperty().divide(6.0);
        firewall.bindSize(sizeBinding);
        ninja.bindSize(sizeBinding);
        pirate.bindSize(sizeBinding);
        paladin.bindSize(sizeBinding);
        muncher.bindSize(sizeBinding);

        return scene;
    }

    private void setBackgroundImage(Region region, String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                region.setStyle("-fx-background-color: black;");
                return;
            }

            BackgroundSize size = new BackgroundSize(
                    100, 100,
                    true, true,
                    true, true
            );

            BackgroundImage bg = new BackgroundImage(
                    new Image(url.toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    size
            );

            region.setBackground(new Background(bg));
        } catch (Exception e) {
            region.setStyle("-fx-background-color: black;");
        }
    }

    private AvatarCard createCard(String avatarName, String imagePath) {
        Image image = loadImage(imagePath);

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);

        Rectangle border = new Rectangle();
        border.setArcWidth(20);
        border.setArcHeight(20);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        border.setStrokeWidth(5);

        StackPane imageBox = new StackPane(border, imageView);

        Label nameLabel = new Label(avatarName);
        nameLabel.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 18 8 18;" +
                "-fx-background-radius: 20;"
        );

        VBox container = new VBox(10, imageBox, nameLabel);
        container.setAlignment(Pos.CENTER);

        AvatarCard card = new AvatarCard(avatarName, container, imageView, border);
        container.setOnMouseClicked(e -> selectCard(card));

        if (image != null) {
            imageView.setImage(image);
        }

        return card;
    }

    private void selectCard(AvatarCard card) {
        if (selectedCard != null) {
            selectedCard.border.setStroke(Color.TRANSPARENT);
        }

        selectedCard = card;
        selectedCard.border.setStroke(Color.YELLOW);
        statusLabel.setText("Seleccionado: " + selectedCard.avatarName);
    }

    private Image loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            return is != null ? new Image(is) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static class AvatarCard {
        String avatarName;
        VBox container;
        ImageView imageView;
        Rectangle border;

        AvatarCard(String avatarName, VBox container, ImageView imageView, Rectangle border) {
            this.avatarName = avatarName;
            this.container = container;
            this.imageView = imageView;
            this.border = border;
        }

        void bindSize(DoubleBinding size) {
            imageView.fitWidthProperty().bind(size);
            imageView.fitHeightProperty().bind(size);
            border.widthProperty().bind(size.add(10));
            border.heightProperty().bind(size.add(10));
        }
    }
}