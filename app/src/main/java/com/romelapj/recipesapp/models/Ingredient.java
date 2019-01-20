package com.romelapj.recipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredientDisplay;

    public Ingredient(String quantity, String measure, String ingredientDisplay) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientDisplay = ingredientDisplay;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredientDisplay = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredientDisplay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredientDisplay() {
        return ingredientDisplay;
    }

    public void setIngredientDisplay(String ingredientDisplay) {
        this.ingredientDisplay = ingredientDisplay;
    }


    @Override
    public String toString() {
        return "\u2022 " + quantity + " " + measure + "" + ingredientDisplay;
    }

}
