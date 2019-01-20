package com.romelapj.recipesapp.ui.adapters;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;

import java.util.Collection;

public class ViewHolderState extends LongSparseArray<ViewHolderState.ViewState> implements Parcelable {

    public ViewHolderState() {
        super();
    }

    private ViewHolderState(int size) {
        super(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final int size = size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            dest.writeLong(keyAt(i));
            dest.writeParcelable(valueAt(i), 0);
        }
    }

    public final Creator<ViewHolderState> CREATOR = new Creator<ViewHolderState>() {

        public ViewHolderState[] newArray(int size) {
            return new ViewHolderState[size];
        }

        public ViewHolderState createFromParcel(Parcel source) {
            int size = source.readInt();
            ViewHolderState state = new ViewHolderState(size);

            for (int i = 0; i < size; i++) {
                long key = source.readLong();
                ViewState value = source.readParcelable(ViewState.class.getClassLoader());
                state.put(key, value);
            }
            return state;
        }
    };

    public void save(Collection<RecyclerView.ViewHolder> holders) {
        for(RecyclerView.ViewHolder holder : holders) {
            save(holder);
        }
    }

    public void save(RecyclerView.ViewHolder holder) {
        // Reuse the previous sparse array if available. We shouldn't need to clear it since the
        // exact same view type is being saved to it, which
        // should have identical ids for all its views, and will just overwrite the previous state
        long itemId = holder.getItemId();
        if (itemId != RecyclerView.NO_ID) {

            ViewState state = get(itemId);
            if (state == null) {
                state = new ViewState();
            }

            state.save(holder.itemView);
            put(itemId, state);
        }
    }

    public void restore(RecyclerView.ViewHolder holder) {
        long itemId = holder.getItemId();
        ViewState state = get(itemId);
        if (state != null && itemId != RecyclerView.NO_ID) {
            state.restore(holder.itemView);
        }
    }

    /**
     * A wrapper around a sparse array as a helper to save the state of a view. This also adds
     * parcelable support
     */
    public static class ViewState extends SparseArray<Parcelable> implements Parcelable {

        public ViewState() {
            super();
        }

        private ViewState(int size, int[] keys, Parcelable[] values) {
            super(size);
            for (int i = 0; i < size; ++i) {
                put(keys[i], values[i]);
            }
        }

        public void save(View view) {
            int originalId = view.getId();

            view.saveHierarchyState(this);
            view.setId(originalId);
        }

        public void restore(View view) {
            int originalId = view.getId();
            view.restoreHierarchyState(this);
            view.setId(originalId);
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            int size = size();
            int[] keys = new int[size];
            Parcelable[] values = new Parcelable[size];
            for (int i = 0; i < size; ++i) {
                keys[i] = keyAt(i);
                values[i] = valueAt(i);
            }
            parcel.writeInt(size);
            parcel.writeIntArray(keys);
            parcel.writeParcelableArray(values, flags);
        }

        public final Creator<ViewState> CREATOR =
                new Creator<ViewState>() {
                    @Override
                    public ViewState createFromParcel(Parcel source) {
                        int size = source.readInt();
                        int[] keys = new int[size];
                        source.readIntArray(keys);
                        Parcelable[] values = source.readParcelableArray(Parcelable.class.getClassLoader());
                        return new ViewState(size, keys, values);
                    }

                    @Override
                    public ViewState[] newArray(int size) {
                        return new ViewState[size];
                    }
                };
    }
}