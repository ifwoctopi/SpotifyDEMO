/*
Staci Tranquille
07/12/2024
Song Controller
Secondary Controller to set & display Song Information (Song Name, Artist, Cover). Songs are to be played through the Spotify Controller.
 */
package com.example.spotifybootleg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;


public class SongController {
    @FXML
    private ImageView img;

    @FXML
    private Label songName;

    @FXML
    private Label artist;

    @FXML
    public static Button songChosen;



    public static Song currentSong = getRandomSong(SpotifyController.songLibrary.getSongs()); //Sets te initial "current song" to a random song in the library



    private SpotifyController spotifyController;

    public void setSpotifyController(SpotifyController spotifyController) {
        this.spotifyController = spotifyController;
    }

    public static Song getRandomSong(List<Song> list) {
        if (list == null || list.isEmpty()) {
            return null; // Return null if the list is null or empty
        }
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }





    public void initialize() throws IOException {
        //I have yet to figure out the autoplay feature so I'm experimenting with placement of this block of code.
        // Kind of brute forcing
        SpotifyController.mediaPlayer.setOnEndOfMedia(() -> {
            SpotifyController.mediaPlayer.stop();
            currentSong = SpotifyController.songLibrary.getSongs().get(SpotifyController.songLibrary.getSongs().indexOf(currentSong) + 1 );
            SpotifyController.setMediaPlayer();
            spotifyController.sliderControl();
            spotifyController.setCurrentSong();
            try {
                SpotifyController.mediaPlayerManager.play();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    //Method for displaying song information
    public void setData(Song song){


        InputStream inputStream = song.getClass().getResourceAsStream("albums/" + song.getFilePath() + ".PNG");

        if (inputStream != null) {
            System.out.println("file exists!");
            Image image = new Image(inputStream);
            img.setImage(image);
            // Further processing like reading the file content can be done here.
        } else {
            System.out.println("file does not exist!");
            System.out.println(song.getFilePath());
        }


        songName.setText(song.getName());
        artist.setText(song.getArtist());



    }

    //This Method controls what happens when a song is pressed without pressing the play button.
    //It is supposed to play and keep playing (autoplay is being brute-forced once again)
    public void setSong(ActionEvent ae) throws IOException {
        String chosenTitle = songName.getText();
        currentSong = SpotifyController.songLibrary.search(chosenTitle);
        System.out.print(currentSong.getName());
        spotifyController.setHeartImg();

        if(SpotifyController.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING || SpotifyController.mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED){
            SpotifyController.mediaPlayer.stop();
            currentSong = SpotifyController.songLibrary.search(chosenTitle);
            SpotifyController.setMediaPlayer();
            spotifyController.sliderControl();
            spotifyController.setCurrentSong();
            SpotifyController.mediaPlayerManager.play();
            spotifyController.playForSongController();
            spotifyController.Test();
            for(int i = 0; i < spotifyController.songLibrary.getSongs().size(); i++) {
                SpotifyController.mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = SpotifyController.songLibrary.getSongs().get(SpotifyController.songLibrary.getSongs().indexOf(currentSong) + 1);
                    SpotifyController.setMediaPlayer();
                    spotifyController.sliderControl();
                    spotifyController.setCurrentSong();

                    try {
                        SpotifyController.mediaPlayerManager.play();
                        spotifyController.Test();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }





        }
        else{
            currentSong = SpotifyController.songLibrary.search(chosenTitle);
            SpotifyController.setMediaPlayer();
            spotifyController.sliderControl();
            spotifyController.setCurrentSong();
            SpotifyController.mediaPlayerManager.play();
            spotifyController.playForSongController();
            spotifyController.Test();
            for(int i = 0; i < spotifyController.songLibrary.getSongs().size(); i++) {
                SpotifyController.mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = SpotifyController.songLibrary.getSongs().get(SpotifyController.songLibrary.getSongs().indexOf(currentSong) + 1);
                    SpotifyController.setMediaPlayer();
                    spotifyController.sliderControl();
                    spotifyController.setCurrentSong();
                    try {
                        SpotifyController.mediaPlayerManager.play();
                        spotifyController.Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
            }


        }

    }
}


