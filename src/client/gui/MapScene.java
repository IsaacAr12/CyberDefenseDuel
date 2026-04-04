package client.gui;

import client.ClientController;
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

public class MapScene {

    private final GUIManager guiManager;
    private final ClientController controller;

    private MapCard selectedCard;
    private Label statusLabel;

    public MapScene(GUIManager guiManager, ClientController controller) {
        this.guiManager = guiManager;
        this.controller = controller;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #14a3dc;");

        Label title = new Label("SELECCIONA TU MAPA");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #a64ac9;" +
                "-fx-padding: 12 28 12 28;"
        );

        VBox topBox = new VBox(14, title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(25, 0, 10, 0));
        root.setTop(topBox);

        FlowPane cardsPane = new FlowPane();
        cardsPane.setHgap(40);
        cardsPane.setVgap(30);
        cardsPane.setPadding(new Insets(25));
        cardsPane.setAlignment(Pos.TOP_CENTER);

        MapCard map1 = createCard("Habitacion de Programador", 3, "/images/Habitacion de Programador.png");
        MapCard map2 = createCard("Oficina de Trabajo", 5, "/images/Oficina de Trabajo.png");
        MapCard map3 = createCard("Cueva de Hacker", 7, "/images/Cueva de Hacker.png");

        cardsPane.getChildren().addAll(
                map1.container,
                map2.container,
                map3.container
        );

        root.setCenter(cardsPane);

        statusLabel = new Label("Escoge un mapa para continuar");
        statusLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Button confirmButton = createPurpleButton("CONFIRMAR MAPA");
        Button backButton = createPurpleButton("VOLVER");

        confirmButton.setOnAction(e -> {
            if (selectedCard == null) {
                statusLabel.setText("Debes escoger un mapa.");
                return;
            }

            String mapName = selectedCard.mapName;
            guiManager.getSetupData().setSelectedMap(mapName);
            statusLabel.setText("Esperando decisión final del servidor...");
            controller.sendMapChoice(mapName);
        });

        backButton.setOnAction(e -> guiManager.showAvatarScene());

        VBox bottomBox = new VBox(14, statusLabel, confirmButton, backButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 0, 20, 0));
        root.setBottom(bottomBox);

        return new Scene(root);
    }

    private MapCard createCard(String mapName, int laneCount, String imagePath) {
        Image image = loadImage(imagePath);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(360);
        imageView.setFitHeight(215);
        imageView.setPreserveRatio(false);

        if (image != null) {
            imageView.setImage(image);
        }

        Rectangle selectionBorder = new Rectangle(372, 227);
        selectionBorder.setArcWidth(18);
        selectionBorder.setArcHeight(18);
        selectionBorder.setFill(Color.TRANSPARENT);
        selectionBorder.setStroke(Color.TRANSPARENT);
        selectionBorder.setStrokeWidth(6);

        StackPane imageBox = new StackPane(selectionBorder, imageView);
        imageBox.setMinSize(372, 227);
        imageBox.setMaxSize(372, 227);
        imageBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-padding: 4;"
        );

        Label nameLabel = new Label(mapName);
        nameLabel.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-background-radius: 22;"
        );

        Label laneLabel = new Label("Carriles: " + laneCount);
        laneLabel.setStyle(
                "-fx-background-color: #ffffff;" +
                "-fx-text-fill: #222222;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 6 16 6 16;" +
                "-fx-background-radius: 18;"
        );

        VBox container = new VBox(12, imageBox, nameLabel, laneLabel);
        container.setAlignment(Pos.CENTER);

        MapCard card = new MapCard(mapName, laneCount, container, selectionBorder);
        container.setOnMouseClicked(e -> selectCard(card));

        return card;
    }

    private void selectCard(MapCard card) {
        if (selectedCard != null) {
            selectedCard.selectionBorder.setStroke(Color.TRANSPARENT);
        }

        selectedCard = card;
        selectedCard.selectionBorder.setStroke(Color.web("#ffe600"));
        statusLabel.setText("Seleccionado: " + selectedCard.mapName + " (" + selectedCard.laneCount + " carriles)");
    }

    private Button createPurpleButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #a64ac9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 16;" +
                "-fx-padding: 10 24 10 24;"
        );
        return button;
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

    private static class MapCard {
        String mapName;
        int laneCount;
        VBox container;
        Rectangle selectionBorder;

        MapCard(String mapName, int laneCount, VBox container, Rectangle selectionBorder) {
            this.mapName = mapName;
            this.laneCount = laneCount;
            this.container = container;
            this.selectionBorder = selectionBorder;
        }
    }
}