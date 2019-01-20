package com.romelapj.recipesapp.ui.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.ui.adapters.GenericAdapterRecyclerView;

public class RecipeView extends CardView implements GenericAdapterRecyclerView.ItemView<Recipe> {

    private TextView textView;
    private Recipe itemRecipe;

    public RecipeView(Context context) {
        super(context);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_recipe, this, true);
        MarginLayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 15, 30, 15);
        setLayoutParams(layoutParams);
        textView = findViewById(R.id.textViewName);
    }


    @Override
    public void bind(Recipe item, int position) {
        itemRecipe = item;
        textView.setText(item.getName());
    }

    @Override
    public void setItemClickListener(GenericAdapterRecyclerView.OnItemClickListener onItemClickListener) {

    }

    @Override
    public int getIdForClick() {
        return 0;
    }

    @Override
    public Recipe getData() {
        return itemRecipe;
    }

}
