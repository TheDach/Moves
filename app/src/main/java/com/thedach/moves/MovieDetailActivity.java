package com.thedach.moves;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = "MovieDetailActivity";

    private static final String EXTRA_MOVIE = "movie";

    private MovieDetailViewModel movieDetailViewModel;

    private ImageView imageViewPosterDetail;
    private ImageView imageViewStar;
    private TextView textViewMovieTitle;
    private TextView textViewMovieYear;
    private TextView textViewMovieDescription;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;

    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);

        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);


        trailersAdapter = new TrailersAdapter();
        recyclerViewTrailers.setAdapter(trailersAdapter);

        reviewsAdapter = new ReviewsAdapter();
        recyclerViewReviews.setAdapter(reviewsAdapter);




        movieDetailViewModel.getTrailersList().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                if (trailers == null || trailers.isEmpty()) {
                    recyclerViewTrailers.setVisibility(View.GONE);
                } else {
                    recyclerViewTrailers.setVisibility(View.VISIBLE);
                    trailersAdapter.setTrailers(trailers);
                }
            }
        });
        movieDetailViewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews == null || reviews.isEmpty()) {
                    recyclerViewReviews.setVisibility(View.GONE);
                } else {
                    recyclerViewReviews.setVisibility(View.VISIBLE);
                    reviewsAdapter.setReviews(reviews);
                }
            }
        });
        movieDetailViewModel.loadData(movie);

        Drawable starOff = ContextCompat.getDrawable(MovieDetailActivity.this, android.R.drawable.star_big_off);
        Drawable starOn = ContextCompat.getDrawable(MovieDetailActivity.this, android.R.drawable.star_big_on);
        /**
         * Здесь мы проверяем наличе FavouriteMovie в базе с помощью LiveData, если оно есть то ставим starOn иначе starOff
         * После того как установили цвет звезды, ставим на нее слушатель клинка, то есть если
         * на starOff нажемт пользователь, то нужно добавить в базу фильм,
         * иначе если starOn и пользователь нажал, удаляем его из базы
         */
        movieDetailViewModel.getFavouriteMovie(movie).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieFromDb) {
                if (movieFromDb == null) {
                    imageViewStar.setImageDrawable(starOff);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            movieDetailViewModel.insertFavouriteMovie(movie);
                        }
                    });
                } else {
                    imageViewStar.setImageDrawable(starOn);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            movieDetailViewModel.removeFavouriteMovie(movie);
                        }
                    });
                }
            }
        });


        Glide.with(this)
                .load(movie.getPoster().getUrl())
                .into(imageViewPosterDetail);
        textViewMovieTitle.setText(movie.getName());
        textViewMovieYear.setText(String.valueOf(movie.getYear()));
        textViewMovieDescription.setText(movie.getDescription());



        trailersAdapter.setOnTrailerClickListener(new TrailersAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(Trailer trailer) {
                // Неявный Intent; ACTION_VIEW - открывает адресс в интернет для просмотра
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        imageViewPosterDetail = findViewById(R.id.imageViewPosterDetail);
        imageViewStar = findViewById(R.id.imageViewStar);
        textViewMovieTitle = findViewById(R.id.textViewMovieTitle);
        textViewMovieYear = findViewById(R.id.textViewMovieYear);
        textViewMovieDescription = findViewById(R.id.textViewMovieDescription);

        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById((R.id.recyclerViewReviews));
    }

    /// Так как мы реализовали в Movie интерфейс Serializable
    /// То с помощью этого Java поймет как преобразовать класс Movie в поток байт и передатьв Intent
    public static Intent newIntent(
            Context context,
            Movie movie
    ) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}