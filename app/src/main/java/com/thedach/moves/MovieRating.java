package com.thedach.moves;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MovieRating implements Serializable {

    @SerializedName("kp")
    private double kinoPoisk;

    public MovieRating(double kinoPoisk) {
        this.kinoPoisk = kinoPoisk;
    }

    public double getKinoPoisk() {
        return kinoPoisk;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "kp='" + kinoPoisk + '\'' +
                '}';
    }
}
