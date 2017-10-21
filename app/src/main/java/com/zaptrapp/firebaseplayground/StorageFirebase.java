package com.zaptrapp.firebaseplayground;

/**
 * Created by Nishanth on 21-Oct-17.
 */

public class StorageFirebase {
    CategoryFirebase categoryFirebase;
    String name;
    String weight;

    public StorageFirebase(CategoryFirebase categoryFirebase, String name, String weight) {
        this.categoryFirebase = categoryFirebase;
        this.name = name;
        this.weight = weight;
    }

    public CategoryFirebase getCategoryFirebase() {
        return categoryFirebase;
    }

    public void setCategoryFirebase(CategoryFirebase categoryFirebase) {
        this.categoryFirebase = categoryFirebase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


}
