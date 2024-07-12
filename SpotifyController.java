/*
Staci Tranquille
07/12/2024
Controller for Spotify Demo
Controls Main GUI for application as well as uploads music files to be stored in the SongDataBase
 */
package com.example.spotifybootleg;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import static com.example.spotifybootleg.SongController.currentSong;



public class SpotifyController implements Initializable {

    //Note to Self: FXML variables will NOT work if they are set to public. Same thing with static.

    @FXML
    private Label currentArtist;

    @FXML
    private ImageView currentCover;

    @FXML
    private Label currentSongName;

    @FXML
    private Label currentTime;

    @FXML
    private Label duration;
    @FXML
    private Button isPlaying;
    @FXML
    private Slider mySlider;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;



    public static boolean currentlyPlaying = false;

    @FXML
    private ImageView playButton;

    @FXML
    private HBox favoritesContainer;

    @FXML
    private HBox recentlyPlayedContainer;
    @FXML
    private Button repeatButton;
    @FXML
    private Button shuffleButton;

    @FXML
    private ImageView shuffleImg;
    @FXML
    private ImageView repeatImg;
    @FXML
    private ImageView heartImg;
    @FXML
    private Button heartButton;

    private List<Song> recentlyPlayedSongs;
    private List<Song> favoriteSongs;
    private List<Song> generalSongs = new ArrayList<>();

    public static boolean isRepeating = false;
    public static boolean isShuffled = false;
    public static boolean isHearted = false;

    public static SongDataBase songLibrary = new SongDataBase();
    public static MediaPlayerManager mediaPlayerManager = new MediaPlayerManager();
    public static MediaPlayer mediaPlayer;




//Method that runs when the application is launched
// (for setting things that need to be set before the user takes any action)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentlyPlayedSongs = getRecentlyPlayed();
        favoriteSongs = getFavoriteSongs();
        songLibrary.setSongs(generalSongs);
        setMediaPlayer();
        setCurrentSong();


        sliderControl();
        try {
            initializeSongs(recentlyPlayedSongs, recentlyPlayedContainer);
            initializeSongs(favoriteSongs, favoritesContainer);



        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("MediaPlayer Null");
        }

        //Once a song ends the next song plays
        for(int i = 0; i < songLibrary.getSongs().size(); i++) {
            mediaPlayer.setOnEndOfMedia(() -> {
                SpotifyController.mediaPlayer.stop();
                currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                setMediaPlayer();
                setCurrentSong();
                sliderControl();
                mediaPlayer.play();
                try {
                    Test();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }





    }
    //Initializes the songs with information from the csv file to be set using Song Controller. Adds a vbox every time a song is set and
    // displays the information in the created vbox
    private void initializeSongs(List<Song> songs, HBox container) throws IOException {
        for (Song song : songs) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/spotifybootleg/song.fxml"));
            VBox vBox = fxmlLoader.load();
            SongController songController = fxmlLoader.getController();
            songController.setData(song);
            container.getChildren().add(vBox);
            songController.setSpotifyController(SpotifyController.this);


        }
    }

    //Same idea but for single songs. I made a single initializer so I could add song  VBoxes to the
    //recently played and favorites containers.

    private void initializeSong(Song song, HBox container) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/spotifybootleg/song.fxml"));
        VBox vBox = fxmlLoader.load();
        SongController songController = fxmlLoader.getController();
        songController.setData(song);
        songController.setSpotifyController(SpotifyController.this);

        if(isHearted == true) {
            container.getChildren().add(vBox);
        }
        if(isHearted == false) { //if the song is in favorites
            container.getChildren().remove(favoriteSongs.indexOf(song)); //remove vbox
        }
    }
    public void recentlyPlayedInitializer(Song song, HBox container) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/spotifybootleg/song.fxml"));
        VBox vBox = fxmlLoader.load();
        SongController songController = fxmlLoader.getController();
        songController.setData(song);
        songController.setSpotifyController(SpotifyController.this);

        if(recentlyPlayedSongs.contains(song)){
            container.getChildren().remove(recentlyPlayedSongs.indexOf(song));
            container.getChildren().add(0,vBox);
            recentlyPlayedSongs.remove(song);
            recentlyPlayedSongs.add(0,song);
        }
        else{
            container.getChildren().add(0,vBox);
            recentlyPlayedSongs.add(0,song);
        }
    }
    //Method for calling the recentlyPlayed Initializer without making it static
    public void Test() throws IOException {
        recentlyPlayedInitializer(currentSong,recentlyPlayedContainer);
    }



    //Method for displaying the information of the current song playing
    public void setCurrentSong() {
        currentSongName.setText(currentSong.getName());
        currentArtist.setText(currentSong.getArtist());
        Image image = new Image(getClass().getResourceAsStream("albums/" + currentSong.getFilePath() + ".PNG"));
        currentCover.setImage(image);
    }

    //Sets the song to be played in the mediaplayer
    public static void setMediaPlayer(){
        String MusicFile = currentSong.getFilePath();
        String MusicFilePath = SpotifyController.class.getResource("Songs/" + MusicFile + ".mp3").toString();

        if (MusicFilePath != null) {
            System.out.println("file exists!");

            // Further processing like reading the file content can be done here.
        } else {
            System.out.println("file does not exist!");
            System.out.println(MusicFilePath);
        }


        Media media = new Media(MusicFilePath);

        try {
            mediaPlayer = new MediaPlayer(media);
            mediaPlayerManager.setMediaPlayer(mediaPlayer);


            System.out.println(mediaPlayerManager.getMediaPlayer());




        } catch (MediaException e) {
            System.out.println("Media Exception");
            e.printStackTrace(); // Log the stack trace for debugging
        }
    }

    //When the heart button is selected and a song isn't in favorites
    // the song is added into favorite songs
    public void setHeartButton(ActionEvent ae) throws IOException {
        if (favoriteSongs.contains(currentSong)) {
            InputStream unhearted = getClass().getResourceAsStream("Images/heart_icon.png");
            Image unheartedButtonImage = new Image(unhearted);

            heartImg.setImage(unheartedButtonImage);
            isHearted = false;
            initializeSong(currentSong, favoritesContainer);
            favoriteSongs.remove(currentSong);

            if (!recentlyPlayedSongs.contains(currentSong)){
                songLibrary.getSongs().remove(currentSong);
            }


            System.out.println(favoriteSongs);



        }
        else{
            InputStream heart = getClass().getResourceAsStream("Images/hearted_icon.png");
            Image HeartedButtonImage = new Image(heart);

            heartImg.setImage(HeartedButtonImage);
            isHearted = true;
            favoriteSongs.add(currentSong);
            initializeSong(currentSong, favoritesContainer);

            System.out.println(favoriteSongs);

        }


    }

    //Method for controlling how the heart button looks without clicking on it
    public void setHeartImg() {
        if (favoriteSongs.contains(currentSong)) {
            InputStream hearted = getClass().getResourceAsStream("Images/hearted_icon.png");
            Image heartedButtonImage = new Image(hearted);

            heartImg.setImage(heartedButtonImage);


        } else {
            InputStream heart = getClass().getResourceAsStream("Images/heart_icon.png");
            Image HeartButtonImage = new Image(heart);

            heartImg.setImage(HeartButtonImage);


        }
    }

    //A lot of code is duplicated from here on out because I need to
    // figure out how to have the autoplay feature run regardless of idle behavior.

    //Controller for the play button
    public void setPlayButton(ActionEvent ae) throws IOException {
        if (currentlyPlaying == false) {
            InputStream pause = getClass().getResourceAsStream("Images/pause_icon.png");
            Image pauseButtonImage = new Image(pause);

            playButton.setImage(pauseButtonImage);
            mediaPlayerManager.play();
            Test(); //Once the song is playing it is added to recently played songs
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }




        }
        else if (currentlyPlaying == true) {

            InputStream play = getClass().getResourceAsStream("Images/play_icon2.png");
            Image playButtonImage = new Image(play);
            playButton.setImage (playButtonImage);
            mediaPlayerManager.pause();


        }
    }
    //Method that allows you to control the play button image from other classes
    public void playForSongController() {
        InputStream pause = getClass().getResourceAsStream("Images/pause_icon.png");
        Image pauseButtonImage = new Image(pause);

        playButton.setImage(pauseButtonImage);
    }
    //Controls the duration times and the slider behavior
    public void sliderControl(){
        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {



            if (!mySlider.isValueChanging()) { //the slider isn't moving

                mySlider.setValue(newTime.toSeconds());     //The time moves up second by second like normal
                System.out.println(newTime.toSeconds());
                int minutes = (int) newTime.toMinutes();
                int seconds = (int) newTime.toSeconds() % 60;

                if (seconds < 10) {
                    currentTime.setText(minutes + ":" + "0" + seconds);
                }

                else {
                    currentTime.setText(minutes + ":" + seconds);
                }

            }


        });




        mediaPlayer.setOnReady(() -> {
            mySlider.setMin(0);
            mySlider.setMax(mediaPlayer.getTotalDuration().toSeconds());  //Max is set to the song duration
            mySlider.setValue(0);
            int totalMinutes = (int) mediaPlayer.getTotalDuration().toMinutes();
            int totalSeconds = (int) mediaPlayer.getTotalDuration().toSeconds() % 60;;


            if (totalSeconds < 10) {
                duration.setText(totalMinutes + ":" + "0" + totalSeconds);
            }

            else {
                duration.setText(totalMinutes + ":" + totalSeconds);
            }
        });



        // Add listener for slider value change
        mySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mySlider.isValueChanging()) { //User moves the slider

                mediaPlayer.seek(Duration.seconds(newValue.doubleValue())); //Seeks to the associated time with the position the slider is placed at
                System.out.println("MediaPlayer state: " + mediaPlayer.getStatus());
                System.out.print("Slider new value: " + newValue.doubleValue());
                System.out.print("Current Media Time: " + mediaPlayer.getCurrentTime().toSeconds());
            }

        });

    }
    //Controls the previous button
    public void prevButton(ActionEvent ae) throws IOException {

        int currentSongIndex = 0;
        System.out.println("next button clicked");
        System.out.println("Current Song: " + currentSong.getName());


        mediaPlayerManager.stop();

        currentSongIndex = songLibrary.getSongs().indexOf(currentSong);
        if (currentSongIndex == 0) { //if you're playing the first song it will start the first song over

            currentSong = songLibrary.getSongs().get(0);


            setMediaPlayer();
            setCurrentSong();
            sliderControl();
            mediaPlayerManager.play();
            Test();
            playForSongController();
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        }
        else {
            currentSong = songLibrary.getSongs().get(currentSongIndex - 1);


            setMediaPlayer();
            setCurrentSong();
            sliderControl();
            mediaPlayerManager.play();
            Test();

            playForSongController();
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        }



    }

    //Controls the next button
    public void nextButton(ActionEvent ae) throws IOException {
        int currentSongIndex = 0;
        System.out.println("next button clicked");
        System.out.println("Current Song: " + currentSong.getName());


        mediaPlayerManager.stop();
        currentSongIndex = songLibrary.getSongs().indexOf(currentSong);





        if (currentSongIndex == songLibrary.getSongs().size() - 1) { //if song is the last song in the library the first song in the library will play
            currentSong = songLibrary.getSongs().get(0);


            setMediaPlayer();
            setCurrentSong();
            sliderControl();
            mediaPlayerManager.play();
            playForSongController();
            Test();
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }


        }
        else {
            currentSong = songLibrary.getSongs().get(currentSongIndex + 1);


            setMediaPlayer();
            setCurrentSong();
            sliderControl();
            mediaPlayerManager.play();
            playForSongController();
            Test();

            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        }




    }

    //Controls the repeat button
    public void repeatButtonCtrl(ActionEvent ae){
        if(isRepeating == false){
            int currentSongIndex = songLibrary.getSongs().indexOf(currentSong);
            songLibrary.setSong(currentSongIndex + 1, currentSong); //adds the same song to the next index in the library
            isRepeating = true;
            InputStream repeating = getClass().getResourceAsStream("Images/repeating_icon.png");
            Image repeatButtonImage = new Image(repeating);

            repeatImg.setImage(repeatButtonImage);

            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }


            for(Song song : songLibrary.getSongs()) {
                System.out.println(song);
            }

        }
        else{
            int currentSongIndex = songLibrary.getSongs().indexOf(currentSong);
            songLibrary.removeSong(currentSongIndex + 1); //removes the song from the next index
            isRepeating = false;
            InputStream notRepeating = getClass().getResourceAsStream("Images/repeat_icon.png");
            Image notRepeatingButtonImage = new Image(notRepeating);
            repeatImg.setImage(notRepeatingButtonImage);
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            for(Song song : songLibrary.getSongs()) {
                System.out.println(song);
            }
        }
    }

    public void shuffleButtonCtrl(ActionEvent ae){
        if(isShuffled == false){
            InputStream shuffled = getClass().getResourceAsStream("Images/shuffled_icon.png");
            Image shuffledButtonImage = new Image(shuffled);
            shuffleImg.setImage(shuffledButtonImage);
            List<Song> shuffledSongs = songLibrary.shuffleSongs();//wanted to shuffle the songs while keeping a copy of the original song list to revert back to
            songLibrary.setSongs(shuffledSongs);
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            System.out.println(shuffledSongs);
            isShuffled = true;

        }
        else{
            InputStream notShuffled = getClass().getResourceAsStream("Images/shuffle_icon.png");
            Image NotShuffledButtonImage = new Image(notShuffled);
            shuffleImg.setImage(NotShuffledButtonImage);
            songLibrary.setSongs(generalSongs); //reverts back to the original song list
            for(int i = 0; i < songLibrary.getSongs().size(); i++) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    SpotifyController.mediaPlayer.stop();
                    currentSong = songLibrary.getSongs().get(songLibrary.getSongs().indexOf(currentSong) + 1);
                    setMediaPlayer();
                    setCurrentSong();
                    sliderControl();
                    mediaPlayer.play();
                    try {
                        Test();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            System.out.println(generalSongs);
            isShuffled = false;
        }


    }


    //Gets file for recently played songs and adds them into the song library
    public List<Song> getRecentlyPlayed() {
        List<Song> recentlyPlayed = new ArrayList<>();


        try {

            InputStream csvStream = getClass().getResourceAsStream("/com/example/spotifybootleg/recentlyPlayed.csv");
            if (csvStream == null) {
                throw new FileNotFoundException("CSV file not found in classpath");
            }
            Scanner myReader = new Scanner(csvStream);
            myReader.nextLine();

            while (myReader.hasNextLine()) {
                String songScanned = myReader.nextLine();
                String[] songInfo = songScanned.split(",");

                Song song = new Song();
                song.setName(songInfo[0]);
                song.setFilePath(songInfo[2]); //Music file and Cover file are named the same with different paths
                song.setArtist(songInfo[1]);
                song.setDurationMilli(Integer.parseInt(songInfo[4]));

                recentlyPlayed.add(song); //recentlyplayed list
                generalSongs.add(song); //general song library
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }


        return recentlyPlayed;
    }

    //Gets file for favorite songs and adds them into the song library
    public List<Song> getFavoriteSongs() {
        List<Song> favorites = new ArrayList<>();



        try {

            InputStream csvStream = getClass().getResourceAsStream("/com/example/spotifybootleg/favorites.csv");
            if (csvStream == null) {
                throw new FileNotFoundException("CSV file not found in classpath");
            }
            Scanner myReader = new Scanner(csvStream);
            myReader.nextLine();

            while (myReader.hasNextLine()) {
                String songScanned = myReader.nextLine();
                String[] songInfo = songScanned.split(",");

                Song song = new Song();
                song.setName(songInfo[0]);
                song.setFilePath(songInfo[2]);
                song.setArtist(songInfo[1]);
                song.setDurationMilli(Integer.parseInt(songInfo[4]));

                favorites.add(song); //favorites song list
                generalSongs.add(song); //general song library
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }



        return favorites;
    }

}
