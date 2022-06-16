package com.itcraftsolution.picturepoint.Models;

import java.util.ArrayList;

public class SearchModel {
    private ArrayList<ImageModel> photos;

    public SearchModel(ArrayList<ImageModel> results) {
        this.photos = results;
    }

    public ArrayList<ImageModel> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<ImageModel> photos) {
        this.photos = photos;
    }
}
