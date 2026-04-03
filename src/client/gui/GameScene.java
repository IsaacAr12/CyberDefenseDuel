package client.gui;

import client.ClientController;
import client.PlayerSetupData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.Config;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.Random;

public class GameScene {

    private final GUIManager guiManager;
    private final ClientController controller;
    private final String mapName;
    private final Config config;

    private double width = 1280;
    private double height = 720;

    private int laneCount;
    private double[] laneX;

    private static final int MAX_ATTACKS = 256;

    private int selectedLane;
    private int hp;
    private int score = 0;
    private int level = 0;

    private int opponentHp = 100;
    private int opponentScore = 0;
    private int opponentLevel = 0;

    private int networkXp = 0;
    private int malwareXp = 0;
    private int cryptoXp = 0;

    private int opponentNetworkXp = 0;
    private int opponentMalwareXp = 0;
    private int opponentCryptoXp = 0;

    private final FallingAttack[] attacks = new FallingAttack[MAX_ATTACKS];
    private int attackCount = 0;

    private final Random random = new Random();
    private double spawnTimer = 0;

    private boolean localGameOver = false;
    private boolean opponentGameOver = false;

    private Label hpLabel;
    private Label scoreLabel;
    private Label levelLabel;
    private Label opponentLabel;
    private Label mapLabel;
    private Label centerStatusLabel;

    private Image mapHabitacion;
    private Image mapOficina;
    private Image mapCueva;

    private Image ddosImage;
    private Image malwareImage;
    private Image credImage;

    private Image capitanPjImage;
    private Image ninjaPjImage;
    private Image piratePjImage;
    private Image paladinPjImage;
    private Image muncherPjImage;

    public GameScene(GUIManager guiManager, ClientController controller, String mapName) {
        this.guiManager = guiManager;
        this.controller = controller;
        this.mapName = mapName;

        Config receivedConfig = guiManager.getSetupData().getGameConfig();
        this.config = (receivedConfig != null) ? receivedConfig : new Config();

        this.hp = this.config.getInitialHp();

        this.laneCount = resolveLaneCount(mapName);
        this.laneX = new double[laneCount];
        this.selectedLane = laneCount / 2;

        loadImages();
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f172a;");

        hpLabel = createHudLabel("HP: " + hp);
        scoreLabel = createHudLabel("Score: 0");
        levelLabel = createHudLabel("Nivel: 0");
        mapLabel = createHudLabel("Mapa: " + mapName);
        opponentLabel = createHudLabel("Rival HP: 100 | Rival Score: 0 | Rival Nivel: 0");

        HBox top = new HBox(14, hpLabel, scoreLabel, levelLabel, mapLabel, opponentLabel);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(12));
        top.setStyle(
                "-fx-background-color: rgba(10,20,35,0.72);" +
                "-fx-border-color: white;" +
                "-fx-border-width: 0 0 2 0;"
        );
        root.setTop(top);

        Canvas canvas = new Canvas(width, height - 90);

        centerStatusLabel = new Label("");
        centerStatusLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: rgba(166,74,201,0.92);" +
                "-fx-padding: 18 34 18 34;" +
                "-fx-background-radius: 18;"
        );
        centerStatusLabel.setVisible(false);
        centerStatusLabel.setManaged(false);

        StackPane centerPane = new StackPane(canvas, centerStatusLabel);
        StackPane.setAlignment(centerStatusLabel, Pos.CENTER);
        root.setCenter(centerPane);

        Scene scene = new Scene(root);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            width = newVal.doubleValue();
            canvas.setWidth(width);
            updateLanePositions();
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            height = newVal.doubleValue();
            canvas.setHeight(height - 90);
        });

        updateLanePositions();

        scene.setOnKeyPressed(e -> {
            if (localGameOver) {
                return;
            }

            if (e.getCode() == KeyCode.LEFT) {
                selectedLane = Math.max(0, selectedLane - 1);
            } else if (e.getCode() == KeyCode.RIGHT) {
                selectedLane = Math.min(laneCount - 1, selectedLane + 1);
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
                        centerStatusLabel.setVisible(true);
                        saveLocalFinalData();
                        controller.sendGameOver(hp, score, level, networkXp, malwareXp, cryptoXp);
                    }
                }

                render(gc);

                hpLabel.setText("HP: " + hp);
                scoreLabel.setText("Score: " + score);
                levelLabel.setText("Nivel: " + level);
                opponentLabel.setText(
                        "Rival HP: " + opponentHp +
                        " | Rival Score: " + opponentScore +
                        " | Rival Nivel: " + opponentLevel
                );
            }
        }.start();

        return scene;
    }

    private Label createHudLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-background-color: rgba(255,255,255,0.90);" +
                "-fx-text-fill: #111111;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 14 8 14;" +
                "-fx-background-radius: 16;"
        );
        return label;
    }

    private int resolveLaneCount(String mapName) {
        if ("Oficina de Trabajo".equalsIgnoreCase(mapName)) {
            return 5;
        }
        if ("Cueva de Hacker".equalsIgnoreCase(mapName)) {
            return 7;
        }
        return 3;
    }

    private void loadImages() {
        mapHabitacion = loadImage("/images/Habitacion de Programador.png");
        mapOficina = loadImage("/images/Oficina de Trabajo.png");
        mapCueva = loadImage("/images/Cueva de Hacker.png");

        ddosImage = loadImage("/images/DDOD.png");
        malwareImage = loadImage("/images/MALWARE.png");
        credImage = loadImage("/images/CRED.png");

        capitanPjImage = loadImage("/images/personajes/Capitan_Firewall_pj.png");
        ninjaPjImage = loadImage("/images/personajes/Byte_Ninja_marco_pj.png");
        piratePjImage = loadImage("/images/personajes/Packet_Pirate_pj.png");
        paladinPjImage = loadImage("/images/personajes/Null_Pointer_Paladin_pj.png");
        muncherPjImage = loadImage("/images/personajes/Malware_Muncher_pj.png");
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

    private void updateLanePositions() {
        laneX = new double[laneCount];

        double leftMargin = width * 0.12;
        double rightMargin = width * 0.88;
        double usableWidth = rightMargin - leftMargin;

        if (laneCount == 1) {
            laneX[0] = width / 2.0;
            return;
        }

        for (int i = 0; i < laneCount; i++) {
            laneX[i] = leftMargin + (usableWidth * i / (laneCount - 1));
        }
    }

    private void saveLocalFinalData() {
        PlayerSetupData data = guiManager.getSetupData();
        data.setFinalHp(hp);
        data.setFinalScore(score);
        data.setFinalLevel(level);
        data.setNetworkXp(networkXp);
        data.setMalwareXp(malwareXp);
        data.setCryptoXp(cryptoXp);
    }

    private void update(double delta) {
        if (config.getDifficultyStepScore() > 0) {
            level = score / config.getDifficultyStepScore();
        } else {
            level = 0;
        }

        spawnTimer += delta;

        double spawnRate = config.getBaseSpawnRate() *
                Math.pow(config.getSpawnMultiplierPerLevel(), level);

        double spawnInterval = 1.0 / Math.max(0.1, spawnRate);

        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            spawnAttack();
        }

        int i = 0;
        while (i < attackCount) {
            FallingAttack atk = attacks[i];
            atk.y += atk.speed * delta;

            if (atk.y >= height - 135) {
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
            if (atk.lane == selectedLane && atk.y >= height - 260 && atk.y <= height - 110) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            return;
        }

        FallingAttack target = attacks[targetIndex];

        if (target.type.equals(expectedType)) {
            score += config.getScorePerKill();

            if ("DDOS".equals(target.type)) {
                networkXp += config.getScorePerKill();
            } else if ("MALWARE".equals(target.type)) {
                malwareXp += config.getScorePerKill();
            } else if ("CRED".equals(target.type)) {
                cryptoXp += config.getScorePerKill();
            }
        } else {
            hp -= target.damage;
        }

        removeAttackAt(targetIndex);
    }

    private void spawnAttack() {
        if (attackCount >= MAX_ATTACKS) {
            return;
        }

        int lane = random.nextInt(laneCount);
        int typeIndex = random.nextInt(3);

        String type;
        int damage;

        if (typeIndex == 0) {
            type = "DDOS";
            damage = config.getDdosDamage();
        } else if (typeIndex == 1) {
            type = "MALWARE";
            damage = config.getMalwareDamage();
        } else {
            type = "CRED";
            damage = config.getCredDamage();
        }

        double speed = config.getBaseAttackSpeed() + (config.getSpeedAddPerLevel() * level);

        attacks[attackCount] = new FallingAttack(lane, type, damage, speed);
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
        gc.setFill(Color.web("#08142b"));
        gc.fillRect(0, 0, width, height);

        drawBackground(gc);

        double playTop = 22;
        double playBottom = height - 18;
        double playHeight = playBottom - playTop;

        double laneWidth;
        if (laneCount >= 7) {
            laneWidth = width * 0.085;
        } else if (laneCount >= 5) {
            laneWidth = width * 0.11;
        } else {
            laneWidth = width * 0.16;
        }

        double laneHeight = playHeight - 36;
        double laneTop = playTop + 10;

        for (int i = 0; i < laneCount; i++) {
            double laneLeft = laneX[i] - laneWidth / 2.0;

            gc.setFill(Color.rgb(20, 30, 50, 0.18));
            gc.fillRect(laneLeft, laneTop, laneWidth, laneHeight);

            if (i == selectedLane) {
                gc.setStroke(Color.YELLOW);
                gc.setLineWidth(4);
            } else {
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(2);
            }

            gc.strokeRect(laneLeft, laneTop, laneWidth, laneHeight);
        }

        drawPlayer(gc, laneTop, laneHeight);
        drawAttacks(gc);
    }

    private void drawBackground(GraphicsContext gc) {
        Image bg = null;

        if ("Habitacion de Programador".equalsIgnoreCase(mapName)) {
            bg = mapHabitacion;
        } else if ("Oficina de Trabajo".equalsIgnoreCase(mapName)) {
            bg = mapOficina;
        } else if ("Cueva de Hacker".equalsIgnoreCase(mapName)) {
            bg = mapCueva;
        }

        if (bg != null) {
            gc.drawImage(bg, 0, 0, width, height);
        } else {
            gc.setFill(Color.web("#10243f"));
            gc.fillRect(0, 0, width, height);
        }

        gc.setFill(Color.rgb(5, 10, 20, 0.20));
        gc.fillRect(0, 0, width, height);
    }

    private void drawPlayer(GraphicsContext gc, double laneTop, double laneHeight) {
        double px = laneX[selectedLane];
        double py = laneTop + laneHeight - 90;

        Image playerImage = getSelectedPlayerImage();
        if (playerImage != null) {
            double size = Math.max(95, width * 0.075);
            gc.drawImage(playerImage, px - size / 2.0, py - size / 2.0, size, size);
        } else {
            gc.setFill(Color.LIME);
            gc.fillOval(px - 20, py - 20, 40, 40);
        }
    }

    private Image getSelectedPlayerImage() {
        String avatar = guiManager.getSetupData().getSelectedAvatar();

        if (avatar == null) {
            return null;
        }

        switch (avatar) {
            case "Capitan Firewall":
                return capitanPjImage;
            case "Byte Ninja":
                return ninjaPjImage;
            case "Packet Pirate":
                return piratePjImage;
            case "Null Pointer Paladin":
                return paladinPjImage;
            case "Malware Muncher":
                return muncherPjImage;
            default:
                return null;
        }
    }

    private void drawAttacks(GraphicsContext gc) {
        for (int i = 0; i < attackCount; i++) {
            FallingAttack atk = attacks[i];
            Image img = getAttackImage(atk.type);

            double size;
            if (laneCount >= 7) {
                size = Math.max(44, width * 0.032);
            } else if (laneCount >= 5) {
                size = Math.max(50, width * 0.038);
            } else {
                size = Math.max(58, width * 0.05);
            }

            double x = laneX[atk.lane] - size / 2.0;
            double y = atk.y;

            if (img != null) {
                gc.drawImage(img, x, y, size, size);
            } else {
                if ("DDOS".equals(atk.type)) {
                    gc.setFill(Color.RED);
                } else if ("MALWARE".equals(atk.type)) {
                    gc.setFill(Color.ORANGE);
                } else {
                    gc.setFill(Color.CYAN);
                }
                gc.fillOval(x, y, size, size);
            }

            gc.setFill(Color.WHITE);
            gc.fillText(atk.type, x, y - 8);
        }
    }

    private Image getAttackImage(String type) {
        if ("DDOS".equals(type)) {
            return ddosImage;
        } else if ("MALWARE".equals(type)) {
            return malwareImage;
        } else if ("CRED".equals(type)) {
            return credImage;
        }
        return null;
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
        opponentGameOver = true;

        try {
            JsonObject obj = JsonParser.parseString(payload).getAsJsonObject();

            opponentHp = obj.get("hp").getAsInt();
            opponentScore = obj.get("score").getAsInt();
            opponentLevel = obj.get("level").getAsInt();
            opponentNetworkXp = obj.get("networkXp").getAsInt();
            opponentMalwareXp = obj.get("malwareXp").getAsInt();
            opponentCryptoXp = obj.get("cryptoXp").getAsInt();

            PlayerSetupData data = guiManager.getSetupData();
            data.setOpponentFinalHp(opponentHp);
            data.setOpponentFinalScore(opponentScore);
            data.setOpponentFinalLevel(opponentLevel);
            data.setOpponentNetworkXp(opponentNetworkXp);
            data.setOpponentMalwareXp(opponentMalwareXp);
            data.setOpponentCryptoXp(opponentCryptoXp);

        } catch (Exception ignored) {
        }

        // Solo el jugador que perdió ve el mensaje central.
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
}