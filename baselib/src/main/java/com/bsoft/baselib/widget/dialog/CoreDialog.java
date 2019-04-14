package com.bsoft.baselib.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.bsoft.baselib.R;

@SuppressLint("ValidFragment")
public class CoreDialog extends CoreDialogFragment {
    /*Default*/
    /*Util*/
    /*Flag*/
    /*View*/
    private View root;

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
}
