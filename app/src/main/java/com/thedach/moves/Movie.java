package com.thedach.moves;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/// Интерфейс Serializable (Выступает как маркер) показывает что этот класс можно превратить впоток байт
/// для того что бы потом его передаь его в Intent
/// Приметивные тиры и String он поймет как превратить в поток байт,
///  а в классах Poster и MovieRating нужно тоже реализовать этот интерфейс (никакие методы не надо переопределять)
@Entity(tableName = "favourite_movies")
public class Movie implements Serializable {

    /// делаем @SerializedName("|value|") так как в релизе будет Обфускация
    /// Обфускация - это метод, при котором исходный код программы изменяется так,
    ///  что его трудно понять. Цель — затруднить анализ, понимание алгоритмов работы и
    ///  модификацию кода при декомпиляции.
    /// @Embedded - точно так же, только для создания базы данных

    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("year")
    private int year;
    @SerializedName("description")
    private String description;
    @SerializedName("type")
    private String type;

    @SerializedName("poster")
    @Embedded
    private Poster poster;
    @SerializedName("rating")
    @Embedded
    private MovieRating rating;
    @TypeConverters(TrailerListConverter.class)
    @SerializedName("videos")
    private TrailersList trailersList;

    public Movie(String description,
                 int id,
                 String name,
                 Poster poster,
                 MovieRating rating,
                 String type,
                 int year,
                 TrailersList trailersList
    ) {

        this.description = description;
        this.id = id;
        this.name = name;
        this.poster = poster;
        this.rating = rating;
        this.type = type;
        this.year = year;
        this.trailersList = trailersList;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Poster getPoster() {
        return poster;
    }

    public MovieRating getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public TrailersList getTrailersList() {
        return trailersList;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", type='" + type + '\'' +
                ", poster=" + poster +
                ", rating=" + rating +
                ", trailers=" + trailersList +
                '}';
    }
}
