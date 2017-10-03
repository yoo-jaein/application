package com.example.application.ProblemDomain;

/**
 * Created by jm on 2017-10-03.
 */

public class Song {
    private int songId;
    private String songName;
    private int albumId;
    private String albumName;
    private int artistId;
    private String artistName;

    public Song(int songId, String songName, int albumId, String albumName, int artistId, String artistName) {
        this.songId = songId;
        this.songName = songName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public int getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }
}
