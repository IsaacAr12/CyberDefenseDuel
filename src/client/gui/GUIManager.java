package client.gui;

import client.ClientController;
import client.PlayerSetupData;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUIManager implements ClientController.GUIBridge {

    private final Stage stage;
    private final PlayerSetupData setupData;
    private final ClientController controller;

    private Label loginStatusLabel;
    private Label matchmakingStatusLabel;
    private GameScene currentGameScene;

    public GUIManager(Stage stage) {
        this.stage = stage;
        this.setupData = new PlayerSetupData();
        this.controller = new ClientController(this);
    }

    public PlayerSetupData getSetupData() {
        return setupData;
    }

    public ClientController getController() {
        return controller;
    }

    public void showLoginScene() {
        LoginScene scene = new LoginScene(this, controller);
        stage.setScene(scene.createScene());
    }

    public void showAvatarScene() {
        AvatarScene scene = new AvatarScene(this);
        stage.setScene(scene.createScene());
    }

    public void showMatchmakingScene() {
        MatchmakingScene scene = new MatchmakingScene(this, controller);
        stage.setScene(scene.createScene());
    }

    public void showMapScene() {
        MapScene scene = new MapScene(this, controller);
        stage.setScene(scene.createScene());
    }

    public void showGameScene(String mapName) {
        currentGameScene = new GameScene(this, controller, mapName);
        stage.setScene(currentGameScene.createScene());
    }

    public void showFinalResultScene() {
        FinalResultScene scene = new FinalResultScene(this);
        stage.setScene(scene.createScene());
    }

    public void setLoginStatusLabel(Label loginStatusLabel) {
        this.loginStatusLabel = loginStatusLabel;
    }

    public void setMatchmakingStatusLabel(Label matchmakingStatusLabel) {
        this.matchmakingStatusLabel = matchmakingStatusLabel;
    }

    @Override
    public void onLoginSuccess() {
        if (loginStatusLabel != null) {
            loginStatusLabel.setStyle(GUIStyles.SUCCESS);
            loginStatusLabel.setText("Login correcto.");
        }
        showAvatarScene();
    }

    @Override
    public void onLoginFail() {
        if (loginStatusLabel != null) {
            loginStatusLabel.setStyle(GUIStyles.ERROR);
            loginStatusLabel.setText("Credenciales inválidas o usuario existente.");
        }
    }

    @Override
    public void showWaitingStatus() {
        if (matchmakingStatusLabel != null) {
            matchmakingStatusLabel.setText("Esperando a otro jugador...");
        }
    }

    @Override
    public void onMatchFound() {
        if (matchmakingStatusLabel != null) {
            matchmakingStatusLabel.setText("¡Jugador encontrado!");
        }
        showMapScene();
    }

    @Override
    public void onMapSelected(String mapName) {
        setupData.setFinalMap(mapName);
        showGameScene(mapName);
    }

    @Override
    public void onOpponentState(String payload) {
        if (currentGameScene != null) {
            currentGameScene.updateOpponentState(payload);
        }
    }

    @Override
    public void onOpponentGameOver(String payload) {
        if (currentGameScene != null) {
            currentGameScene.handleOpponentGameOver(payload);
        }
    }

    @Override
    public void onSessionFinished() {
        showFinalResultScene();
    }
}