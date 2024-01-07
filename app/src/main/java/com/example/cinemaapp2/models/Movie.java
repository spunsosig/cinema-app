package com.example.cinemaapp2.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    @SerializedName("id")
    private int id;
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("genres")
    private List<Genre> genres;

    public List<Genre> getGenre() {
        return genres;
    }

    public Movie(int id, List<Genre> genreIds){
//        this.external_id = id;
        this.genres = genreIds;
    }

    public Movie getMovieById(int id, ArrayList<Movie> movies){
        for (Movie movie: movies){
            if (id == movie.getId()){
                return movie;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

}
