package com.itcraftsolution.picturepoint.Models;

public class CategoryModel {
    private String CategoryName;
    private String imgUri;

    public CategoryModel(String image, String categoryName) {
        imgUri = image;
        CategoryName = categoryName;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
