/*
Staci Tranquille
07/12/2024
Spotify Demo Application
Main Application for loading layouts and launching
 */
package com.example.spotifybootleg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class SpotifyDEMOApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("spotify-layout.fxml"));
        stage.setTitle("Spotify Bootleg");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}