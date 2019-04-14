package com.bsoft.baselib.widget.recyclerview.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * chenkai
 *
 * @param <T>
 */
public class CoreListAdapter<T> extends RecyclerView.Adapter<CoreViewHolder<T>> {
    /*Default*/
    /*Util*/
    private Activity activity;
    /*Flag*/
    private ArrayList<T> mDatas;
    private ItemViewDelegateManager<T> manager;
    /*View*/

    public CoreListAdapter(@NonNull Activity activity, @NonNull ArrayList<ItemViewDelegate<T>> viewDelegates) {
        this.activity = activity;
        this.manager = new ItemViewDelegateManager<>();
        for (ItemViewDelegate delegate : viewDelegates) {
            manager.addDelegate(delegate);
        }
    }

    public CoreListAdapter(@NonNull Activity activity, @NonNull ItemViewDelegate<T>... viewDelegates) {
        this.activity = activity;
        this.manager = new ItemViewDelegateManager<>();
        for (ItemViewDelegate delegate : viewDelegates) {
            manager.addDelegate(delegate);
        }
    }

    public CoreListAdapter(@NonNull Activity activity, @NonNull ItemViewDelegateManager<T> manager) {
        this.activity = activity;
        this.manager = manager;
    }

    @NonNull
    @Override
    public CoreViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return manager.onCreateViewHolder(parent, activity, this, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CoreViewHolder<T> holder, int position) {
        holder.onBindViewHolder(mDatas, position);
    }

    @Override
    public int getItemViewType(int position) {
        return manager.getItemViewType(mDatas, position);
    }

    @Override
    public int getItemCount() {
        return isEmpty() ? 0 : mDatas.size();
    }

    public void setData(ArrayList<T> datas) {
        if (datas == null) {
            return;
        }

        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<T> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }

        if (isEmpty()) {
            setData(datas);
            return;
        } else {
            this.mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return mDatas;
    }

    public boolean isEmpty() {
        return (mDatas == null || mDatas.isEmpty());
    }

    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public void update(int position) {
        if (mDatas == null) {
            return;
        }
        if (position < 0 || position >= mDatas.size()) {return;}
        notifyItemChanged(position);
    }

    public void update(T data) {
        if (mDatas == null) {
            return;
        }
        if (mDatas.contains(data)) {
            int indexOf = mDatas.indexOf(data);
            mDatas.remove(indexOf);
            mDatas.add(indexOf, data);
            update(indexOf);
        }
    }

    public void update(ArrayList<T> datas) {
        if (mDatas == null || datas == null || datas.size() == 0) {
            return;
        }
        for (T data : datas) {
            if (mDatas.contains(data)) {
                int indexOf = mDatas.indexOf(data);
                mDatas.remove(indexOf);
                mDatas.add(indexOf, data);
            }
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mDatas == null) {
            return;
        }
        if (position < 0 || position >= mDatas.size()){ return;}
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size() - position);
    }

    public void remove(T data) {
        if (mDatas == null) {
            return;
        }
        remove(mDatas.indexOf(data));
    }

    public void remove(ArrayList<T> datas) {
        if (mDatas == null || datas == null || datas.size() == 0) {
            return;
        }
        mDatas.removeAll(datas);
        notifyDataSetChanged();
    }

    public void insert(int position, T data) {
        if (data == null || mDatas == null) {return;}
        if (position >= mDatas.size()) {position = mDatas.size();}
        if (position < 0){ position = 0;}
        mDatas.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDatas.size() - position);
    }

    public void insert(int position, ArrayList<T> datas) {
        if (mDatas == null || datas == null || datas.size() == 0){ return;}
        if (position >= mDatas.size()) {position = mDatas.size();}
        if (position < 0){ position = 0;}
        mDatas.addAll(position, datas);
        notifyDataSetChanged();
    }

    public void insertTop(T data) {
        insert(0, data);
    }

    public void insertBottom(T data) {
        insert(mDatas.size(), data);
    }

    public boolean constains(T data) {
        if (mDatas == null) {
            return false;
        }
        return mDatas.contains(data);
    }
}
