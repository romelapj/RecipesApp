package com.romelapj.recipesapp.ui.adapters;

public class GenericItemModel<T> implements GenericAdapterRecyclerView.ItemModel<T> {

    private T data;
    private int type;

    public GenericItemModel(T data, int type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getViewType() {
        return type;
    }
}
