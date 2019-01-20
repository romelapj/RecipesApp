package com.romelapj.recipesapp.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Recipe;
import com.romelapj.recipesapp.ui.adapters.GenericAdapterRecyclerView;

public class IngredientsView extends FrameLayout implements GenericAdapterRecyclerView.ItemView<Recipe> {

    private TextView textView;

    public IngredientsView(Context context) {
        super(context);
        init();
    }

    public IngredientsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IngredientsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_ingredients, this, true);
        MarginLayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 15, 30, 15);
        setLayoutParams(layoutParams);
        textView = findViewById(R.id.textView_ingredients);
    }


    @Override
    public void bind(Recipe item, int position) {
        textView.setText(item.getIngredientsDisplay());
    }

    @Override
    public void setItemClickListener(final GenericAdapterRecyclerView.OnItemClickListener onItemClickListener) {
        findViewById(R.id.layout_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(IngredientsView.this);
            }
        });
    }

    @Override
    public int getIdForClick() {
        return 0;
    }

    @Override
    public Recipe getData() {
        return null;
    }

}
