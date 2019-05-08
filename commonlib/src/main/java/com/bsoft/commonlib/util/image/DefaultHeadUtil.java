package com.bsoft.commonlib.util.image;


import com.bsoft.commonlib.R;

import androidx.annotation.DrawableRes;

public class DefaultHeadUtil {
    public static @DrawableRes
    int getUserHead(String gender){
        //1 male 2 female
        return "2".equals(gender) ? R.mipmap.img_head_user_female 
                : R.mipmap.img_head_user_male;
    }
    public static @DrawableRes
    int getDocHead(String gender){
        //1 male 2 female
        return "2".equals(gender) ? R.mipmap.img_head_doctor_female 
                : R.mipmap.img_head_doctor_male;
    }
    public static @DrawableRes
    int getUserHead(){
        //1 male 2 female
        return R.mipmap.img_head_user_male;
    }
    public static @DrawableRes
    int getDocHead(){
        //1 male 2 female
        return R.mipmap.img_head_doctor_male;
    }
}
