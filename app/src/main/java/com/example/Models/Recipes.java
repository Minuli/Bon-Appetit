package com.example.Models;

public class Recipes {

    private String recipeName;
    private String method;
    private String imageUrl;
    private String servings;
    private String price;

    public Recipes() {

    }

    public Recipes(String method,String imageUrl, String recipeName, String servings, String unit, String price) {

        if(recipeName.trim().equals("")){
            recipeName = "No name Provided";
        }
        if(method.trim().equals("")){
            method = "No method provided";
        }
        if(servings.trim().equals("")){
            servings = "No method provided";
        }
        if(price.trim().equals("")){
            price = "Price not provided";
        }
        this.recipeName = recipeName;
        this.method = method;
        this.imageUrl = imageUrl;
        this.servings = servings;
        this.price = price;
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

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
