package com.example.pahteapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.UserList;


import java.util.LinkedList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private LinkedList<UserList> nUserList;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(Context context, LinkedList<UserList> userList) {
        mInflater = LayoutInflater.from(context);
        this.nUserList = userList;
        this.context = context;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        public final TextView listName;
        final ListAdapter mAdapter;

        public ListViewHolder(View itemView, ListAdapter adapter) {
            super(itemView);
            listName = itemView.findViewById(R.id.listName);

            this.mAdapter = adapter;
        }
    }

    public void setUserList(LinkedList<UserList> userList) {
        this.nUserList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_movie, parent, false);
        return new ListViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, int position) {
        UserList mCurrent = nUserList.get(position);
        holder.listName.setText(mCurrent.getName());
    }

    @Override
    public int getItemCount() {
        return nUserList.size();
    }
}
