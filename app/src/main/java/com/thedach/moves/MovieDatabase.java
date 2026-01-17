package com.thedach.moves;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(TrailerListConverter.class)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance = null;
    private static final String NAME_DB = "movies.db";

    public static MovieDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    MovieDatabase.class,
                    NAME_DB
            ).build();
        }
        return instance;
    }

    public abstract MovieDao movieDao();
}
