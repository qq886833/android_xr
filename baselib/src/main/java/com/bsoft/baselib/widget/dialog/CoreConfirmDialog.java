package com.bsoft.baselib.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bsoft.baselib.R;
import com.bsoft.baselib.util.EffectUtil;


public class CoreConfirmDialog extends CoreDialogFragment {
    /*Default*/
    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_INFO = "info";
    private static final String ARG_COMFIRM_TEXT = "comfirm_text";
    private static final String ARG_CANCEL_TEXT = "cancel_text";
    /*Util*/
    /*Flag*/
    private String title;
    private String content;
    private String info;
    private String confirmText;
    private String cancelText;

    private CommonDialogListener listener;
    /*View*/
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvInfo;
    private TextView tvConfirm;
    private TextView tvCancel;
    private View diver;

    public interface CommonDialogListener {
        void onComplete(boolean ok, String tag);
    }

    public static CoreConfirmDialog newInstance(String content, String confirmText) {
        return newInstance(null, content, null, confirmText, null);
    }

    public static CoreConfirmDialog newInstance(String content, String confirmText, String cancelText) {
        return newInstance(null, content, null, confirmText, cancelText);
    }

    public static CoreConfirmDialog newInstance(String title, String content, String confirmText, String cancelText) {
        return newInstance(title, content, null, confirmText, cancelText);
    }

    public static CoreConfirmDialog newInstance2(String content, String info, String confirmText, String cancelText) {
        return newInstance(null, content, info, confirmText, cancelText);
    }

    public static CoreConfirmDialog newInstance(String title, String content, String info, String confirmText, String cancelText) {
        CoreConfirmDialog fragment = new CoreConfirmDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        args.putString(ARG_INFO, info);
        args.putString(ARG_COMFIRM_TEXT, confirmText);
        args.putString(ARG_CANCEL_TEXT, cancelText);
        fragment.setArguments(args);
        return fragment;
    }

    public CoreConfirmDialog setCommonDialogListener(CommonDialogListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            content = getArguments().getString(ARG_CONTENT);
            info = getArguments().getString(ARG_INFO);
            confirmText = getArguments().getString(ARG_COMFIRM_TEXT);
            cancelText = getArguments().getString(ARG_CANCEL_TEXT);
        }

        Dialog alertDialog = new Dialog(getActivity(), R.style.base_core_dialog_theme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.base_core_dialog_confirm, null);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvContent = view.findViewById(R.id.tvContent);
        tvInfo = view.findViewById(R.id.tvInfo);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        tvCancel = view.findViewById(R.id.tvCancel);
        diver = view.findViewById(R.id.diver);

        tvContent.setText(content);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(info)) {
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(info);
        } else {
            tvInfo.setVisibility(View.GONE);
        }

        tvConfirm.setText(confirmText);
        EffectUtil.addClickEffect(tvConfirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onComplete(true, getTag());
                }
                dismiss();
            }
        });

        if (!TextUtils.isEmpty(cancelText)) {
            tvCancel.setVisibility(View.VISIBLE);
            diver.setVisibility(View.VISIBLE);
            tvCancel.setText(cancelText);
            EffectUtil.addClickEffect(tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onComplete(false, getTag());
                    }
                    dismiss();
                }
            });
        } else {
            tvCancel.setVisibility(View.GONE);
            diver.setVisibility(View.GONE);
        }

        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (listener != null) {
                        listener.onComplete(false, getTag());
                    }
                    dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setContentView(view);
        return alertDialog;
    }
}
