package com.example.cinemaapp2.api;

import com.example.cinemaapp2.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RequestMovie {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    Call<MovieResponse> getnowPlayingMovies(@Query("api_key") String apiKey);
}


