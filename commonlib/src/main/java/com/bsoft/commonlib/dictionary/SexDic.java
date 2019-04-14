package com.bsoft.commonlib.dictionary;

import android.text.TextUtils;


import com.bsoft.baselib.base.dicChoice.ChoiceItem;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.commonlib.R;

import java.util.ArrayList;

public class SexDic {
    public static final String MAN = "1";
    public static final String WOMEN = "2";

    public static String getSexStr(String type) {
        if (TextUtils.equals(type, MAN)) {
            return CoreAppInit.getApplication().getString(R.string.common_man);
        } else if (TextUtils.equals(type, WOMEN)) {
            return CoreAppInit.getApplication().getString(R.string.common_woman);
        } else {
            return "";
        }
    }

    public static ArrayList<ChoiceItem> getChoice() {
        ArrayList<ChoiceItem> choiceItems = new ArrayList<>();

        ChoiceItem idcard = new ChoiceItem();
        idcard.setIndex(MAN);
        idcard.setItemName(getSexStr(MAN));
        choiceItems.add(idcard);

        ChoiceItem officer = new ChoiceItem();
        officer.setIndex(WOMEN);
        officer.setItemName(getSexStr(WOMEN));
        choiceItems.add(officer);

        return choiceItems;
    }
}
