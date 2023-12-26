package com.example.cinemaapp2.models;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("gender")
    private int gender;
    @SerializedName("id")
    private int id;
    @SerializedName("known_for_department")
    private String known_for_department;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profile_path;
    @SerializedName("character")
    private String character;
    @SerializedName("order")
    private int order;

    public boolean isAdult() {
        return adult;
    }

    public int getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getCharacter() {
        return character;
    }

    public int getOrder() {
        return order;
    }
}
