package client;

import client.gui.GUIManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GUIManager guiManager = new GUIManager(primaryStage);
        guiManager.showLoginScene();

        primaryStage.setTitle("Cyber Defense Duel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}