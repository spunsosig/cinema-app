package com.example.cinemaapp2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Genre {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Genre getGenreByName(String name, List<Genre> genres){
        for (Genre genre: genres){
            if (genre.getName().equals(name)){
                return genre;
            }
        }
        return null;
    }

    public static Genre getGenreById(int id, List<Genre> genres){
        for (Genre genre: genres){
            if (genre.getId() == id){
               return genre;
            }
        }
    return null;
    }
}
