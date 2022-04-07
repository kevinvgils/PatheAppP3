package com.example.pahteapp.ui.list;

import static com.example.pahteapp.ui.login.login.SESSION_ID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pahteapp.R;
import com.example.pahteapp.dataaccess.ApiClient;
import com.example.pahteapp.dataaccess.ApiInterface;
import com.example.pahteapp.domain.login.Authenticate;
import com.example.pahteapp.domain.movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MovieViewHolder> {

    private List<Movie> ChildItemList;
    private Integer listId;

    // Constructor
    ChildListAdapter(List<Movie> childItemList, Integer listId)
    {
        this.ChildItemList = childItemList;
        this.listId = listId;
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
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieImage;
        TextView title;
        TextView desc;
        Button delete;



        MovieViewHolder(View itemView)
        {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageView_movie);
            title = itemView.findViewById(R.id.listMovieTitle);
            desc = itemView.findViewById(R.id.listMovieDescription);
            delete = itemView.findViewById(R.id.deleteMovie);

            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<Authenticate> call = apiInterface.deleteMovieFromList(listId, "1e2c1f57cbed4d3e0c5dcad5996f2649", SESSION_ID, ChildItemList.get(getAdapterPosition()).getId());

            call.enqueue(new Callback<Authenticate>() {
                @Override
                public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                    if (!response.isSuccessful()) return;
                    Intent intent = new Intent(view.getContext(), UserListActivity.class);
                    view.getContext().startActivity(intent);
                    Toast.makeText(view.getContext(), "Deleted: " + ChildItemList.get(getAdapterPosition()).getTitle() + " from list", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<Authenticate> call, Throwable t) {
                    Log.d("FAIL", "failed");
                }
            });
        }
    }
}
