package com.example.cinemaapp2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp2.BuildConfig;
import com.example.cinemaapp2.MainActivity;
import com.example.cinemaapp2.R;
import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.api.RequestTVShow;
import com.example.cinemaapp2.databinding.FragmentHomeBinding;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;
import com.example.cinemaapp2.models.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Movie> popularMovies = new ArrayList<Movie>();
    List<Movie> upcomingMovies = new ArrayList<Movie>();
    List<Movie> nowPlayingMovies = new ArrayList<Movie>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);
        RequestTVShow TVService = retrofit.create(RequestTVShow.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        Button btnPopular = root.findViewById(R.id.btnPopular);
        Button btnNowplaying = root.findViewById(R.id.btnNowPlaying);
        Button btnUpcoming = root.findViewById(R.id.btnUpcoming);

        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        Call<MovieResponse> nowPlayingCall =  movieService.getNowPlayingMovies(BuildConfig.TMDB_API_KEY);
        nowPlayingCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    MovieResponse movieResponse = response.body();

                    if (movieResponse != null && movieResponse.getMovies() != null){
                        nowPlayingMovies = movieResponse.getMovies();
//                        Log.d("Now Playing: ", nowPlayingMovies.toString());
                    } else {
                        Log.d("MOVIES", "Error: null or empty movie list");

                    }
                } else {
                    Log.d("API", "Connection unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("API", "Failed to connect");
            }
        });



        Call<MovieResponse> popularCall = movieService.getPopularMovies(BuildConfig.TMDB_API_KEY);
        popularCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    MovieResponse movieResponse = response.body();

                    if (movieResponse != null && movieResponse.getMovies() != null){
                        popularMovies = movieResponse.getMovies();

                        MovieAdapter movieAdapter = new MovieAdapter(popularMovies,HomeFragment.this.getContext());
                        recyclerView.setAdapter(movieAdapter);
                    } else {
                        Log.d("MOVIES", "Error: null or empty movie list");
                    }
                } else {
                    Log.d("API", "Connection unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("API", "Failed to connect");
            }
        });



        Call<MovieResponse> upComingCall = movieService.getUpComingMovies(BuildConfig.TMDB_API_KEY);
        upComingCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    Log.d("Upcoming Movies call", "movie response successful");

                    if (movieResponse != null && movieResponse.getMovies() != null){
                        upcomingMovies = movieResponse.getMovies();
                        Log.d("Upcoming movies", String.valueOf(upcomingMovies.get(6).getId()));

                        MovieAdapter movieAdapter = new MovieAdapter(upcomingMovies,HomeFragment.this.getContext());
                        recyclerView.setAdapter(movieAdapter);
                    } else {

                    }
                } else {
                    Log.d("API", "Upcoming Connection unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("API", "Failed to connect", t);
            }
        });



        btnUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieAdapter movieAdapter = new MovieAdapter(upcomingMovies,HomeFragment.this.getContext());
                recyclerView.setAdapter(movieAdapter);

                btnUpcoming.setPressed(true);
                Log.d("UPCOMING MOVIES", "Error: null or empty movie list");
                Toast.makeText(getContext(), "Showing Upcoming movies", Toast.LENGTH_SHORT).show();

            }
        });

        btnNowplaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieAdapter movieAdapter = new MovieAdapter(nowPlayingMovies,HomeFragment.this.getContext());
                recyclerView.setAdapter(movieAdapter);
                Toast.makeText(getContext(), "Showing Now playing movies", Toast.LENGTH_SHORT).show();

            }
        });

        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieAdapter movieAdapter = new MovieAdapter(popularMovies,HomeFragment.this.getContext());
                recyclerView.setAdapter(movieAdapter);
                Log.d("Popular Movies", popularMovies.toString());
                Toast.makeText(getContext(), "Showing Popular movies", Toast.LENGTH_SHORT).show();

            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}