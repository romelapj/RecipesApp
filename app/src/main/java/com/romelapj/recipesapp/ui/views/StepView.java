package com.romelapj.recipesapp.ui.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romelapj.recipesapp.R;
import com.romelapj.recipesapp.models.Step;
import com.romelapj.recipesapp.ui.adapters.GenericAdapterRecyclerView;

public class StepView extends CardView implements GenericAdapterRecyclerView.ItemView<Step> {

    private TextView textView;
    private Step itemStep;

    public StepView(Context context) {
        super(context);
        init();
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_step, this, true);
        MarginLayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 10, 30, 10);
        setLayoutParams(layoutParams);
        textView = findViewById(R.id.textView_name);
    }


    @Override
    public void bind(Step item, int position) {
        itemStep = item;
        textView.setText(item.getShortDescription());
    }

    @Override
    public void setItemClickListener(final GenericAdapterRecyclerView.OnItemClickListener onItemClickListener) {
        findViewById(R.id.layout_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(StepView.this);
            }
        });
    }

    @Override
    public int getIdForClick() {
        return 0;
    }

    @Override
    public Step getData() {
        return itemStep;
    }

}
