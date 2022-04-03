package com.example.pahteapp.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.Genre;
import com.example.pahteapp.domain.Movie;

import java.util.LinkedList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    private LinkedList<Genre> mGenreList;
    private LayoutInflater mInflater;
    private Context context;

    public FilterAdapter(Context context, LinkedList<Genre> genreList) {
        mInflater = LayoutInflater.from(context);
        mGenreList = new LinkedList<>();
        this.mGenreList = genreList;
        this.context = context;
    }

    @NonNull
    @Override
    public FilterAdapter.FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.filter_layout, parent, false);
        return new FilterViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        Genre mCurrent = mGenreList.get(position);
        holder.genreBox.setText(mCurrent.getName());
        holder.id = mCurrent.getId();
    }

    @Override
    public int getItemCount() {
        return mGenreList.size();
    }

    public void setMGenreList(LinkedList<Genre> genreList) {
        this.mGenreList = genreList;
    }

    class FilterViewHolder extends RecyclerView.ViewHolder{
        protected int id;
        public final CheckBox genreBox;


        public FilterViewHolder(View itemView, FilterAdapter adapter) {
            super(itemView);
            genreBox = itemView.findViewById(R.id.genreBox);
        }

    }
}
