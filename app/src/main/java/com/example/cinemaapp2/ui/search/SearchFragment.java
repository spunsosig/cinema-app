package com.example.cinemaapp2.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.cinemaapp2.models.Genre;
import com.example.cinemaapp2.models.GenreResponse;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;
import com.example.cinemaapp2.models.MovieResponse;
import com.example.cinemaapp2.ui.dashboard.DashboardViewModel;
import com.example.cinemaapp2.ui.home.HomeFragment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {

    private List<Genre> genres;
    private FragmentSearchBinding binding;
    int selectedGenreId = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);
        RequestTVShow TVService = retrofit.create(RequestTVShow.class);


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Movie> searchResults = new ArrayList<Movie>();

        //Movie Filters
        String selectedGenreName = "action";
        Genre selectedGenre = new Genre();

        RecyclerView recyclerView = root.findViewById(R.id.movieSearch);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        Call<GenreResponse> genreCall = movieService.getMovieGenres("en" ,BuildConfig.TMDB_API_KEY);


        genreCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response){
                if(response.isSuccessful()){
                    GenreResponse genreResponse = response.body();
                    genres = genreResponse.getGenres();
                    List<String> genreNames = new ArrayList<>();

                    for(Genre genre: genres){
                        genreNames.add(genre.getName());
                        Log.d("GENRES", genre.getName());
                    }
                    Spinner genreView = root.findViewById(R.id.spinner);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchFragment.this.getContext(), android.R.layout.simple_spinner_item, genreNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    genreView.setAdapter(adapter);

                    genreView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // Handle the item selection here
                            Genre selectedGenre = genres.get(position);
                            String selectedGenreName = selectedGenre.getName();
                            selectedGenreId = selectedGenre.getId();

                            Log.d("SELECTED_GENRE", String.valueOf(selectedGenreId));

                            Log.d("SELECTED_GENRE", selectedGenreName);

                            Call<MovieResponse> call = movieService.getMoviesWithGenre(selectedGenre.getName(),BuildConfig.TMDB_API_KEY);
                            call.enqueue(new Callback<MovieResponse>() {
                                @Override
                                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                                    MovieResponse movieResponse = response.body();
                                    if(movieResponse != null){
                                        List<Movie> movies = movieResponse.getMovies();
                                        for (Movie movie : movies) {
                                            Log.d("Results", movie.getTitle());
                                        }
                                    } else {
                                        Log.d("Results", "Response is null");
                                    }
                                }

                                @Override
                                public void onFailure(Call<MovieResponse> call, Throwable t) {
                                    Log.d("GENRECALL", "Error getting response");
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Do nothing here, or add any default behavior if needed
                        }
                    });

                } else {
                    Log.e("APIGENRE", "Error: " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                Log.d("APIGENRE","API failed to connect", t);
            }

        });

        TextView numOfResults = root.findViewById(R.id.resultsText);
        EditText searchbar = root.findViewById(R.id.search_bar);
        Button button = (Button) binding.button;
        String language = "en";
        int page = 1;
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String query = searchbar.getText().toString();
                String appendToResponse = "";
                Log.d("SUBMIT", query);

                Call<MovieResponse> searchCall = movieService.searchMovie(query ,BuildConfig.TMDB_API_KEY, appendToResponse);
                searchCall.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        Log.d("API","API successfully connected");
                        if (response.isSuccessful()){
                            MovieResponse searchResponse = response.body();

                            if (searchResponse != null && !searchResponse.getMovies().isEmpty()){
                                List<Movie> searchResults = searchResponse.getMovies();

                                for (Movie movie: searchResults){
                                    Log.d("Results", movie.getTitle());
                                }

                                Call<MovieResponse> filterCall = movieService.getFilteredMovies(language ,selectedGenreName, page ,BuildConfig.TMDB_API_KEY, query);

                                filterCall.enqueue(new Callback<MovieResponse>() {
                                    @Override
                                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                                        if (response.isSuccessful()){
                                            MovieResponse movieResponse = response.body();

                                            if (movieResponse != null){
                                                List<Movie> allMovies = movieResponse.getMovies();
                                                List<Movie> filteredMovies = new ArrayList<Movie>();

                                                for(Movie movie: searchResults){
                                                    int[] genreIds = movie.getGenre();
                                                    for (int genreId: genreIds){
                                                        if (genreId == selectedGenreId){
                                                            filteredMovies.add(movie);

                                                        }
                                                        Log.d("Movie genre", String.valueOf(genreId));
                                                        Log.d("Target genre", String.valueOf(selectedGenreId));
                                                    }
                                                }
                                                numOfResults.setText(filteredMovies.size() + " Results for " + query);
                                                MovieAdapter movieAdapter = new MovieAdapter(filteredMovies, SearchFragment.this.getContext());

                                                recyclerView.setAdapter(movieAdapter);

                                            }
                                            Log.d("Response", "No response");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                                        Log.d("error","Failed to connect");
                                    }
                                });
                            }
                        } else {
                            Log.d("SEARCH","Response not successful  " + response.errorBody().toString());
                            numOfResults.setText("No results found for " + query);
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
