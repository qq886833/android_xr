package com.bsoft.baselib.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.bsoft.baselib.R;

import androidx.annotation.IdRes;

@SuppressLint("ValidFragment")
public class CoreDialog extends CoreDialogFragment {
    /*Default*/
    /*Util*/
    /*Flag*/
    /*View*/
    private boolean cancelable = false;
    private boolean canceledOnTouchOutside = false;
    private DialogInterface.OnKeyListener onKeyListener;
    /*View*/
    private View root;
    private Dialog alertDialog;

    public CoreDialog(View root) {
        this.root = root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog alertDialog = new Dialog(getActivity(), R.style.base_core_dialog_theme);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setContentView(root);
        return alertDialog;
    }
    @Override
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        if (alertDialog != null) {
            alertDialog.setCancelable(cancelable);
        }
    }

    public void setCanceledOnTouchOutside(boolean touchOutside) {
        this.canceledOnTouchOutside = touchOutside;
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(touchOutside);
        }
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
        if (alertDialog != null) {
            alertDialog.setOnKeyListener(onKeyListener);
        }
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (root == null) {
            return null;
        }
        return root.findViewById(id);
    }
}
