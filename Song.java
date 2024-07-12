/*
Staci Tranquille
07/12/2024
Song Object Class
Class for defining the song object and storing getters and setters for the
 */
package com.example.spotifybootleg;

public class Song {
    private String filePath;
    private String name;
    private String artist;
    private int DurationMilli;

    //Setters and Getters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDurationMilli() {
        return DurationMilli;
    }

    public void setDurationMilli(int durationMilli) {
        DurationMilli = durationMilli;
    }


    //A toString Method for Debugging

    @Override
    public String toString() {
        return "Song Name: " + name + ", Artist: " + artist;
    }
}
