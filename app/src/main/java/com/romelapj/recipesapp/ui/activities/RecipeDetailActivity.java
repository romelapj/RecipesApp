package com.romelapj.recipesapp.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Step;
import com.romelapj.recipesapp.ui.fragments.RecipeDetailFragment;

import java.util.Collections;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {


    public static final String ARG_ITEM_STEP_ID = "item_id";
    public static final String ARG_ITEM_STEPS = "item_steps";

    String currentStepId = "";

    List<Step> steps = Collections.emptyList();

    ImageView backImageView;
    ImageView nextImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        backImageView = findViewById(R.id.backImageView);
        nextImageView = findViewById(R.id.nextImageView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {


            currentStepId = getIntent().getStringExtra(ARG_ITEM_STEP_ID);

            steps = getIntent().getParcelableArrayListExtra(ARG_ITEM_STEPS);

            drawStep(getCurrentPosition());

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            backImageView.setVisibility(View.GONE);
            nextImageView.setVisibility(View.GONE);
        }
    }

    public void nextStep(View view) {
        drawStep(getCurrentPosition() + 1);
    }

    public void backStep(View view) {
        drawStep(getCurrentPosition() - 1);
    }

    private void drawStep(int currentPosition) {

        if (currentPosition >= 0 && currentPosition < steps.size()) {
            Step step = steps.get(currentPosition);
            addRecipeDetailFragment(step);
            backImageView.setVisibility(currentPosition == 0 ? View.INVISIBLE : View.VISIBLE);
            nextImageView.setVisibility(currentPosition == steps.size() - 1 ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private int getCurrentPosition() {
        for (int i = 0; i < steps.size(); i++) {
            if (currentStepId.equals(steps.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    private void addRecipeDetailFragment(Step step) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        currentStepId = step.getId();
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_STEP, step);
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_detail_container, fragment)
                .commit();
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
