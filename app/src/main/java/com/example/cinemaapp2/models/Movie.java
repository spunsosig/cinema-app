package com.example.cinemaapp2.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Movie {
    @SerializedName("id")
    private int id;
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("genre_ids")
    private int[] genreIds;
    @SerializedName("genres")
    private List<Genre> genres = new List<Genre>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Genre> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Genre genre) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Genre> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends Genre> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Genre get(int index) {
            return null;
        }

        @Override
        public Genre set(int index, Genre element) {
            return null;
        }

        @Override
        public void add(int index, Genre element) {

        }

        @Override
        public Genre remove(int index) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<Genre> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Genre> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<Genre> subList(int fromIndex, int toIndex) {
            return null;
        }
    };


    public Movie(int id, List<Genre> genres){
//        this.external_id = id;
        this.id = id;
        this.genres = genres;
    }

    public Movie getMovieById(int id, ArrayList<Movie> movies){
        for (Movie movie: movies){
            if (id == movie.getId()){
                return movie;
            }
        }
        return null;
    }
    public List<Genre> getGenre() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    @Override
    public String toString(){
        return getTitle() + " { " + getId() + " " + getGenre() + " " + getOverview() + " ";
    }

}
