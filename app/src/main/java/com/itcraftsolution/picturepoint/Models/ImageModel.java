package com.itcraftsolution.picturepoint.Models;

public class ImageModel {

    private UrlModel src;
    private String url;

    public ImageModel(UrlModel src, String url) {
        this.src = src;
        this.url = url;
    }

    public UrlModel getSrc() {
        return src;
    }

    public void setSrc(UrlModel src) {
        this.src = src;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
