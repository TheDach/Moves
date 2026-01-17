package com.thedach.moves;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private static final String TYPE_POSITIVE = "Позитивный";
    private static final String TYPE_NEGATIVE = "Негативный";
    private static final String TYPE_NEUTRAL = "Нейтральный";

    private List<Review> reviews = new ArrayList<>();

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.review_item,
                        parent,
                        false
                );

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.textViewReviewerAuthor.setText(review.getAuthor());
        holder.textViewReviewDescription.setText(review.getReview());

        /*
             Форматирую фоормат даты 2023-11-21T19:30:19.000Z
             в: 21-11-2003
         */
        Instant instant = Instant.parse(review.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withZone(ZoneId.systemDefault());
        String formattedDate = formatter.format(instant);
        holder.textViewDateReview.setText(formattedDate);

        String type = review.getType();
        int colorResId = android.R.color.holo_green_light;

        switch (type) {
            case TYPE_POSITIVE:
                colorResId = android.R.color.holo_green_light;
                break;
            case TYPE_NEUTRAL:
                colorResId = android.R.color.holo_orange_light;
                break;
            default:
                colorResId = android.R.color.holo_red_light;
                break;
        }

        int color = ContextCompat.getColor(holder.itemView.getContext(), colorResId);
        holder.linearLayoutReviewItem.setBackgroundColor(color);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewReviewerAuthor;
        private final TextView textViewDateReview;
        private final TextView textViewReviewDescription;
        private final LinearLayout linearLayoutReviewItem;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewReviewerAuthor = itemView.findViewById(R.id.textViewReviewerAuthor);
            textViewDateReview = itemView.findViewById(R.id.textViewDateReview);
            textViewReviewDescription = itemView.findViewById(R.id.textViewReviewDescription);
            linearLayoutReviewItem = itemView.findViewById(R.id.linearLayoutReviewItem);
        }
    }
}
