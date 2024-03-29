package com.example.cinemaapp2.api;

import com.example.cinemaapp2.models.Genre;
import com.example.cinemaapp2.models.GenreResponse;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieResponse;
import com.example.cinemaapp2.models.PersonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RequestMovie {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpComingMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieById(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> searchMovie(
            @Query("query") String query,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(@Query("language") String language, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MovieResponse> getFilteredMovies(
            @Query("language") String language,
            @Query("with_genre") String genre,
            @Query("page") int page,
            @Query("api_key") String apiKey,
            @Query("keyword") String appendToResponse
    );

    @GET("discover/movie")
    Call<MovieResponse> getMoviesWithGenre(@Query("with_genres") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<PersonResponse> getCast(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);
}


