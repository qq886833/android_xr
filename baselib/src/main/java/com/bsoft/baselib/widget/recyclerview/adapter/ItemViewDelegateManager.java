package com.bsoft.baselib.widget.recyclerview.adapter;

import android.app.Activity;

import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;

public class ItemViewDelegateManager<T> {
    private SparseArrayCompat<ItemViewDelegate<T>> delegates = new SparseArrayCompat<>();

    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        int viewType = delegates.size();
        if (delegate != null) {
            delegates.put(viewType, delegate);
        }
        return this;
    }

    public ItemViewDelegateManager<T> addDelegate(int viewType, ItemViewDelegate<T> delegate) {
        if (delegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + delegates.get(viewType));
        }
        delegates.put(viewType, delegate);
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(ItemViewDelegate<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemViewDelegate is null");
        }
        int indexToRemove = delegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    public ItemViewDelegate<T> getItemViewDelegate(int viewType) {
        return delegates.get(viewType);
    }

    public int getItemViewType(ItemViewDelegate<T> itemViewDelegate) {
        return delegates.indexOfValue(itemViewDelegate);
    }

    public CoreViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent,
                                                      Activity activity,
                                                      CoreListAdapter<T> adapter, int viewType) {
        ItemViewDelegate<T> delegate = getItemViewDelegate(viewType);
        delegate.onCreateViewHolder(parent);
        return delegate.initCreateViewHolder(activity, adapter);
    }

    public int getItemViewType(ArrayList<T> datas, int position) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(datas, position)) {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegate added that matches position=" + position + " in data source");
    }
}
