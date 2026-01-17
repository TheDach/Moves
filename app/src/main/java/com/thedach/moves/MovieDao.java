package com.thedach.moves;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM FAVOURITE_MOVIES")
    LiveData<List<Movie>> getAllFavouriteMovies();

    @Query("SELECT * FROM FAVOURITE_MOVIES WHERE id = :movieId")
    LiveData<Movie> getFavouriteMovie(int movieId); // для подсветки звездочки добавления в избранное

    @Insert
    Completable insertMovie(Movie movie);

    @Query("DELETE FROM FAVOURITE_MOVIES WHERE id = :movieId")
    Completable removeMovie(int movieId);
}
