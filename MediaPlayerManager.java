
/*
Staci Tranquille
07/12/2024
Media Player Manager
Controls the mediaplayer to play and pause music.
 */
package com.example.spotifybootleg;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;


public class MediaPlayerManager {
    private static MediaPlayerManager instance;
    private MediaPlayer mediaPlayer;
    private SpotifyController spotifyController;

    // Private constructor to prevent instantiation
    public  MediaPlayerManager() {
    }



    public void setSpotifyController(SpotifyController spotifyController) {
        this.spotifyController = spotifyController;
    }




    // Method to set the media player
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    // Method to pause the media player
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            SpotifyController.currentlyPlaying = false;
        }
    }

    // Additional methods for play, stop, etc.
    public void play() throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.play();

            SpotifyController.currentlyPlaying = true;

        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            SpotifyController.currentlyPlaying = false;
        }
    }


}

