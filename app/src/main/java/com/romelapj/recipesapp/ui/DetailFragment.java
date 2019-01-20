package com.romelapj.recipesapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.ui.adapters.GenericAdapterRecyclerView;
import com.romelapj.recipesapp.ui.adapters.GenericItemModel;
import com.romelapj.recipesapp.ui.adapters.ViewFactory;
import com.romelapj.recipesapp.ui.views.IngredientsView;

public class DetailFragment extends Fragment {

    public Recipe recipe;
    GenericAdapterRecyclerView adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recipe = getArguments().getParcelable("recipe");
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initRecyclerView(view);
        populateRecyclerView();
        return view;
    }

    private void populateRecyclerView() {
        adapter.addItem(new GenericItemModel<>(recipe, 0));
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerViewRecipes = view.findViewById(R.id.recyclerView_recipe);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GenericAdapterRecyclerView(new ViewFactory<GenericAdapterRecyclerView.ItemView<Recipe>>() {
            @Override
            public GenericAdapterRecyclerView.ItemView<Recipe> getView(ViewGroup parent, int viewType) {
                return new IngredientsView(parent.getContext());
            }
        });
        recyclerViewRecipes.setAdapter(adapter);
    }

    public static DetailFragment newInstance(Recipe recipe) {

        Bundle args = new Bundle();

        args.putParcelable("recipe", recipe);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
