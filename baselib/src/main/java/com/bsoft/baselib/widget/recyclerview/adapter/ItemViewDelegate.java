package com.bsoft.baselib.widget.recyclerview.adapter;


import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;


import com.bsoft.baselib.util.EffectUtil;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

/**
 * Created by chenkai on 16/6/22.
 */
public abstract class ItemViewDelegate<T> implements Cloneable {
    /*Default*/
    /*Util*/
    protected Activity activity;
    protected CoreListAdapter<T> adapter;
    protected OnItemClickListener<T> onItemClickListener;
    /*Flag*/
    /*View*/
    protected View root;

    public abstract void onCreateViewHolder(ViewGroup parent);

    public abstract boolean isForViewType(ArrayList<T> datas, int position);

    public abstract void onBindViewHolder( ArrayList<T> datas, int position);

    @Override
    protected ItemViewDelegate<T> clone() throws CloneNotSupportedException {
        return (ItemViewDelegate<T>) super.clone();
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        if (root == null) {
            return null;
        }
        return root.findViewById(id);
    }

    protected CoreViewHolder<T> initCreateViewHolder(@NonNull Activity activity,
                                                     @NonNull CoreListAdapter<T> adapter) {
        this.activity = activity;
        this.adapter = adapter;
        CoreViewHolder<T> holder = new CoreViewHolder<>(root);
        try {
            holder.itemViewDelegate = this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return holder;
    }

    protected void initBindViewHolder(@NonNull final ArrayList<T> datas, final int position) {
        if (onItemClickListener != null) {
            EffectUtil.addClickEffect(root);
            root.setClickable(false);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(datas, position);
                }
            });

            root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemClickListener.onItemLongClick(datas, position);
                }
            });
        }
    }

    public void removeItemClick() {
        if (root != null) {
            root.setClickable(false);
            root.setOnClickListener(null);
            root.setOnLongClickListener(null);
            root.setOnTouchListener(null);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(@NonNull ArrayList<T> datas, int position);

        boolean onItemLongClick(@NonNull ArrayList<T> datas, int position);

        void onItemViewClick(@NonNull View view, @NonNull ArrayList<T> datas, int position);

        boolean onItemViewLongClick(@NonNull View view, @NonNull ArrayList<T> datas, int position);
    }
}
