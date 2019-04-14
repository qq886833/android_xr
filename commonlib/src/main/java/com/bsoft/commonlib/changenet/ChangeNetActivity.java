package com.bsoft.commonlib.changenet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bsoft.baselib.base.activity.BaseActivity;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.util.EffectUtil;
import com.bsoft.baselib.util.ExitUtil;
import com.bsoft.commonlib.R;
import com.bsoft.commonlib.arouter.CommonArouterGroup;
import com.bsoft.commonlib.arouter.interceptor.CommonTInterceptor;
import com.bsoft.commonlib.util.FilterEmoji;

import java.util.ArrayList;

import androidx.annotation.IdRes;


/**
 * create change_net chenkai 20170904
 */


@Route(path = CommonArouterGroup.CHANGE_NET_ACTIVITY, extras = CommonTInterceptor.GREEN_ALL)
public class ChangeNetActivity extends BaseActivity {
    /*Default*/
    /*Util*/
    /*Flag*/
    private NetAddressVo originVo;
    private ArrayList<NetAddressVo> netAddressVos;
    private ArrayList<NetRadio> radioButtons = new ArrayList<>();
    private NetAddressVo curAddressVo;
    /*View*/
    private TextView tvInternalVersion;
    private EditText edit;
    private ImageView ivBack;
    private TextView tvComfirm;
    private RadioGroup changeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_change_net);
        //default
        netAddressVos = NetEnvironmentUtil.getNetEnvironments(this);
        originVo = NetEnvironmentUtil.getCurEnvironment(this);

        initLayout();

        tvInternalVersion.setText("内部版本号：" + CoreConstant.INTERNALVERSION);
        updateVo(originVo);
        //动态添加选项
        radioButtons.clear();
        if (netAddressVos != null && !netAddressVos.isEmpty()) {
            for (int i = 0; i < netAddressVos.size(); i++) {
                NetAddressVo vo = netAddressVos.get(i);
                if (vo != null) {
                    addRadioBtn(i, vo);
                }
            }
        }
        //listener
        edit.addTextChangedListener(new FilterEmoji(edit, this));
        changeItems.setOnCheckedChangeListener(checkedChangeListener);
        EffectUtil.addClickEffect(ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EffectUtil.addClickEffect(tvComfirm);
        tvComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (originVo != null && curAddressVo != null
                        && (!TextUtils.equals(originVo.getHttpApiUrl(), edit.getText().toString())
                        || !TextUtils.equals(originVo.getEnvironment(), curAddressVo.getEnvironment()))) {
                    NetEnvironmentUtil.setEnvironment(ChangeNetActivity.this, curAddressVo.getEnvironment());
                    if (NetEnvironmentUtil.isManual(curAddressVo.getEnvironment())) {
                        NetEnvironmentUtil.setManualHttpUrl(ChangeNetActivity.this, edit.getText().toString());
                    }
                    ExitUtil.exitApp();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onRefreshView() {
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        ivBack = findViewById(R.id.ivBack);
        tvComfirm = findViewById(R.id.tvComfirm);
        tvInternalVersion = findViewById(R.id.tvInternalVersion);
        edit = findViewById(R.id.edit);
        changeItems = findViewById(R.id.changeItems);
    }

    private void updateVo(NetAddressVo addressVo) {
        if (addressVo == null) {
            return;
        }
        curAddressVo = addressVo;
        edit.setText(addressVo.getHttpApiUrl());
        if (NetEnvironmentUtil.isManual(addressVo.getEnvironment())) {
            edit.setEnabled(true);
        } else {
            edit.setEnabled(false);
        }
    }

    private void addRadioBtn(int id, NetAddressVo addressVo) {
        if (addressVo == null) {
            return;
        }
        RadioButton radioButton = new RadioButton(this);
        NetRadio netRadio = new NetRadio();

        radioButton.setId(id);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT
                , RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setText(addressVo.getEnvironmentText() + " " + addressVo.getHttpApiUrl());
        radioButton.setTextSize(12);
        //radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setPadding(10, 10, 10, 10);
        if (originVo != null && TextUtils.equals(originVo.getEnvironment(), addressVo.getEnvironment())) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
        netRadio.addressVo = addressVo;
        netRadio.radioButton = radioButton;
        radioButtons.add(netRadio);
        changeItems.addView(radioButton);
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            NetRadio radio = radioButtons.get(i);
            if (radio != null) {
                updateVo(radio.addressVo);
            }

        }
    };
}
