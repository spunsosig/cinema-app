package com.example.cinemaapp2.api;

import com.example.cinemaapp2.models.Genre;
import com.example.cinemaapp2.models.GenreResponse;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RequestMovie {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/external_ids")
    Call<Movie> getMovieById(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

//    @GET("search/movie?query={query}")
//    Call<MovieResponse> searchMovie(@Query("query") String query, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> searchMovie(@Query("query") String query, @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(@Query("language") String language, @Query("api_key") String apiKey);

}


