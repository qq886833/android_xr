package com.bsoft.baselib.base.dicChoice;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bsoft.baselib.R;
import com.bsoft.baselib.widget.recyclerview.adapter.ItemViewDelegate;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ChoiceViewDelegate extends ItemViewDelegate<ChoiceItem> {
    /*Default*/
    /*Util*/
    /*Flag*/
    /*View*/
    private TextView tvName;
    private ImageView ivSelect;

    @Override
    public void onCreateViewHolder(@NonNull ViewGroup parent) {
        root = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_core_item_singechoice, parent,false);
        tvName = root.findViewById(R.id.tvName);
        ivSelect = root.findViewById(R.id.ivSelect);
    }

    @Override
    public boolean isForViewType(@NonNull ArrayList<ChoiceItem> datas, int position) {
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArrayList<ChoiceItem> datas, final int position) {
        ChoiceItem item = datas.get(position);
        if (item == null) {
            return;
        }

        tvName.setText(item.getItemName());
        ivSelect.setSelected(item.isChoice());
    }
}
