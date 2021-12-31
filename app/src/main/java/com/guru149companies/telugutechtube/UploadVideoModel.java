package com.guru149companies.telugutechtube;

public class UploadVideoModel {
    String comments, description;
    int likes;
    String profilepic,pushKey, tags, tittle, uid;
    int uploadedTime;
    String uploaderName, videoStatus, videoUri;
    int view;
    String viewertype;

    public UploadVideoModel() {
    }

    public UploadVideoModel(String comments, String description, int likes, String profilepic, String pushKey, String tags, String tittle, String uid, int uploadedTime, String uploaderName, String videoStatus, String videoUri, int view, String viewertype) {
        this.comments = comments;
        this.description = description;
        this.likes = likes;
        this.profilepic = profilepic;
        this.pushKey = pushKey;
        this.tags = tags;
        this.tittle = tittle;
        this.uid = uid;
        this.uploadedTime = uploadedTime;
        this.uploaderName = uploaderName;
        this.videoStatus = videoStatus;
        this.videoUri = videoUri;
        this.view = view;
        this.viewertype = viewertype;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(int uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(String videoStatus) {
        this.videoStatus = videoStatus;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getViewertype() {
        return viewertype;
    }

    public void setViewertype(String viewertype) {
        this.viewertype = viewertype;
    }
}