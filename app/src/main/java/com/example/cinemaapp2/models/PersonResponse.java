package com.example.cinemaapp2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonResponse {
    @SerializedName("cast")
    List<Person> cast;

    public List<Person> getCast() {
        return cast;
    }
}
