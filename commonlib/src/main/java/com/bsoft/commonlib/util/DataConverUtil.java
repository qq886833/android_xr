package com.bsoft.commonlib.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

/**
 * Created by chenkai on 2018/4/10.
 */

public class DataConverUtil {

    /**
     * 把modelA对象的属性值赋值给bClass对象的属性。
     *
     * @param modelA
     * @param bClass
     * @param <B>
     * @return
     */
    public static <A, B> B convertAtoB(A modelA, Class<B> bClass) {
        if (modelA == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            String strA = null;
            if (modelA instanceof String) {
                strA = (String) modelA;
            } else {
                strA = gson.toJson(modelA);
            }
            B instanceB = gson.fromJson(strA, bClass);
            return instanceB;
        } catch (Exception e) {
            return null;
        }
    }


    public static <A, B> B convertAtoBInFastJson(A modelA, Class<B> bClass) {
        if (modelA == null) {
            return null;
        }
        try {
            String strA = null;
            if (modelA instanceof String) {
                strA = (String) modelA;
            } else {
                strA = JSON.toJSONString(modelA);
            }
            B instanceB = JSON.parseObject(strA, bClass);
            return instanceB;
        } catch (Exception e) {
            return null;
        }
    }
}
