package com.example.Models;

import android.net.Uri;

public class Cart {
    String receipeName,servings;
    Float price;
    String imageUri;

    public Cart() {
    }

    public String getReceipeName() {
        return receipeName;
    }

    public void setReceipeName(String receipeName) {
        this.receipeName = receipeName;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String  imageUri) {
        this.imageUri = imageUri;
    }
}
