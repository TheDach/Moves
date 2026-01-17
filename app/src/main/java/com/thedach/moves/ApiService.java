package com.thedach.moves;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie?token=" +
            ApiFactory.API_KEY +
            "&limit=30&selectFields=id&selectFields=name&selectFields=description&selectFields=type&selectFields=year&selectFields=rating&selectFields=poster&selectFields=videos&sortField=votes.kp&sortType=-1&rating.kp=7-10")
    Single<MovieResponse> loadMovies(@Query("page") int page);

    @GET("review?token=" +
            ApiFactory.API_KEY +
            "&page=1&limit=10")
    Single<ReviewResponse> loadReviews(@Query("movieId") int movieId);
}
