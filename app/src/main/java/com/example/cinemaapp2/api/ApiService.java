package com.example.cinemaapp2.api;

import com.example.cinemaapp2.models.Movie;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("https://api.themoviedb.org/3/")
    Call<Movie> getApiData();



}
