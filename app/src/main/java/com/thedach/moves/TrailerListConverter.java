package com.thedach.moves;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * TrailerListConverter - вспомогательный класс для того что бы Room понял как
 * TrailerList запихивать в базу данных и как там его ранить
 * Помечается анотацией @TypeConverter, с указанием класса который нужно использовать для конверта
 */
public class TrailerListConverter {
    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<TrailersList>() {}.getType();

    @TypeConverter
    public static String fromTrailersList(TrailersList trailersList) {
        if (trailersList == null) {
            return null;
        }
        return gson.toJson(trailersList);
    }

    @TypeConverter
    public static TrailersList toTrailersList(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return gson.fromJson(json, type);
    }
}
