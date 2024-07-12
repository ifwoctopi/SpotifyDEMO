module com.example.spotifybootleg {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;

    opens com.example.spotifybootleg to javafx.fxml;
    exports com.example.spotifybootleg;
}