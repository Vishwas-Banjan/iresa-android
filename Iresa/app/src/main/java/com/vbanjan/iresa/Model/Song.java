package com.vbanjan.iresa.Model;

import java.util.ArrayList;

public class Song {
    ArrayList<Artist> artists;
    ArrayList<String> upvotes;
    String documentID, songID, songTitle;
    String imageURL, songURI;
    Long imageHeight, imageWidth;

    public Song() {
    }

    public Song(ArrayList<Artist> artists, String documentID, String songID, String songTitle, String imageURL, String songURI, Long imageHeight, Long imageWidth, ArrayList<String> upvotes) {
        this.artists = artists;
        this.documentID = documentID;
        this.songID = songID;
        this.songTitle = songTitle;
        this.imageURL = imageURL;
        this.songURI = songURI;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.upvotes = upvotes;
    }

    public ArrayList<String> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(ArrayList<String> upvotes) {
        this.upvotes = upvotes;
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

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
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

    public Long getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Long imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Long getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Long imageWidth) {
        this.imageWidth = imageWidth;
    }
}
