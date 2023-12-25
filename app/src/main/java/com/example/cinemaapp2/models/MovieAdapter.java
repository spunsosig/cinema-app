package com.example.cinemaapp2.models;

import com.example.cinemaapp2.MovieDetailsActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movies;
    private Context context;
    private int movieId = 0;


    public MovieAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String posterPath = movie.getPosterPath();

        if (movie != null && posterPath != null) {
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterPath).into(holder.movieImageView);
            Log.d("posterpath",posterPath);
            movieId = movie.getId();
            Log.d("MovieIds", String.valueOf(movieId));

            holder.movieImageView.setOnClickListener(view -> {
                Log.d("ONCLICK", "Movie Clicked");
                Log.d("MovieAdapter", "Context: " + context);
                movieId = movie.getId();
                Log.d("MovieIds", String.valueOf(movieId + " " + movie.getTitle()));

                // Create an intent to start the MovieDetailsActivity
                Intent intent = new Intent(context, MovieDetailsActivity.class);

                // Pass data (for example, movie ID) to the MovieDetailsActivity
                intent.putExtra("movieId", movieId);

                // Start the activity
                context.startActivity(intent);
            });
        }
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.movieImageView);

        }
    }
}
