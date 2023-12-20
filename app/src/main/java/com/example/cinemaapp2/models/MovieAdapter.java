package com.example.cinemaapp2.models;

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

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
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

        if (posterPath != null && !posterPath.isEmpty()){
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterPath).into(holder.movieImageView);

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
