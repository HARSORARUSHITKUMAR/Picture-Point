package com.itcraftsolution.picturepoint.Models;

public class CategoryModel {
    private String CategoryName;
    private int Image;

    public CategoryModel(int image, String categoryName) {
        Image = image;
        CategoryName = categoryName;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
