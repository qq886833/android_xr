package com.bsoft.baselib.widget.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class CoreDatePickerDialog extends CoreDialogFragment {
    /*Default*/
    private static final String ARG_CUR_YEAR = "year";
    private static final String ARG_CUR_MONTH = "month";
    private static final String ARG_CUR_DAY = "day";
    /*Util*/
    /*Flag*/
    private int curYear;
    private int curMonth;
    private int curDay;
    private long maxDate;

    private OnDateSetListener listener;
    /*View*/

    public interface OnDateSetListener {
        void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    public static CoreDatePickerDialog newInstance(int curYear, int curMonth, int curDay) {
        CoreDatePickerDialog fragment = new CoreDatePickerDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_CUR_YEAR, curYear);
        args.putInt(ARG_CUR_MONTH, curMonth);
        args.putInt(ARG_CUR_DAY, curDay);
        fragment.setArguments(args);
        return fragment;
    }

    public CoreDatePickerDialog setCommonDialogListener(OnDateSetListener listener) {
        this.listener = listener;
        return this;
    }

    public CoreDatePickerDialog setMaxDate(long maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            curYear = getArguments().getInt(ARG_CUR_YEAR);
            curMonth = getArguments().getInt(ARG_CUR_MONTH);
            curDay = getArguments().getInt(ARG_CUR_DAY);
        }
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (listener != null) {
                    listener.onDateSet(view, year, monthOfYear+1, dayOfMonth);
                }
            }
        },
                curYear,
                curMonth,
                curDay);

        if (maxDate != 0) {
            dialog.getDatePicker().setMaxDate(maxDate);
        }

        return dialog;
    }
}
