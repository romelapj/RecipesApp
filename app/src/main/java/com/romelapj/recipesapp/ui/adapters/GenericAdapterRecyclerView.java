package com.romelapj.recipesapp.ui.adapters;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapterRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<ItemModel> items;
    protected OnItemClickListener onItemClickListener;
    protected OnItemGestureListener onItemGestureListener;
    private ViewFactory viewFactory;
    private int positionLastItemAnimated = -1;
    private ViewHolderState viewHolderState;
    private RecyclerView.RecycledViewPool viewPool;

    public GenericAdapterRecyclerView(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        items = new ArrayList<>();
        viewHolderState = new ViewHolderState();
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void addItems(List<? extends ItemModel> itemsToAdd) {
        if (!items.containsAll(itemsToAdd)) {
            items.addAll(itemsToAdd);
            notifyDataSetChanged();
        }
    }

    public void updateItemsWithDiffUtil(List<? extends ItemModel> newItems) {
        List<ItemModel> previousItems = new ArrayList<>(getItems());
        updateItems(newItems);
        DiffUtil.calculateDiff(new GenericAdapterDiffUtil(previousItems, newItems))
                .dispatchUpdatesTo(this);
    }

    public void addItems(int position, List<? extends ItemModel> itemsToAdd) {
        if (!items.containsAll(itemsToAdd)) {
            items.addAll(position, itemsToAdd);
            notifyItemRangeInserted(position, itemsToAdd.size());
        }
    }

    public void removeItemAtPosition(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItemsAt(int positionStart, int count) {
        if ((positionStart + count) <= items.size()) {
            for (int i = 0; i < count; i++) {
                items.remove(positionStart);
                notifyItemRemoved(positionStart);
            }
        }
    }

    public void updateItems(List<? extends ItemModel> itemsToUpdate) {
        this.items.clear();
        this.items.addAll(itemsToUpdate);
    }

    public void updateItem(int position, ItemModel itemToAdd) {
        items.set(position, itemToAdd);
        notifyItemChanged(position);
    }

    public void clearItems() {
        if (items != null) {
            items.clear();
            items = new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    public ItemModel getItemAt(int position) {
        return items.get(position);
    }

    public void addItem(int index, ItemModel itemToAdd) {
        items.add(index, itemToAdd);
        notifyItemInserted(index);
    }

    public void addItem(ItemModel itemToAdd) {
        items.add(itemToAdd);
        notifyItemInserted(items.size() - 1);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemGestureListener(OnItemGestureListener onItemGestureListener) {
        this.onItemGestureListener = onItemGestureListener;
    }

    public List<? extends ItemModel> getItems() {
        return this.items;
    }

    public void setItems(List<? extends ItemModel> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (items != null && items.size() > position && items.get(position) instanceof ItemModel) ? items.get(position).getViewType() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(viewFactory.getView(parent, viewType));
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (hasStableIds()) {
            viewHolderState.save(holder);
        }
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds()) {
            return items.get(position).hashCode();
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemView itemView = (ItemView) holder.itemView;
        if (items.get(position) instanceof ItemModel) {
            itemView.bind(items.get(position).getData(), position);
        }
        viewHolderState.restore(holder);
        if (onItemClickListener != null) {
            itemView.setItemClickListener(onItemClickListener);
        }

        if (onItemGestureListener != null && itemView instanceof ItemGesture) {
            ((ItemGesture) itemView).setGestureItemListener(onItemGestureListener);
        }

        if (itemView instanceof ItemAnimationView) {
            ItemAnimationView itemAnimationView = (ItemAnimationView) itemView;
            if (itemAnimationView.shouldAnimate()) {
                if (position > positionLastItemAnimated) {
                    itemAnimationView.initAnimation();
                    positionLastItemAnimated = position;
                } else {
                    itemAnimationView.finishAnimation();
                }
            }
        }
        if (itemView instanceof ItemViewWithInnerRecycler) {
            ((ItemViewWithInnerRecycler) itemView).setViewPool(viewPool);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Object itemView = holder.itemView;
            if (itemView instanceof ItemViewV2) {
                ((ItemViewV2) itemView).updateAfterDispatchDiff(((Bundle) payloads.get(0)), position);
            } else if (itemView instanceof ItemView) {
                ((ItemView) itemView).bind(((ItemModel) payloads.get(0)).getData(), position);
            } else {
                super.onBindViewHolder(holder, position, payloads);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public int getPositionOf(Object data) {
        int position = -1;
        for (int i = 0, limit = items.size(); i < limit; i++) {
            if (items.get(i).getData().equals(data)) {
                position = i;
                break;
            }
        }
        return position;
    }

    public RecyclerView.RecycledViewPool getViewPool() {
        return viewPool;
    }

    public interface ItemViewWithInnerRecycler<T> extends ItemViewV2<T> {
        void setViewPool(RecyclerView.RecycledViewPool viewPool);
    }

    public interface ItemViewV2<T> extends ItemView<T> {
        void updateAfterDispatchDiff(Bundle information, int position);
    }

    public interface ItemView<T> {
        void bind(T item, int position);

        void setItemClickListener(OnItemClickListener onItemClickListener);

        int getIdForClick();

        T getData();
    }

    public interface ItemGesture<T> extends ItemViewV2<T> {
        void setGestureItemListener(OnItemGestureListener gestureItemListener);
    }

    public interface ItemAnimationView<E> extends ItemView<E> {
        void initAnimation();

        void finishAnimation();

        boolean shouldAnimate();
    }

    public interface ItemModel<T> {
        T getData();

        void setData(T data);

        int getViewType();
    }

    public interface OnItemGestureListener {

        void onMotionEvents(MotionEvent event, ItemView itemView);
    }

    public interface OnItemClickListener {
        void onItemClicked(ItemView itemView);

        void onItemLongClicked(ItemView itemView);
    }

    public static class OnItemClickListenerAdapter implements OnItemClickListener {

        @Override
        public void onItemClicked(ItemView itemView) {
        }

        @Override
        public void onItemLongClicked(ItemView itemView) {

        }
    }

    public static class ItemModelAbstract implements ItemModel<Object> {

        private Object data;
        private int viewType;

        public ItemModelAbstract(Object data, int viewType) {
            this.data = data;
            this.viewType = viewType;
        }

        public ItemModelAbstract(int viewType) {
            this.viewType = viewType;
        }

        @Override
        public Object getData() {
            return data;
        }

        @Override
        public void setData(Object data) {
            this.data = data;
        }

        @Override
        public int getViewType() {
            return viewType;
        }

        @Override
        public boolean equals(Object obj) {
            ItemModelAbstract item = obj instanceof ItemModelAbstract ? (ItemModelAbstract) obj : null;
            return item != null && item.getViewType() == viewType && item.getData() != null && item.getData().equals(data);
        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : super.hashCode();
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ItemView itemView) {
            super((View) itemView);
        }
    }
}
