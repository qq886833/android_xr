package com.bsoft.commonlib.changenet;


import com.bsoft.baselib.core.CoreVo;

/**
 * Created by 83990 on 2018/2/7.
 */

public class NetAddressVo extends CoreVo {
    private String environment;
    private String environmentText;
    private String HttpApiUrl;
    private String HttpDownloadUrl;
    private String HttpImgUrl;

    public String getEnvironment() {
        return environment;
    }

    public String getHttpApiUrl() {
        return HttpApiUrl;
    }

    public String getHttpDownloadUrl() {
        return HttpDownloadUrl;
    }

    public String getHttpImgUrl() {
        return HttpImgUrl;
    }

    public String getEnvironmentText() {
        return environmentText;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setEnvironmentText(String environmentText) {
        this.environmentText = environmentText;
    }

    public void setHttpApiUrl(String httpApiUrl) {
        HttpApiUrl = httpApiUrl;
    }

    public void setHttpDownloadUrl(String httpDownloadUrl) {
        HttpDownloadUrl = httpDownloadUrl;
    }

    public void setHttpImgUrl(String httpImgUrl) {
        HttpImgUrl = httpImgUrl;
    }
}
