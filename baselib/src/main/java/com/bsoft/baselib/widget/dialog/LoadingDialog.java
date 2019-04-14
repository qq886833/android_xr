package com.bsoft.baselib.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsoft.baselib.R;
import com.bumptech.glide.Glide;


import androidx.annotation.DrawableRes;

public class LoadingDialog extends CoreDialogFragment {
    /*Default*/
    private static final String ARG_RESOURCE_ID = "resourceId";
    private static final String ARG_MSG = "msg";
    /*Util*/
    /*Flag*/
    private int resourceId;
    private String msg;
    private boolean needBack = true;
    /*View*/
    private ImageView ivLoading;
    private TextView tvMsg;


    public static LoadingDialog newInstance() {
        return newInstance(0, null);
    }

    public static LoadingDialog newInstance(@DrawableRes int resourceId, String msg) {
        LoadingDialog fragment = new LoadingDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_RESOURCE_ID, resourceId);
        args.putString(ARG_MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isNeedBack() {
        return needBack;
    }

    public void setNeedBack(boolean needBack) {
        this.needBack = needBack;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            resourceId = getArguments().getInt(ARG_RESOURCE_ID);
            msg = getArguments().getString(ARG_MSG);
        }

        Dialog alertDialog = new Dialog(getActivity(), R.style.base_core_dialog_theme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.base_core_dialog_loading, null);
        ivLoading = view.findViewById(R.id.ivLoading);
        tvMsg = view.findViewById(R.id.tvMsg);

        if (resourceId != 0) {
            Glide.with(getActivity()).load(resourceId).into(ivLoading);
        } else {
            Glide.with(getActivity()).load(R.drawable.base_core_loading).into(ivLoading);
        }

        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(msg);
        } else {
            tvMsg.setVisibility(View.GONE);
        }

        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && needBack) {
                    dismiss();
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setContentView(view);
        return alertDialog;
    }
}
