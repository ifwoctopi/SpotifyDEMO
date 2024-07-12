/*
Staci Tranquille
07/12/2024
Song Database
Song DataBase for storing songs and their information in various classes. You can also alter the song database through the various methods.
 */
package com.example.spotifybootleg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongDataBase {

        //Song  arraylist (because type arraylist is the parameter needed for a database object)
        private List<Song> songs;

        //When a SongDatabase object is declared without parameters that instance of books is a new arraylist
        public SongDataBase() {
            this.songs = new ArrayList<>();

        }

        //SongDatabase constructor
        public SongDataBase(List<Song> songs) {
            this.songs = songs;

        }

        //Song Setter and Getter (For accessing and mutating author objects outside the class)
        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }

        public List<Song> getSongs() {
            return this.songs;
        }

        //SongDatabase Methods

        //Adds a song to the database
        public void addSong(Song song) {
            this.songs.add(song);
        }

        //Removes a song from the database
        public void removeSong(Song song) {
            this.songs.remove(song);
        }
        public void setSong(int index, Song song){
        this.songs.add(index,song);
    }
    public void removeSong(int index) {
        this.songs.remove(index);
    }

        //Searches for all songs in the database with a given title
        public Song search(String title) {

            //Prompt that repeats back what you're searching for
            System.out.println("Searching for..." + title);

            Song results = null;

            //Searches the database for the song that fits the criteria and adds it to the results arraylist
            for (Song song : this.songs) {
                String searchedTitle = song.getName();

                if (searchedTitle.equals(title)){
                    results = song;

                }

            }
            //returns the song found (all methods follow this format)
            return results;
        }

        //Shuffles the song library by shuffling the arraylist of songs
        public List<Song> shuffleSongs() {
            List<Song> shuffledSongs = new ArrayList<>(this.songs);
            Collections.shuffle(shuffledSongs);
            return shuffledSongs;
        }
}
