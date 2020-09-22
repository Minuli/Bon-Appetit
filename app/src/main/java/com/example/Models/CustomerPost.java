package com.example.Models;

public class CustomerPost {


    private String recipeName;
    private String imgUrl;
    private String  ingredients;
    private String  method;


    public CustomerPost( String recipeName, String imgUrl, String ingredients, String method) {

        this.recipeName = recipeName;
        this.imgUrl = imgUrl;
        this.ingredients = ingredients;
        this.method = method;
    }



    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
