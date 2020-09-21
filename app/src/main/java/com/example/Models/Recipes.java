package com.example.Models;

public class Recipes {

    private String recipeName;
    private String method;
    private String imageUrl;

    public Recipes() {

    }

    public Recipes(String method,String imageUrl, String recipeName) {

        if(recipeName.trim().equals("")){
            recipeName = "No name Provided";
        }
        if(method.trim().equals("")){
            method = "No method provided";
        }
        this.recipeName = recipeName;
        this.method = method;
        this.imageUrl = imageUrl;
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

}
