package com.bsoft.commonlib.shapref;

public class LocalDataUtil {
    private static LocalDataUtil localDataUtil = null;



    private LocalDataUtil() {
    }


    public static LocalDataUtil getInstance() {
        if (localDataUtil == null) {
            localDataUtil = new LocalDataUtil();
        }
        return localDataUtil;
    }



























    public void setLoginState(boolean b) {
        AccountSharpref.getInstance().setLoginState(b);
    }

    public boolean getLoginState() {
        return AccountSharpref.getInstance().getLoginState();
    }
}
