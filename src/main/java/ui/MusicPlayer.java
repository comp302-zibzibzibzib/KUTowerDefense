package ui;

import domain.controller.GameOptionsController;
import javafx.animation.PauseTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class MusicPlayer {

    private static MediaPlayer mediaPlayer;
    private static final Duration LOOP_DELAY = Duration.seconds(10);

    public static void playBackgroundMusic(String musicFilePath) {
        if (mediaPlayer == null) {
            URL resource = MusicPlayer.class.getResource(musicFilePath);
            if (resource == null) {
                System.err.println("Music file not found: " + musicFilePath);
                return;
            }
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnEndOfMedia(() -> {
                PauseTransition delay = new PauseTransition(LOOP_DELAY);
                delay.setOnFinished(event -> {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                });
                delay.play();
            });

            mediaPlayer.play();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public static double getVolume() {
        if (mediaPlayer != null) return mediaPlayer.getVolume();
        return 0.0;
    }

    public static void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}