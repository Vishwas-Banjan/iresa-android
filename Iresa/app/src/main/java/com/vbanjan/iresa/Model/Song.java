package com.vbanjan.iresa.Model;

import java.util.ArrayList;

public class Song {
    ArrayList<String> artists;
    String documentID, songID, songTitle;
    String imageURL, songURI, imageHeight, imageWidth;

    public Song() {
    }

    public Song(ArrayList<String> artists, String documentID, String songID, String songTitle, String imageURL, String songURI, String imageHeight, String imageWidth) {
        this.artists = artists;
        this.documentID = documentID;
        this.songID = songID;
        this.songTitle = songTitle;
        this.imageURL = imageURL;
        this.songURI = songURI;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    @Override
    public String toString() {
        return "Song{" +
                "artists=" + artists +
                ", documentID='" + documentID + '\'' +
                ", songID='" + songID + '\'' +
                ", songTitle='" + songTitle + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", songURI='" + songURI + '\'' +
                ", imageHeight='" + imageHeight + '\'' +
                ", imageWidth='" + imageWidth + '\'' +
                '}';
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<String> artists) {
        this.artists = artists;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSongURI() {
        return songURI;
    }

    public void setSongURI(String songURI) {
        this.songURI = songURI;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }
}
