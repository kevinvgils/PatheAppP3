package com.example.pahteapp.ui;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pahteapp.R;
import com.example.pahteapp.domain.reviews.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> nReviewList;
    private LayoutInflater mInflater;
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviews) {
        mInflater = LayoutInflater.from(context);
        this.nReviewList = reviews;
        this.context = context;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public final TextView reviewUser;
        public final TextView reviewContent;
        private RatingBar userRating;
        public TextView reviewRating;

        final ReviewAdapter mAdapter;

        public ReviewViewHolder(View itemView, ReviewAdapter adapter) {
            super(itemView);
            reviewUser = itemView.findViewById(R.id.reviewUserName);
            userRating = itemView.findViewById(R.id.ratingBarReview);
            reviewContent = itemView.findViewById(R.id.reviewContent);
            reviewRating = itemView.findViewById(R.id.userRating);
            this.mAdapter = adapter;
        }
    }

    public void setReviewList(List<Review> reviewList) {
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
        holder.reviewContent.setText(Html.fromHtml(mCurrent.getContent()));
        if (mCurrent.getAuthorDetails().getRating() == 69) {
            holder.reviewRating.setText("No rating given");
        } else {
            holder.reviewRating.setText(mCurrent.getAuthorDetails().getRating() / 2 + "/5");
            holder.userRating.setRating((float) (mCurrent.getAuthorDetails().getRating() / 2));
        }
    }

    @Override
    public int getItemCount() {
        return nReviewList.size();
    }
}

