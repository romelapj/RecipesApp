package com.romelapj.recipesapp.ui.adapters;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class GenericAdapterDiffUtil extends DiffUtil.Callback {

    private List<? extends GenericAdapterRecyclerView.ItemModel> oldList;
    private List<? extends GenericAdapterRecyclerView.ItemModel> newList;

    public GenericAdapterDiffUtil(List<? extends GenericAdapterRecyclerView.ItemModel> oldList,
                                  List<? extends GenericAdapterRecyclerView.ItemModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getViewType() == newList.get(newItemPosition).getViewType();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldData = oldList.get(oldItemPosition).getData();
        Object newData = newList.get(newItemPosition).getData();
        return !(oldData == null || newData == null) && oldData.equals(newData);
    }
}
