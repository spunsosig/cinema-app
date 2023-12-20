package com.example.cinemaapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.ApiService;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;
import com.example.cinemaapp2.models.MovieResponse;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);

        Call<MovieResponse> call = movieService.getPopularMovies(BuildConfig.TMDB_API_KEY);
//          Call<MovieResponse> call =  movieService.getNowPlayingMovies(BuildConfig.TMDB_API_KEY);


        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();

                    if (movieResponse != null && movieResponse.getMovies() != null) {
                        List<Movie> movies = movieResponse.getMovies();

                        MovieAdapter movieAdapter = new MovieAdapter(movies);
                        recyclerView.setAdapter(movieAdapter);
                    } else {
                        Log.d("MOVIES", "Error: null or empty movie list");
                    }

                } else {
                    Log.d("MOVIES", "Error: null or empty movie list");
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("MOVIES", "could not load movies");
            }
        });
    }
}


