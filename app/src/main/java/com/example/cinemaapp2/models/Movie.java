package com.example.cinemaapp2.models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("external_id")
    private int external_id;
    private String title;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    private String releaseDate;
    private double voteAverage;

    public Movie(int id){
        this.external_id = id;
    }

    public int getId() {
        return external_id;
    }

    public void setId(int id) {
        this.external_id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

}
