package com.example.Models;

import android.net.Uri;

public class Recipes {

    private String recipeName;
    private String method;
    private String imageUrl;
    private String category;

    public Recipes() {

    }

    public Recipes(String method,String imageUrl, String recipeName, String category) {

        if(recipeName.trim().equals("")){
            recipeName = "No name Provided";
        }
        if(method.trim().equals("")){
            method = "No method provided";
        }
        if(category.trim().equals("")){
            category = "No method provided";
        }
        this.recipeName = recipeName;
        this.method = method;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
