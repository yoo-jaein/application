package com.example.myapplication.ProblemDomain;

import java.io.Serializable;

/**
 * Created by jm on 2017-10-03.
 */

public class Music implements Serializable {
    private static final long serialVersionUID = 100L;

    private int musicId;
    private String musicName;
    private int albumId;
    private String albumName;
    private int artistId;
    private String artistName;
    private int menuId;

    public Music(){
        this.musicId = -1;
        this.musicName = null;
        this.albumId = -1;
        this.albumName = null;
        this.artistId = -1;
        this.artistName = null;
        this.menuId = -1;
    }

    public Music(int musicId, String musicName, int albumId, String albumName, int artistId, String artistName, int menuId) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.menuId = menuId;
    }

    public int getMusicId() {
        return musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
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
        return "Music{" +
                "musicId=" + musicId +
                ", musicName='" + musicName + '\'' +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
