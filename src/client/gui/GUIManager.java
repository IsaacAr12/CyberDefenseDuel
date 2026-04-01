package client.gui;

import client.PlayerSetupData;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {

    private final Stage stage;
    private final PlayerSetupData setupData;

    public GUIManager(Stage stage) {
        this.stage = stage;
        this.setupData = new PlayerSetupData();
    }

    public PlayerSetupData getSetupData() {
        return setupData;
    }

    public void showLoginScene() {
        LoginScene loginScene = new LoginScene(this);
        stage.setScene(loginScene.createScene());
    }

    public void showAvatarScene() {
        AvatarScene avatarScene = new AvatarScene(this);
        stage.setScene(avatarScene.createScene());
    }

    public void showMatchmakingScene() {
        MatchmakingScene matchmakingScene = new MatchmakingScene(this);
        stage.setScene(matchmakingScene.createScene());
    }

    public void showMapScene() {
        MapScene mapScene = new MapScene(this);
        stage.setScene(mapScene.createScene());
    }

    public void showFinalMapMessage() {
        FinalMapScene finalMapScene = new FinalMapScene(this);
        stage.setScene(finalMapScene.createScene());
    }
}