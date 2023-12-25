package com.example.cinemaapp2.models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    private int id;
    private String title;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    private String releaseDate;
    private double voteAverage;
    @SerializedName("genre_ids")
    private int[] genre_ids;

//    public int getExternal_id() {
//        return external_id;
//    }

    public int[] getGenre() {
        return genre_ids;
    }

    public Movie(int id, int[] genreIds){
//        this.external_id = id;
        genre_ids = genreIds;
    }

    public int getId() {
        return id;
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
