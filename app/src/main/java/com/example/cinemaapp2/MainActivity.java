package com.example.cinemaapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.ApiService;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);

        Call<MovieResponse> call = movieService.getPopularMovies(BuildConfig.TMDB_API_KEY);
//          Call<MovieResponse> call =  movieService.getnowPlayingMovies(BuildConfig.TMDB_API_KEY);


        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                ImageView imageView = findViewById(R.id.moviePoster);

//                if(movieResponse != null && movieResponse.getMovies() != null ) {
//                    for (Movie movie : movieResponse.getMovies()){
//                        String poster = movie.getPosterPath();
//                        Log.d("MOVIES", movie.getTitle());
//                        if (poster != null){
//                            Picasso.get().load(movie.getPosterPath()).into(imageView);
//                        } else {
//                            Log.d("POSTER", "Error: poster path is null or empty");
//                        }
//                    }
//
//                } else {
//                    Log.d("MOVIES", "Error: null");
//                }
                if (movieResponse != null && movieResponse.getMovies() != null && !movieResponse.getMovies().isEmpty()) {
                    // Assuming you want to load the first movie's poster
                    Movie firstMovie = movieResponse.getMovies().get(0);
                    String posterPath = firstMovie.getPosterPath();
                    Log.d("PosterPath", firstMovie.getPosterPath());



                    // Ensure that the poster path is not null or empty
                    if (posterPath != null && !posterPath.isEmpty()) {
                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterPath).into(imageView);
                        Log.d("MOVIETITLE", firstMovie.getTitle());
                    } else {
                        Log.d("MOVIES", "Error: Poster path is null or empty");
                    }
                } else {
                    Log.d("MOVIES", "Error: null or empty movie list");
                }

                for (Movie movie: movieResponse.getMovies()){
                    Log.d("MOVIETITLE", movie.getTitle());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("MOVIES", "could not load movies");
            }
        });
    }
    }

