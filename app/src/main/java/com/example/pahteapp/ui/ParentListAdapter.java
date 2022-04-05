package com.example.pahteapp.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.UserList;

import java.util.List;

public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.ParentViewHolder> {

    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<UserList> itemList;
    private Context context;
    private LayoutInflater mInflater;

    private Integer movieID;


    ParentListAdapter(Context context, List<UserList> itemList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.context = context;
    }

    public void setMovieID(Integer id){
        movieID = id;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // Here we inflate the corresponding
        // layout of the parent item
        View view = mInflater.inflate(R.layout.list_movie, viewGroup, false);

        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder parentViewHolder, int position) {

        if(movieID != -1){
            parentViewHolder.imgButton.setVisibility(View.VISIBLE);
        }

        // Create an instance of the ParentItem
        // class for the given position
        UserList parentItem = itemList.get(position);

        // For the created instance,
        // get the title and set it
        // as the text for the TextView
        parentViewHolder.ParentItemTitle.setText(parentItem.getName());

        // Create a layout manager
        // to assign a layout
        // to the RecyclerView.

        // Here we have assigned the layout
        // as LinearLayout with vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentViewHolder.ChildRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(parentItem.getItemCount());

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        ChildListAdapter childItemAdapter = new ChildListAdapter(parentItem.getMovies());
        parentViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        parentViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
        parentViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
    }

    public void setUserList(List<UserList> userList) {
        this.itemList = userList;
        Log.d("UserListLast", itemList.toString());
        notifyDataSetChanged();
    }

    // This method returns the number
    // of items we have added in the
    // ParentItemList i.e. the number
    // of instances we have created
    // of the ParentItemList
    @Override
    public int getItemCount()
    {

        return itemList.size();
    }

    // This class is to initialize
    // the Views present in
    // the parent RecyclerView
    class ParentViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView imgButton;
        private TextView ParentItemTitle;

        private RecyclerView ChildRecyclerView;

        ParentViewHolder(final View itemView)
        {
            super(itemView);

            imgButton = itemView.findViewById(R.id.ConnectMovieToListButton);
            ParentItemTitle = itemView.findViewById(R.id.listName);
            ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
    }
}
