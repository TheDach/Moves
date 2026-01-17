package com.thedach.moves;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private final String TAG = "MainViewModel";

    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int page = 1;

    /// Объект ViewModel живет дольше чем активити, поэтому что бы не загружать
    /// данныее (вызывать loadMovies), когда мы переворачиваем телефон переносим в конструктор
    public MainViewModel(@NonNull Application application) {
        super(application);
        loadMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadMovies() {
        Boolean loading = isLoading.getValue();
        /// Для того что бы не загружалось много страниц при скролинге в самый конец,
        /// так как ещё не подгрузилась страница следующая
        if (loading != null && loading) {
            return;
        }

        Disposable disposable = ApiFactory.apiService.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Throwable {
                        List<Movie> loadMovies = movies.getValue();

                        if(loadMovies != null) {
                            /// Сохраняем значени из LiveData, иначе они будут загружаться новые, а
                            /// старые будут затираться новыми, поэтому мы берем старые из LiveData
                            /// и сохраняем их, после чего добавляем после них новые полученые
                            loadMovies.addAll(movieResponse.getMovies());
                            movies.setValue(loadMovies);
                        } else {
                            /// Для самой первой загрузки данных
                            movies.setValue(movieResponse.getMovies());
                        }
                        Log.d(TAG, "Loading: " + page);
                        page++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "Error: " + Arrays.toString(throwable.getStackTrace()));
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
