package tests;

import game.*;
import patterns.AttackFactoryImpl;

public class TestHarness {

    public static void main(String[] args) {
        int failures = 0;
        System.out.println("=== TestHarness: iniciando pruebas rápidas ===");

        if (!testFactoryCreateAttack()) { failures++; }
        if (!testAttackUpdateMovement()) { failures++; }
        if (!testSpawnTimerBehavior()) { failures++; }
        if (!testCollisionConsumeOrReenqueue()) { failures++; }

        System.out.println("=== Resultados ===");
        if (failures == 0) {
            System.out.println("TODAS LAS PRUEBAS PASARON ✅");
            System.exit(0);
        } else {
            System.out.printf("FALLARON %d pruebas ❌%n", failures);
            System.exit(2);
        }
    }

    // Test 1: AttackFactoryImpl.createAttack devuelve Attack con damage y baseSpeed coherentes
    private static boolean testFactoryCreateAttack() {
        System.out.println("[Test] Factory createAttack()");
        Config cfg = new Config();
        AttackFactoryImpl factory = new AttackFactoryImpl(cfg);

        Attack a1 = factory.createAttack("DDOS", 1, 10.0, 0.0);
        Attack a3 = factory.createAttack("DDOS", 3, 20.0, 0.0);

        if (a1 == null || a3 == null) {
            System.out.println("  FAIL: factory devolvió null");
            return false;
        }

        int dmg1 = a1.getDamage();
        int dmg3 = a3.getDamage();
        if (dmg3 <= dmg1) {
            System.out.printf("  FAIL: damage no escala por nivel (lvl1=%d lvl3=%d)%n", dmg1, dmg3);
            return false;
        }

        double s1 = a1.getBaseSpeed();
        double s3 = a3.getBaseSpeed();
        if (s3 < s1) {
            System.out.printf("  FAIL: baseSpeed no escala correctamente (lvl1=%.3f lvl3=%.3f)%n", s1, s3);
            return false;
        }

        System.out.printf("  PASS: DDOS lvl1 dmg=%d speed=%.3f ; lvl3 dmg=%d speed=%.3f%n", dmg1, s1, dmg3, s3);
        return true;
    }

    // Test 2: Attack.update mueve Y según (baseSpeed + speedMultiplier) * delta
    private static boolean testAttackUpdateMovement() {
        System.out.println("[Test] Attack.update(delta, speedMultiplier)");
        // Creamos un ataque con baseSpeed conocido
        Attack a = new Attack("TEST", 1, 100.0, 0.0, 0.0);
        double delta = 0.5; // segundos
        double speedMultiplier = 10.0; // unidades/segundo
        double beforeY = a.getY();
        a.update(delta, speedMultiplier);
        double afterY = a.getY();
        double expected = (100.0 + speedMultiplier) * delta;
        double diff = Math.abs(afterY - beforeY - expected);
        if (diff > 1e-6) {
            System.out.printf("  FAIL: movimiento incorrecto (esperado=%.6f real=%.6f diff=%.6f)%n", expected, afterY - beforeY, diff);
            return false;
        }
        System.out.printf("  PASS: moved by %.6f as expected%n", afterY - beforeY);
        return true;
    }

    // Test 3: spawnTimer acumulado genera N spawns cuando delta >= spawnRate * N
    private static boolean testSpawnTimerBehavior() {
        System.out.println("[Test] spawnTimer behavior (AttackManager.update)");
        Config cfg = new Config();
        // Ajustamos spawnRate para test determinista
        cfg.setBaseSpawnRate(0.5); // 0.5s
        AttackManager am = new AttackManager(cfg);
        int level = 1;

        // Simulamos un delta = 1.25s -> debería generar floor(1.25/0.5) = 2 spawns y dejar spawnTimer = 0.25
        double delta = 1.25;
        int before = am.activeCount();
        am.update(delta, level);
        int after = am.activeCount();
        int spawned = after - before;
        double spawnTimer = am.getSpawnTimer();
        if (spawned != 2) {
            System.out.printf("  FAIL: spawns generados incorrectos (esperado=2 real=%d)%n", spawned);
            return false;
        }
        double expectedResidue = 1.25 - 2 * 0.5;
        if (Math.abs(spawnTimer - expectedResidue) > 1e-6) {
            System.out.printf("  FAIL: spawnTimer residue incorrecto (esperado=%.6f real=%.6f)%n", expectedResidue, spawnTimer);
            return false;
        }
        System.out.printf("  PASS: spawned=%d spawnTimer residue=%.6f%n", spawned, spawnTimer);
        return true;
    }

    // Test 4: checkCollisions consume ataques en rango y re-enqueuea los que no colisionan
    private static boolean testCollisionConsumeOrReenqueue() {
        System.out.println("[Test] GameEngine.checkCollisions() consume o re-enqueuea correctamente");
        Config cfg = new Config();
        cfg.setBaseSpawnRate(10.0); // evitar spawns automáticos
        GameEngine engine = new GameEngine(cfg, "tester");
        AttackManager am = engine.getAttackManager();
        Player p = engine.getPlayer();

        // Posicionamos player en (100,100)
        p.setX(100.0);
        p.setY(100.0);

        // Creamos un ataque dentro del umbral y otro fuera
        Attack hit = new Attack("MALWARE", 8, 50.0, 100.0, 100.0); // colisionará
        Attack miss = new Attack("DDOS", 5, 50.0, 300.0, 300.0); // no colisionará

        am.enqueueExisting(hit);
        am.enqueueExisting(miss);

        int beforeCount = am.activeCount();
        // Llamamos a update + checkCollisions via engine.update
        engine.update(0.016); // un frame pequeño
        int afterCount = am.activeCount();

        // El ataque que colisionó debe haber sido consumido -> count decrece por 1
        if (afterCount != beforeCount - 1) {
            System.out.printf("  FAIL: activeCount incorrecto (antes=%d despues=%d)%n", beforeCount, afterCount);
            return false;
        }

        // HP del player debe haber bajado por el daño del ataque 'hit'
        if (p.getHp() >= cfg.getInitialHp()) {
            System.out.printf("  FAIL: HP no disminuyó tras colisión (hp=%d)%n", p.getHp());
            return false;
        }

        System.out.printf("  PASS: collision consumida y miss re-enqueueado (antes=%d despues=%d hp=%d)%n", beforeCount, afterCount, p.getHp());
        return true;
    }
}