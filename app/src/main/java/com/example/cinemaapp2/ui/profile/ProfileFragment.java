package com.example.cinemaapp2.ui.profile;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.cinemaapp2.ui.home.HomeFragment;

import java.util.ArrayList;

import connectwithsql.DBHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    DBHandler myDB;
    ArrayList<Integer> movieIds;
    Movie movie;
    ArrayList<Movie> movies;
    RecyclerView watchList;
    RecyclerView watchLater;
    MovieAdapter movieAdapter;
    View root;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile, container, false);

        Retrofit retrofit = ApiClient.getClient();
        myDB = new DBHandler(this.getContext());

        movies = new ArrayList<>();

        watchList = root.findViewById(R.id.watchList);
        GridLayoutManager layoutManagerWatchList = new GridLayoutManager(this.getContext(), 5);
        watchList.setLayoutManager(layoutManagerWatchList);

        // Set up the adapter before calling displayMovies
        movieAdapter = new MovieAdapter(movies, this.getContext());
        watchList.setAdapter(movieAdapter);

        displayMovies();

        // Use the already inflated view
        return root;
    }


    public void displayMovies(){

        movieIds = new ArrayList<>();
        Cursor cursor = myDB.getAllMovieIds();
        if (cursor != null){
            if (cursor.getCount() == 0){
                Toast.makeText(this.getContext(), "No data", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    movieIds.add(cursor.getInt(0));
                }
            }
        }
        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);

        for (Integer id: movieIds){
            Call<Movie> call = movieService.getMovieById(id, BuildConfig.TMDB_API_KEY);

            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movie = response.body();
                    if (movie != null){
                        movies.add(movie);
                        movieAdapter.notifyDataSetChanged();
                        Log.d("MYMOVIES", movie.getTitle());

                    } else {
                        Log.d("MOVIE", "MOVIE WITH THIS ID DOES NOT EXIST");
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.d("PROFILE", "API FAILURE", t);
                }
            });


        }
        movieAdapter = new MovieAdapter(movies, this.getContext());
        watchList.setAdapter(movieAdapter);

    }
}