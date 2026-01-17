package com.thedach.moves;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private final String TAG = "MovieDetailViewModel";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<Trailer>> trailersList = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private final MovieDao movieDao;

    private boolean isDataLoaded = false;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<List<Trailer>> getTrailersList() {
        return trailersList;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<Movie> getFavouriteMovie(Movie movie) {
        return movieDao.getFavouriteMovie(movie.getId());
    }

    public void loadData(Movie movie) {
        if (!isDataLoaded) {
            loadTrailers(movie);
            loadReviews(movie);
            isDataLoaded = true;
        }
    }

    public void insertFavouriteMovie(Movie movie) {
        Disposable disposable = movieDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void removeFavouriteMovie(Movie movie) {

        Disposable disposable = movieDao.removeMovie(movie.getId())
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    private void loadTrailers(Movie movie) {
        if (movie.getTrailersList() != null) {
            trailersList.setValue(movie.getTrailersList().getTrailers());
        } else {
            trailersList.setValue(null);
        }
    }

    private void loadReviews(Movie movie) {
        Disposable disposable = ApiFactory.apiService.loadReviews(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /// map - используется для предотвращения вызова несколько раз методов для
                /// получения спискаили чего то ещё по необходимости
                .map(new Function<ReviewResponse, List<Review>>() {
                    @Override
                    public List<Review> apply(ReviewResponse reviewResponse) throws Throwable {
                        return reviewResponse.getReviewList();
                    }
                })
                .subscribe(new Consumer<List<Review>>() {
                    @Override
                    public void accept(List<Review> reviewsList) throws Throwable {
                        reviews.setValue(reviewsList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "Error: " + throwable.toString());
                    }
                });

        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
