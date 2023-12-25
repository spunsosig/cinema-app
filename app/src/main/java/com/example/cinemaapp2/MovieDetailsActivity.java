package com.example.cinemaapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.api.RequestTVShow;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;
import com.example.cinemaapp2.models.MovieResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra("movieId", -1);

        if (movieId != -1) {
            // Fetch detailed information about the movie using the movieId
            fetchMovieDetails(movieId);
            Log.d("MOVIE Details ID", String.valueOf(movieId));

        } else {
            Log.d("MOVIES", "No movie ID provided");
        }
    }

    private void fetchMovieDetails(int movieId) {
        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);

        Call<Movie> call = movieService.getMovieById(movieId ,BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.isSuccessful()){
                    Movie movie = response.body();

                    if (movie != null){
                        updateUI(movie);
                    } else {
                        Log.e("MovieDetailsActivity", "No movie with this ID");
                    }

                } else {
                    // Log error details
                    Log.e("MovieDetailsActivity", "Api response not successful");
                    Log.d("MovieDetailsActivity", "ID: " + movieId);
                    Log.d("MovieDetailsActivity", "Response code: " + response.code());
                    try {
                        Log.d("MovieDetailsActivity", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("MovieDetailsActivity", "No response");

            }
        });
    }

    private void updateUI(Movie movie) {
        // Update your UI components with the details of the fetched movie
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("Movie Title: " + movie.getTitle());

        // Update other UI components as needed
    }

}
