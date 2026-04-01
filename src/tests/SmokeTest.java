package tests;

import game.Config;
import game.GameEngine;

public class SmokeTest {
    public static void main(String[] args) {
        Config cfg = new Config(); 
        GameEngine engine = new GameEngine(cfg, "player1");

        // Simular 600 frames (~10 segundos a 60fps)
        for (int i = 0; i < 600; i++) {
            engine.update(0.016); // delta ~16ms
            System.out.printf("Frame %d | HP: %d | Score: %d | Level: %d | ActiveAttacks: %d%n",
                    i, engine.getPlayer().getHp(), engine.getScore(), engine.getLevel(), engine.getAttackManager().activeCount());
            if (engine.getPlayer().getHp() <= 0) {
                System.out.println("Player muerto, terminando simulación.");
                break;
            }
        }
        System.out.println("SmokeTest finalizado.");
    }
}