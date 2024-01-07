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
import com.example.cinemaapp2.R;
import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;

import java.util.ArrayList;

import com.example.cinemaapp2.connectwithsql.DBHandler;
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
    ArrayList<Integer> ListIds;
    ArrayList<Integer> LaterIds;
    Movie movie;
    ArrayList<Movie> watchLaterMovies;
    ArrayList<Movie> watchListMovies;
    RecyclerView watchList;
    RecyclerView watchLater;
    MovieAdapter watchListAdapter;
    MovieAdapter watchLaterAdapter;
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

        watchListMovies = new ArrayList<>();
        watchLaterMovies = new ArrayList<>();

        watchList = root.findViewById(R.id.watchList);
        GridLayoutManager layoutManagerWatchList = new GridLayoutManager(this.getContext(), 4);
        watchList.setLayoutManager(layoutManagerWatchList);

        watchLater = root.findViewById(R.id.watchLater);
        GridLayoutManager layoutManagerWatchLater = new GridLayoutManager(this.getContext(), 4);
        watchLater.setLayoutManager(layoutManagerWatchLater);

        // Set up the adapter before calling displayMovies
        watchListAdapter = new MovieAdapter(watchListMovies, this.getContext());
        watchList.setAdapter(watchListAdapter);

        watchLaterAdapter = new MovieAdapter(watchLaterMovies, this.getContext());
        watchLater.setAdapter(watchLaterAdapter);

        watchList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("Long press", "Long press activated");
                return false;
            }
        });

        displayMovies();

        // Use the already inflated view
        return root;
    }

    public void displayMovies(){

        ListIds = new ArrayList<>();
        Cursor cursor = myDB.getAllWatchListIds();
        if (cursor != null){
            if (cursor.getCount() == 0){
                Toast.makeText(this.getContext(), "No data", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    ListIds.add(cursor.getInt(0));
                }
            }
        }
        Retrofit retrofit = ApiClient.getClient();
        RequestMovie movieService = retrofit.create(RequestMovie.class);

        for (Integer id: ListIds){
            Call<Movie> call = movieService.getMovieById(id, BuildConfig.TMDB_API_KEY);

            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movie = response.body();
                    if (movie != null){
                        watchListMovies.add(movie);
                        watchListAdapter.notifyDataSetChanged();
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
        watchListAdapter = new MovieAdapter(watchListMovies, this.getContext());
        watchList.setAdapter(watchListAdapter);

        LaterIds = new ArrayList<>();

        LaterIds = new ArrayList<>();
        Cursor cursor2 = myDB.getAllWatchLaterIds();
        if (cursor2 != null){
            if (cursor2.getCount() == 0){
                Toast.makeText(this.getContext(), "No data", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor2.moveToNext()) {
                    LaterIds.add(cursor2.getInt(0));
                }
            }
        }

        for (Integer id: LaterIds){
            Call<Movie> call = movieService.getMovieById(id, BuildConfig.TMDB_API_KEY);

            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movie = response.body();
                    if (movie != null){
                        watchLaterMovies.add(movie);
                        watchLaterAdapter.notifyDataSetChanged();
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

        watchLaterAdapter = new MovieAdapter(watchLaterMovies, this.getContext());
        watchLater.setAdapter(watchLaterAdapter);
    }
}