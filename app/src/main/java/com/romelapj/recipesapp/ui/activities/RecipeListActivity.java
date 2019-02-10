package com.romelapj.recipesapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.models.Step;
import com.romelapj.recipesapp.ui.adapters.GenericAdapterRecyclerView;
import com.romelapj.recipesapp.ui.adapters.GenericItemModel;
import com.romelapj.recipesapp.ui.adapters.ViewFactory;
import com.romelapj.recipesapp.ui.fragments.RecipeDetailFragment;
import com.romelapj.recipesapp.ui.views.IngredientsView;
import com.romelapj.recipesapp.ui.views.StepView;

import java.util.ArrayList;
import java.util.List;

import static com.romelapj.recipesapp.ui.activities.RecipeDetailActivity.ARG_ITEM_STEPS;
import static com.romelapj.recipesapp.ui.activities.RecipeDetailActivity.ARG_ITEM_STEP_ID;

public class RecipeListActivity extends AppCompatActivity implements GenericAdapterRecyclerView.OnItemClickListener {

    private boolean mTwoPane;
    private Recipe recipe;
    GenericAdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        recipe = getIntent().getParcelableExtra("recipe");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipe.getName());
        }


        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        initRecyclerView(recyclerView);
        populateRecyclerView();
    }


    private void populateRecyclerView() {
        List<Step> steps = recipe.getSteps();
        int limit = steps.size();
        List<GenericItemModel> items = new ArrayList<>(limit + 1);
        items.add(new GenericItemModel<>(recipe, 0));
        for (int i = 0; i < limit; i++) {
            items.add(new GenericItemModel<>(steps.get(i), 1));
        }
        adapter.addItems(items);
    }

    private void initRecyclerView(RecyclerView recyclerViewRecipes) {
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapterRecyclerView(new ViewFactory<GenericAdapterRecyclerView.ItemView<?>>() {
            @Override
            public GenericAdapterRecyclerView.ItemView<?> getView(ViewGroup parent, int viewType) {
                return (viewType == 1) ? getStepView() : new IngredientsView(parent.getContext());
            }
        });
        recyclerViewRecipes.setAdapter(adapter);
    }

    private GenericAdapterRecyclerView.ItemView<?> getStepView() {
        StepView stepView = new StepView(this);
        stepView.setItemClickListener(this);
        return stepView;
    }


    @Override
    public void onItemClicked(GenericAdapterRecyclerView.ItemView itemView) {
        if (itemView.getData() instanceof Step) {
            Step itemViewData = (Step) itemView.getData();

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_STEP, itemViewData);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(this, RecipeDetailActivity.class);
                intent.putExtra(ARG_ITEM_STEP_ID, itemViewData.getId());
                intent.putParcelableArrayListExtra(ARG_ITEM_STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());

                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemLongClicked(GenericAdapterRecyclerView.ItemView itemView) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
