package client.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class SoundManager {

    private static MediaPlayer backgroundMusic;

    private SoundManager() {
    }

    public static AudioClip loadClip(String resourcePath) {
        try {
            var url = SoundManager.class.getResource(resourcePath);
            if (url == null) {
                System.out.println("No se encontró sonido: " + resourcePath);
                return null;
            }
            AudioClip clip = new AudioClip(url.toExternalForm());
            clip.setVolume(0.65);
            return clip;
        } catch (Exception e) {
            System.out.println("Error cargando sonido " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }

    public static void playClip(AudioClip clip) {
        if (clip != null) {
            clip.play();
        }
    }

    public static void playMusic(String resourcePath, double volume) {
        stopMusic();

        try {
            var url = SoundManager.class.getResource(resourcePath);
            if (url == null) {
                System.out.println("No se encontró música: " + resourcePath);
                return;
            }

            Media media = new Media(url.toExternalForm());
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusic.setVolume(volume);
            backgroundMusic.play();
        } catch (Exception e) {
            System.out.println("Error cargando música " + resourcePath + ": " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) {
            try {
                backgroundMusic.stop();
                backgroundMusic.dispose();
            } catch (Exception ignored) {
            }
            backgroundMusic = null;
        }
    }
}