package com.romelapj.recipesapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.models.Step;
import com.romelapj.recipesapp.ui.fragments.RecipeDetailFragment;
import com.romelapj.recipesapp.ui.fragments.StepFragment;

public class RecipeActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeDetailInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            getSupportActionBar().setTitle(recipe.getName());
            Fragment detailFragment = RecipeDetailFragment.newInstance(recipe);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.layout_container, detailFragment).commit();
        }
    }

    @Override
    public void onStepSelected(Step step) {
        Fragment detailFragment = StepFragment.newInstance(step);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layout_container, detailFragment).commit();
    }
}
