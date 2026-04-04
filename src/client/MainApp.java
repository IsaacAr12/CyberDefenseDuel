package client;

import client.gui.GUIManager;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GUIManager guiManager = new GUIManager(primaryStage);

        primaryStage.setTitle("Cyber Defense Duel");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setMaximized(true);

        guiManager.showLoginScene();

        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}