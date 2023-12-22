package com.example.cinemaapp2.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp2.BuildConfig;
import com.example.cinemaapp2.R;
import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.api.RequestTVShow;
import com.example.cinemaapp2.databinding.FragmentSearchBinding;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;
import com.example.cinemaapp2.models.MovieResponse;
import com.example.cinemaapp2.ui.dashboard.DashboardViewModel;
import com.example.cinemaapp2.ui.home.HomeFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);
        RequestTVShow TVService = retrofit.create(RequestTVShow.class);


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView = root.findViewById(R.id.movieSearch);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        Spinner genreView = root.findViewById(R.id.spinner);
        Call<MovieResponse> genreCall = movieService.getMovieGenres("en" ,BuildConfig.TMDB_API_KEY);

        genreCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> genreCall, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();

                Log.d("API","API successfully connected");
                if (response.isSuccessful()){

                } else {

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> genreCall, Throwable t) {
                Log.d("API","API failed to connect");

            }
        });

        TextView numOfResults = root.findViewById(R.id.resultsText);
        EditText searchbar = root.findViewById(R.id.search_bar);
        Button button = (Button) binding.button;
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String query = searchbar.getText().toString();
                Log.d("SUBMIT", query);

                Call<MovieResponse> call = movieService.searchMovie(query ,BuildConfig.TMDB_API_KEY);

                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        Log.d("API","API successfully connected");
                        MovieResponse movieResponse = response.body();
                        if (response.isSuccessful()){
                            if(query != ""){
                                numOfResults.setText(movieResponse.getTotalResults() + " Results for " + query);
                                List<Movie> movies = movieResponse.getMovies();
                                for(Movie movie: movies){
                                    Log.d("Results", movie.getTitle());
                                    MovieAdapter movieAdapter = new MovieAdapter(movies, SearchFragment.this.getContext());
                                    recyclerView.setAdapter(movieAdapter);
                                }
                            }
                        } else {
                            Log.d("SEARCH","No results found for: " + query);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d("API","API failed to connect");

                    }
                });
            }
        });

        final TextView textView = binding.textSearch;
        searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
