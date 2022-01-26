package com.itcraftsolution.picturepoint.Models;

public class ImageModel {

    private UrlModel urls;
    private UserModel user;
    private DownloadModel download;

    public ImageModel(UrlModel urls, UserModel user, DownloadModel download) {
        this.urls = urls;
        this.user = user;
        this.download = download;
    }

    public UrlModel getUrls() {
        return urls;
    }

    public void setUrls(UrlModel urls) {
        this.urls = urls;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public DownloadModel getDownload() {
        return download;
    }

    public void setDownload(DownloadModel download) {
        this.download = download;
    }
}
