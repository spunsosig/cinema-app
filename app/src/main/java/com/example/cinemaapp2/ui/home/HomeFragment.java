package com.example.cinemaapp2.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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

        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        //        Call<MovieResponse> call =  movieService.getNowPlayingMovies(BuildConfig.TMDB_API_KEY);
        //        Call<MovieResponse> call =  TVService.getPopularTV(BuildConfig.TMDB_API_KEY);
        Call<MovieResponse> call = movieService.getPopularMovies(BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();

                    if (movieResponse != null && movieResponse.getMovies() != null) {
                        List<Movie> movies = movieResponse.getMovies();

                        MovieAdapter movieAdapter = new MovieAdapter(movies,HomeFragment.this.getContext());
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}