package com.romelapj.recipesapp.models;

import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("name")
    private String name;

    public Recipe(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
