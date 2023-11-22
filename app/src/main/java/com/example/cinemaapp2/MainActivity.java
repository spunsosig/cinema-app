package com.example.cinemaapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.ApiService;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);

        Call<MovieResponse> call = movieService.getPopularMovies(BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                if(movieResponse != null && movieResponse.getMovies() != null ) {
                    for (Movie movie : movieResponse.getMovies()){
                        Log.d("MOVIES", movie.getTitle());
                    }
                } else {
                    Log.d("MOVIES", "Error: null");

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("MOVIES", "could not load movies");
            }
        });

    }

}