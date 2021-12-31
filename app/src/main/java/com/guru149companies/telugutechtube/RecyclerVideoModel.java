package com.guru149companies.telugutechtube;

public class RecyclerVideoModel {
String videoId,videoUri,title,description;
int likes,shares,downloads,commnets;

    public RecyclerVideoModel() {
    }

    public RecyclerVideoModel(String videoId, String videoUri, String title, String description, int likes, int shares, int downloads, int commnets) {
        this.videoId = videoId;
        this.videoUri = videoUri;
        this.title = title;
        this.description = description;
        this.likes = likes;
        this.shares = shares;
        this.downloads = downloads;
        this.commnets = commnets;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getCommnets() {
        return commnets;
    }

    public void setCommnets(int commnets) {
        this.commnets = commnets;
    }
}
