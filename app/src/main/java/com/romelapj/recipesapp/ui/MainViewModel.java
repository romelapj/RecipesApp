package com.romelapj.recipesapp.ui;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.rest.RecipesApi;
import com.romelapj.recipesapp.rest.RetrofitClientInstance;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PublishSubject<List<Recipe>> actionsSubject = PublishSubject.create();

    private RecipesApi recipesApi;


    public MainViewModel() {
        this.recipesApi = RetrofitClientInstance.create();
    }

    public void loadRecipes() {
        compositeDisposable.add(recipesApi.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> recipes) {
                        actionsSubject.onNext(recipes);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("Main", throwable.getMessage());
                    }
                }));
    }
}
