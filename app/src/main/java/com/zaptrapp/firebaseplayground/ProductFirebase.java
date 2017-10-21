package com.zaptrapp.firebaseplayground;

/**
 * Created by Nishanth on 21-Oct-17.
 */

public class ProductFirebase {
    String id;
    String category;


    public ProductFirebase(String id, String category, String weight, String imageUrl) {
        this.id = id;
        this.category = category;
        this.weight = weight;
        this.imageUrl = imageUrl;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String weight;
    String imageUrl;


    public ProductFirebase() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
