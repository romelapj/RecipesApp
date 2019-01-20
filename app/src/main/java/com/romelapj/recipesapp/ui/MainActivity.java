package com.romelapj.recipesapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.ui.adapters.GenericAdapterRecyclerView;
import com.romelapj.recipesapp.ui.adapters.GenericItemModel;
import com.romelapj.recipesapp.ui.adapters.ViewFactory;
import com.romelapj.recipesapp.ui.views.RecipeView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MainViewModel viewModel = new MainViewModel();
    GenericAdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclerView();
        viewModel.loadRecipes();
    }

    private void initRecyclerView() {
        RecyclerView recyclerViewRecipes = findViewById(R.id.recyclerView_recipes);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapterRecyclerView(new ViewFactory<GenericAdapterRecyclerView.ItemView<Recipe>>() {
            @Override
            public GenericAdapterRecyclerView.ItemView<Recipe> getView(ViewGroup parent, int viewType) {
                return new RecipeView(parent.getContext());
            }
        });
        recyclerViewRecipes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindSubscription();
        viewModel.loadRecipes();
    }

    private void bindSubscription() {
        compositeDisposable.add(
                viewModel.actionsSubject.subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> recipes) {
                        int limit = recipes.size();
                        List<GenericItemModel> items = new ArrayList<>(limit);
                        for (int i = 0; i < limit; i++) {
                            items.add(new GenericItemModel<>(recipes.get(i), 0));
                        }
                        adapter.addItems(items);
                    }
                })
        );
    }

    @Override
    protected void onPause() {
        compositeDisposable.clear();
        super.onPause();
    }
}
