package com.bsoft.baselib.widget.recyclerview.adapter;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 *
 * @param <T>
 */
public class CoreViewHolder<T> extends RecyclerView.ViewHolder {
    /*Default*/
    /*Util*/
    @NonNull
    public ItemViewDelegate<T> itemViewDelegate;
    /*Flag*/
    /*View*/

    public CoreViewHolder(View itemView) {
        super(itemView);
    }

    public void onBindViewHolder(ArrayList<T> datas, int position) {
        itemViewDelegate.initBindViewHolder(datas, position);
        itemViewDelegate.onBindViewHolder(datas, position);
    }
}
