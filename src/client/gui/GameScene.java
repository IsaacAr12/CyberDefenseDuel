package client.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScene {

    private final double[] lanes = {200, 450, 700};
    private int selectedLane = 1;

    private int hp = 100;

    private final List<Attack> attacks = new ArrayList<>();
    private final Random random = new Random();

    public Scene createScene() {

        Canvas canvas = new Canvas(900, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {

            long last = 0;

            @Override
            public void handle(long now) {

                if (last == 0) {
                    last = now;
                    return;
                }

                double delta = (now - last) / 1e9;
                last = now;

                update(delta);
                render(gc);
            }

        }.start();

        return new Scene(new StackPane(canvas));
    }

    private void update(double delta) {

        if (random.nextDouble() < 0.02) {
            attacks.add(new Attack(random.nextInt(3)));
        }

        for (Attack a : attacks) {
            a.y += 200 * delta;

            if (a.y > 550) {
                hp -= 5;
            }
        }
    }

    private void render(GraphicsContext gc) {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 900, 600);

        for (int i = 0; i < 3; i++) {
            gc.setStroke(Color.WHITE);
            gc.strokeRect(lanes[i] - 50, 50, 100, 500);
        }

        gc.setFill(Color.GREEN);
        gc.fillRect(lanes[selectedLane] - 40, 520, 80, 20);

        for (Attack a : attacks) {
            gc.setFill(Color.RED);
            gc.fillOval(lanes[a.lane] - 20, a.y, 40, 40);
        }
    }

    private static class Attack {
        int lane;
        double y = 60;

        Attack(int lane) {
            this.lane = lane;
        }
    }
}