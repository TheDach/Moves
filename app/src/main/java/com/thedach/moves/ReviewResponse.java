package com.thedach.moves;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {

    @SerializedName("docs")
    private List<Review> reviewList;

    public ReviewResponse(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    @Override
    public String toString() {
        return "ReviewResponse{" +
                "reviewList=" + reviewList +
                '}';
    }
}
