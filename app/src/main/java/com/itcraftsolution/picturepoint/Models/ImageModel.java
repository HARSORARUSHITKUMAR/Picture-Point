package com.itcraftsolution.picturepoint.Models;

public class ImageModel {

    private UrlModel urls;
    private UserModel user;
    private DownloadModel links;

    public ImageModel(UrlModel urls, UserModel user, DownloadModel links) {
        this.urls = urls;
        this.user = user;
        this.links = links;
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

    public DownloadModel getLinks() {
        return links;
    }

    public void setLinks(DownloadModel links) {
        this.links = links;
    }
}
