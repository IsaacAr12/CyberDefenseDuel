package client.gui;

import client.ClientController;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class GameScene {

    private final GUIManager guiManager;
    private final ClientController controller;
    private final String mapName;

    private final double width = 900;
    private final double height = 600;
    private final double[] laneX = {200, 450, 700};

    private static final int MAX_ATTACKS = 256;

    private int selectedLane = 1;
    private int hp = 100;
    private int score = 0;
    private int level = 0;

    private int opponentHp = 100;
    private int opponentScore = 0;
    private int opponentLevel = 0;

    private final FallingAttack[] attacks = new FallingAttack[MAX_ATTACKS];
    private int attackCount = 0;

    private final Random random = new Random();
    private double spawnTimer = 0;

    private boolean localGameOver = false;
    private boolean opponentGameOver = false;

    private Label hpLabel;
    private Label scoreLabel;
    private Label laneLabel;
    private Label levelLabel;
    private Label opponentLabel;
    private Label centerStatusLabel;

    public GameScene(GUIManager guiManager, ClientController controller, String mapName) {
        this.guiManager = guiManager;
        this.controller = controller;
        this.mapName = mapName;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f172a;");

        hpLabel = new Label("HP: 100");
        hpLabel.setStyle(GUIStyles.LABEL);

        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle(GUIStyles.LABEL);

        laneLabel = new Label("Carril: 2");
        laneLabel.setStyle(GUIStyles.LABEL);

        levelLabel = new Label("Nivel: 0");
        levelLabel.setStyle(GUIStyles.LABEL);

        Label mapLabel = new Label("Mapa: " + mapName);
        mapLabel.setStyle(GUIStyles.LABEL);

        opponentLabel = new Label("Rival HP: 100 | Rival Score: 0 | Rival Nivel: 0");
        opponentLabel.setStyle(GUIStyles.LABEL);

        HBox top = new HBox(18, hpLabel, scoreLabel, laneLabel, levelLabel, mapLabel, opponentLabel);
        top.setAlignment(Pos.CENTER);
        top.setStyle("-fx-padding: 12;");
        root.setTop(top);

        Canvas canvas = new Canvas(width, height - 100);

        centerStatusLabel = new Label("");
        centerStatusLabel.setStyle(GUIStyles.TITLE);

        VBoxCenterPane centerPane = new VBoxCenterPane(canvas, centerStatusLabel);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, width, height);

        scene.setOnKeyPressed(e -> {
            if (localGameOver) {
                return;
            }

            if (e.getCode() == KeyCode.LEFT) {
                selectedLane = Math.max(0, selectedLane - 1);
                laneLabel.setText("Carril: " + (selectedLane + 1));
            } else if (e.getCode() == KeyCode.RIGHT) {
                selectedLane = Math.min(2, selectedLane + 1);
                laneLabel.setText("Carril: " + (selectedLane + 1));
            } else if (e.getCode() == KeyCode.Q) {
                defend("DDOS");
            } else if (e.getCode() == KeyCode.W) {
                defend("MALWARE");
            } else if (e.getCode() == KeyCode.E) {
                defend("CRED");
            }
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                double delta = (now - last) / 1_000_000_000.0;
                last = now;

                if (!localGameOver) {
                    update(delta);
                    controller.sendGameState(hp, score, level);

                    if (hp <= 0) {
                        localGameOver = true;
                        centerStatusLabel.setText("Waiting for opponent...");
                        controller.sendGameOver(hp, score, level);
                    }
                }

                render(gc);

                hpLabel.setText("HP: " + hp);
                scoreLabel.setText("Score: " + score);
                levelLabel.setText("Nivel: " + level);
                opponentLabel.setText("Rival HP: " + opponentHp + " | Rival Score: " + opponentScore + " | Rival Nivel: " + opponentLevel);
            }
        }.start();

        return scene;
    }

    private void update(double delta) {
        level = score / 100;

        spawnTimer += delta;
        double spawnInterval = Math.max(0.25, 1.0 - (level * 0.05));

        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            spawnAttack();
        }

        int i = 0;
        while (i < attackCount) {
            FallingAttack atk = attacks[i];
            atk.y += (atk.speed + level * 10) * delta;

            if (atk.y >= 500) {
                hp -= atk.damage;
                removeAttackAt(i);
            } else {
                i++;
            }
        }
    }

    private void defend(String expectedType) {
        int targetIndex = -1;

        for (int i = 0; i < attackCount; i++) {
            FallingAttack atk = attacks[i];
            if (atk.lane == selectedLane && atk.y >= 380 && atk.y <= 520) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            return;
        }

        FallingAttack target = attacks[targetIndex];

        if (target.type.equals(expectedType)) {
            score += 10;
        } else {
            hp -= target.damage;
        }

        removeAttackAt(targetIndex);
    }

    private void spawnAttack() {
        if (attackCount >= MAX_ATTACKS) {
            return;
        }

        int lane = random.nextInt(3);
        int typeIndex = random.nextInt(3);

        String type;
        int damage;

        if (typeIndex == 0) {
            type = "DDOS";
            damage = 5;
        } else if (typeIndex == 1) {
            type = "MALWARE";
            damage = 8;
        } else {
            type = "CRED";
            damage = 10;
        }

        attacks[attackCount] = new FallingAttack(lane, type, damage, 160);
        attackCount++;
    }

    private void removeAttackAt(int index) {
        if (index < 0 || index >= attackCount) {
            return;
        }

        for (int i = index; i < attackCount - 1; i++) {
            attacks[i] = attacks[i + 1];
        }

        attacks[attackCount - 1] = null;
        attackCount--;
    }

    private void render(GraphicsContext gc) {
        gc.setFill(Color.web("#111827"));
        gc.fillRect(0, 0, width, height);

        if ("Habitacion de Programador".equalsIgnoreCase(mapName)) {
            gc.setFill(Color.web("#1f2937"));
            gc.fillRect(50, 40, 180, 90);
            gc.fillRect(670, 40, 180, 90);

            gc.setFill(Color.web("#0b1220"));
            gc.fillRect(90, 150, 720, 370);
        } else {
            gc.setFill(Color.web("#22303c"));
            gc.fillRect(80, 100, 740, 420);
        }

        for (int i = 0; i < 3; i++) {
            gc.setStroke(i == selectedLane ? Color.YELLOW : Color.WHITE);
            gc.setLineWidth(i == selectedLane ? 5 : 2);
            gc.strokeRect(laneX[i] - 60, 60, 120, 460);
        }

        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(laneX[selectedLane] - 40, 520, 80, 20);

        for (int i = 0; i < attackCount; i++) {
            FallingAttack atk = attacks[i];

            if ("DDOS".equals(atk.type)) {
                gc.setFill(Color.RED);
            } else if ("MALWARE".equals(atk.type)) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.CYAN);
            }

            gc.fillOval(laneX[atk.lane] - 20, atk.y, 40, 40);
            gc.setFill(Color.WHITE);
            gc.fillText(atk.type, laneX[atk.lane] - 22, atk.y - 5);
        }
    }

    public void updateOpponentState(String payload) {
        try {
            JsonObject obj = JsonParser.parseString(payload).getAsJsonObject();
            opponentHp = obj.get("hp").getAsInt();
            opponentScore = obj.get("score").getAsInt();
            opponentLevel = obj.get("level").getAsInt();
        } catch (Exception ignored) {
        }
    }

    public void handleOpponentGameOver(String payload) {
        opponentHp = 0;
        opponentGameOver = true;

        if (!localGameOver) {
            centerStatusLabel.setText("You win!");
        }
    }

    private static class FallingAttack {
        int lane;
        String type;
        int damage;
        double speed;
        double y;

        FallingAttack(int lane, String type, int damage, double speed) {
            this.lane = lane;
            this.type = type;
            this.damage = damage;
            this.speed = speed;
            this.y = 70;
        }
    }

    private static class VBoxCenterPane extends javafx.scene.layout.VBox {
        VBoxCenterPane(Canvas canvas, Label label) {
            super(12, canvas, label);
            setAlignment(Pos.CENTER);
        }
    }
}