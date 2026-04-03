package client.gui;

import client.ClientController;
import client.PlayerSetupData;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
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

        this.stage.setFullScreenExitHint("");
        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.stage.setMaximized(true);
    }

    public PlayerSetupData getSetupData() {
        return setupData;
    }

    public ClientController getController() {
        return controller;
    }

    private void applyScene(Scene scene) {
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
    }

    public void showLoginScene() {
        applyScene(new LoginScene(this, controller).createScene());
    }

    public void showAvatarScene() {
        applyScene(new AvatarScene(this).createScene());
    }

    public void showMatchmakingScene() {
        applyScene(new MatchmakingScene(this, controller).createScene());
    }

    public void showMapScene() {
        applyScene(new MapScene(this, controller).createScene());
    }

    public void showGameScene(String mapName) {
        currentGameScene = new GameScene(this, controller, mapName);
        applyScene(currentGameScene.createScene());
    }

    public void showFinalResultScene() {
        applyScene(new FinalResultScene(this).createScene());
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