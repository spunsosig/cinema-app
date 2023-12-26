package com.example.cinemaapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.api.RequestTVShow;
import com.example.cinemaapp2.models.Genre;
import com.example.cinemaapp2.models.GenreResponse;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.Person;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MovieDetailsActivity extends AppCompatActivity {
    Retrofit retrofit = ApiClient.getClient();
    RequestMovie movieService = retrofit.create(RequestMovie.class);
    RequestTVShow TVService = retrofit.create(RequestTVShow.class);
    List<Genre> allGenres = new ArrayList<>();
    List<Genre> movieGenres = new ArrayList<>();
    Movie movie;
    Person person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_details);


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

        Call<Movie> call = movieService.getMovieById(movieId, BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movie = response.body();

                    if (movie != null) {
                        if (movieGenres == null || movieGenres.isEmpty()) {
                            updateUI(movie);
                        }
                    } else {
                        Log.e("MovieDetailsActivity", "No movie with this ID");
                        updateUI(movie);
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


        TextView titleTextView = findViewById(R.id.textTitle);
        titleTextView.setText(movie.getTitle());

        ImageView posterImage = findViewById(R.id.imagePoster);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).into(posterImage);

        TextView voteTextView = findViewById(R.id.textVote);
        voteTextView.setText(String.valueOf("Rating: " + movie.getVoteAverage()));

        TextView releaseDate = findViewById(R.id.textRelease);
        releaseDate.setText("Release: " + movie.getReleaseDate());

        TextView genreView = findViewById(R.id.textGenres);

        genreView.setText("");
        allGenres = getGenres();

        int[] movieGenreIds = movie.getGenre();

        if (movieGenreIds != null){
            for (int genreId: movieGenreIds){
                Genre genre = Genre.getGenreById(genreId, allGenres);
                genreView.append(" " + genre.getName());
            }
        } else {
            Log.d("GenreIds", "null");
            genreView.setText("Genres not available");
        }

        TextView overview = findViewById(R.id.textDescription);
        overview.setText(movie.getOverview());

    }


    public List<Genre> getGenres() {
        Call<GenreResponse> genreCall = movieService.getMovieGenres("en", BuildConfig.TMDB_API_KEY);

        genreCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                GenreResponse genreResponse = response.body();
                allGenres = genreResponse.getGenres();

                updateUI(movie);
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                Log.d("API", "failed to connect!", t);
            }
        });

        return allGenres;
    }
}