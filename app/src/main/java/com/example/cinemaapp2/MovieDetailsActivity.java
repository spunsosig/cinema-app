package com.example.cinemaapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.databinding.ActivityMainBinding;
import com.example.cinemaapp2.models.Genre;
import com.example.cinemaapp2.models.GenreResponse;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.Person;
import com.example.cinemaapp2.models.PersonResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import connectwithsql.DBHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    Retrofit retrofit = ApiClient.getClient();
    RequestMovie movieService = retrofit.create(RequestMovie.class);
    List<Genre> allGenres = new ArrayList<>();
    List<Genre> movieGenres = new ArrayList<>();
    int[] movieGenreIds;
    Movie movie;
    List<Person> cast = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        setupBottomNavigationView();

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
        movieGenreIds = movie.getGenre();

        Call<GenreResponse> genreCall = movieService.getMovieGenres("en", BuildConfig.TMDB_API_KEY);

        genreCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                GenreResponse genreResponse = response.body();
                allGenres = genreResponse.getGenres();
                Log.d("MOVIEDETAILSACTIVITY", "GENRES" + allGenres.toString());
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                Log.d("API", "failed to connect!", t);
            }
        });

        if (movieGenreIds != null) {
            for (int genreId : movieGenreIds) {
                Genre genre = Genre.getGenreById(genreId, allGenres);
                genreView.append(" " + genre.getName());
            }
        } else {
            Log.d("GenreIds", "null");
            genreView.setText("Genres not available");
        }

        TextView overview = findViewById(R.id.textDescription);
        overview.setText(movie.getOverview());

        Call<PersonResponse> personResponseCall = movieService.getCast(movie.getId(), BuildConfig.TMDB_API_KEY);
        personResponseCall.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                PersonResponse personResponse = response.body();
                cast = personResponse.getCast();
                Log.d("MOVIEDETAILSACTIVITY", "ACTORS" + cast.toString());

                TextView actorName1 = findViewById(R.id.actorName1);
                actorName1.setText(cast.get(0).getName());
                TextView actorName2 = findViewById(R.id.actorName2);
                actorName2.setText(cast.get(1).getName());
                TextView actorName3 = findViewById(R.id.actorName3);
                actorName3.setText(cast.get(2).getName());
                TextView actorName4 = findViewById(R.id.actorName4);
                actorName4.setText(cast.get(3).getName());

                ImageView actorImage1 = findViewById(R.id.actorImage1);
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + cast.get(0).getProfile_path()).into(actorImage1);
                ImageView actorImage2 = findViewById(R.id.actorImage2);
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + cast.get(1).getProfile_path()).into(actorImage2);
                ImageView actorImage3 = findViewById(R.id.actorImage3);
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + cast.get(2).getProfile_path()).into(actorImage3);
                ImageView actorImage4 = findViewById(R.id.actorImage4);
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + cast.get(3).getProfile_path()).into(actorImage4);


            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
                Log.d("MOVIEDETAILSACTIVITY", "Could not retrieve cast");
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            Log.d("NavigationItemSelected", "Item ID: " + item.getItemId()); // Log the selected item ID
            if (item.getItemId() == R.id.navigation_home) {
                navigateToFragment(R.id.navigation_home);
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                navigateToFragment(R.id.navigation_dashboard);
                return true;
            } else if (item.getItemId() == R.id.navigation_notifications) {
                navigateToFragment(R.id.navigation_notifications);
                return true;
            } else if(item.getItemId() == R.id.navigation_search){
                navigateToFragment(R.id.navigation_search);
                return true;
            }
            return false;
        });

    }



    private void navigateToFragment(int destinationId) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            navController.navigate(destinationId);
            Log.d("Navigation", "navigate to " + destinationId);
        } else {
            Log.e("NavHostFragment", "NavHostFragment is null");
        }
    }


//    private void navigateToFragment(int destinationId) {
//        Intent intent = new Intent(this, MainActivity.class);  // Change MainActivity to your target activity
//        intent.putExtra("destinationId", destinationId);
//        startActivity(intent);
//        finish();  // Optional, depends on your navigation requirements
//    }


//    private void navigateToFragment(int destinationId) {
//        // Find the NavController by locating the NavHostFragment
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
//
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//            navController.navigate(destinationId);
//            Log.d("Navigation", "navigate to " + destinationId);
//        } else {
//            Log.e("NavHostFragment", "NavHostFragment is null");
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.watchList){
            DBHandler myDB = new DBHandler(this);
            myDB.addMovieToList(movie.getId(),
                    movie.getTitle(),
                    movie.getOverview(),
                    movie.getReleaseDate());
        }
        if (id == R.id.watchLater){
            DBHandler myDB = new DBHandler(this);
            myDB.addMovieToLater(movie.getId(),
                    movie.getTitle(),
                    movie.getOverview(),
                    movie.getReleaseDate());
        }
        if (id == R.id.removeFromList){
            DBHandler myDB = new DBHandler(this);
            myDB.removeMovie("watchList", movie.getId());
        }
        if (id == R.id.removeFromLater){
            DBHandler myDB = new DBHandler(this);
            myDB.removeMovie("watchLater", movie.getId());
        }


        return super.onOptionsItemSelected(item);
    }
}