package com.example.pahteapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.reviews.Review;

import java.util.LinkedList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private LinkedList<Review> nReviewList;
    private LayoutInflater mInflater;
    private Context context;

    public ReviewAdapter(Context context, LinkedList<Review> reviews) {
        mInflater = LayoutInflater.from(context);
        this.nReviewList = reviews;
        this.context = context;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public final TextView reviewUser;
        public final TextView reviewContent;
        public final TextView reviewRating;

        final ReviewAdapter mAdapter;

        public ReviewViewHolder(View itemView, ReviewAdapter adapter) {
            super(itemView);
            reviewUser = itemView.findViewById(R.id.reviewUserName);
            reviewContent = itemView.findViewById(R.id.reviewContent);
            reviewRating = itemView.findViewById(R.id.reviewRating);
            this.mAdapter = adapter;
        }
    }

    public void setReviewList(LinkedList<Review> reviewList) {
        this.nReviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.review_layout, parent, false);
        return new ReviewViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review mCurrent = nReviewList.get(position);
        holder.reviewUser.setText(mCurrent.getAuthor());
        holder.reviewContent.setText(mCurrent.getContent());
        holder.reviewRating.setText(mCurrent.getAuthorDetails().getRating());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

