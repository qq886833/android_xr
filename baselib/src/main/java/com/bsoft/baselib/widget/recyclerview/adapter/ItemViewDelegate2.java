package com.bsoft.baselib.widget.recyclerview.adapter;

import android.view.View;
import android.view.ViewGroup;

public abstract class ItemViewDelegate2<T> extends ItemViewDelegate<T> {
    @Override
    public void onCreateViewHolder(ViewGroup parent) {
        root = createView(parent);
        viewCreated(root);
    }

    public abstract View createView(ViewGroup parent);

    public abstract void viewCreated(View root);
}
