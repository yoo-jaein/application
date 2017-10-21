package com.example.myapplication.ProblemDomain;

import java.io.Serializable;

/**
 * Created by jm on 2017-10-03.
 */

public class Song implements Serializable {
    private static final long serialVersionUID = 100L;

    private int songId;
    private String songName;
    private int albumId;
    private String albumName;
    private int artistId;
    private String artistName;
    private int menuId;

    public Song(){
        this.songId = -1;
        this.songName = null;
        this.albumId = -1;
        this.albumName = null;
        this.artistId = -1;
        this.artistName = null;
        this.menuId = -1;
    }

    public Song(int songId, String songName, int albumId, String albumName, int artistId, String artistName, int menuId) {
        this.songId = songId;
        this.songName = songName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.menuId = menuId;
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

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", songName='" + songName + '\'' +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
