package com.zaptrapp.firebaseplayground;

/**
 * Created by Nishanth on 21-Oct-17.
 */

public class ProductFirebase {
    String id;
    String category;
    int weight;
    String imageUrl;

    @Override
    public String toString() {
        return "ProductFirebase{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", weight=" + weight +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductFirebase() {
    }

    public ProductFirebase(String id, String category, int weight, String imageUrl) {
        this.id = id;
        this.category = category;
        this.weight = weight;
        this.imageUrl = imageUrl;
    }
}
