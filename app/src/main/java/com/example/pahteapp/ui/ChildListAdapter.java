package com.example.pahteapp.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MovieViewHolder> {

    private List<Movie> ChildItemList;

    // Constructor
    ChildListAdapter(List<Movie> childItemList)
    {
        this.ChildItemList = childItemList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder childViewHolder, int position) {

        // Create an instance of the ChildItem
        // class for the given position
        Movie childItem = ChildItemList.get(position);

        // For the created instance, set title.
        // No need to set the image for
        // the ImageViews because we have
        // provided the source for the images
        // in the layout file itself
        childViewHolder.desc.setText(childItem.getOverview());
        childViewHolder.title.setText(childItem.getTitle());
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + childItem.getPosterPath()).into(childViewHolder.movieImage);
    }

    @Override
    public int getItemCount()
    {

        // This method returns the number
        // of items we have added
        // in the ChildItemList
        // i.e. the number of instances
        // of the ChildItemList
        // that have been created
        if (ChildItemList == null) return 0;
        return ChildItemList.size();
    }

    // This class is to initialize
    // the Views present
    // in the child RecyclerView
    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImage;
        TextView title;
        TextView desc;



        MovieViewHolder(View itemView)
        {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageView_movie);
            title = itemView.findViewById(R.id.listMovieTitle);
            desc = itemView.findViewById(R.id.listMovieDescription);

        }
    }
}
