package com.example.pahteapp.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private LinkedList<Movie> nMovieList;
    private LayoutInflater mInflater;
    private Context context;

    public MovieAdapter(Context context, LinkedList<Movie> movieList) {
        mInflater = LayoutInflater.from(context);
        this.nMovieList = movieList;
        this.context = context;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        public final TextView movieTitle;
        public final ImageView imageView;
        final MovieAdapter mAdapter;

        public MovieViewHolder(View itemView, MovieAdapter adapter) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            imageView = itemView.findViewById(R.id.imageView);

            this.mAdapter = adapter;
        }
    }

    public void setMovieList(LinkedList<Movie> movieList) {
        this.nMovieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie mCurrent = nMovieList.get(position);
        holder.movieTitle.setText(mCurrent.getTitle());
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + mCurrent.getPosterPath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return nMovieList.size();
    }
}
