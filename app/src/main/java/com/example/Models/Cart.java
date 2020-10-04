package com.example.Models;

public class Cart {
    String receipeName,servings;
    Float price,Total;
    String imageUri;

    public Cart() {
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(float total) {
        Total = total;
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
