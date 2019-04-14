package com.bsoft.baselib.base.dicChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.bsoft.baselib.R;
import com.bsoft.baselib.base.activity.BaseActivity;
import com.bsoft.baselib.widget.recyclerview.RecyclerViewUtil;
import com.bsoft.baselib.widget.recyclerview.adapter.CoreListAdapter;
import com.bsoft.baselib.widget.recyclerview.adapter.ItemViewDelegate;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DicChoiceThemeActivity extends BaseActivity {
    /*Default*/
    public static final String INTENT_TITLE = "title";
    public static final String INTENT_DATA_LIST = "dataList";

    public static final String INTENT_RESULT = "dic_choice_result";
    /*Util*/
    private CoreListAdapter<ChoiceItem> adapter;
    /*Flag*/
    private ArrayList<ChoiceItem> dataList;
    private String title;
    private ChoiceItem choiceResult;
    /*View*/
    private RecyclerView recyclerView;


    public static void appStart(Activity activity, String title
            , ArrayList<ChoiceItem> dataList, ChoiceItem choiceResult, int resultCode) {
        Intent intent = new Intent(activity, DicChoiceThemeActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_DATA_LIST, dataList);
        intent.putExtra(INTENT_RESULT, choiceResult);
        activity.startActivityForResult(intent, resultCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_core_activity_dic_choice_theme);

        parseIntent();

        initLayout();
        initRecyclerView();

        updateView();
    }

    private void parseIntent() {
        if (getIntent() != null) {
            title = getIntent().getExtras().getString(INTENT_TITLE);
            dataList = (ArrayList<ChoiceItem>) getIntent().getSerializableExtra(INTENT_DATA_LIST);
            choiceResult = (ChoiceItem) getIntent().getSerializableExtra(INTENT_RESULT);

            if (null != choiceResult) {
                int index = -1;
                if ((index = dataList.indexOf(choiceResult)) != -1) {
                    dataList.get(index).setChoice(true);
                }
            }
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        recyclerView = findViewById(R.id.recyclerView);

       baseCoreTvTitle.setText(title);
    }

    @Override
    protected void onRefreshView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initRecyclerView() {
        RecyclerViewUtil.initLinearV(baseActivity, recyclerView, R.color.base_core_divider, R.dimen.dp_0_5, R.dimen.dp_15, R.dimen.dp_0);
        ChoiceViewDelegate viewDelegate = new ChoiceViewDelegate();
        viewDelegate.setOnItemClickListener(new ItemViewDelegate.OnItemClickListener<ChoiceItem>() {
            @Override
            public void onItemClick(@NonNull ArrayList<ChoiceItem> datas, int position) {
                choiceResult = datas.get(position);
                choiceResult.setChoice(true);
                for (ChoiceItem item : datas) {
                    if (!choiceResult.equals(item)) {
                        item.setChoice(false);
                    }
                }
                adapter.notifyDataSetChanged();

                getIntent().putExtra(INTENT_RESULT, choiceResult);
                setResult(RESULT_OK, getIntent());
                finish();
            }

            @Override
            public boolean onItemLongClick(@NonNull ArrayList<ChoiceItem> datas, int position) {
                return true;
            }

            @Override
            public void onItemViewClick(@NonNull View view, @NonNull ArrayList<ChoiceItem> datas, int position) {

            }

            @Override
            public boolean onItemViewLongClick(@NonNull View view, @NonNull ArrayList<ChoiceItem> datas, int position) {
                return false;
            }
        });
        adapter = new CoreListAdapter<>(baseActivity, viewDelegate);
        recyclerView.setAdapter(adapter);
    }

    private void updateView() {
        adapter.setData(dataList);
    }
}
