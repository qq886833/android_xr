package com.bsoft.databaselib.entity;
import com.bsoft.baselib.core.CoreVo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chenkai on 2018/6/5.
 */
@Entity
public class DeviceInfo extends CoreVo {
    @Id(autoincrement = true)
    private Long id;

    //厂家
    private String phoneCompany;
    //手机型号
    private String phoneModel;
    //手机平台
    private String phonePlatform;
    //版本号
    private String phoneVersion;
    //imei
    private String imei;
    //屏幕宽度
    private int screenWidth;
    //屏幕高度
    private int ScreenHeight;
    //运营商
    private String phoneOperator;
    @Generated(hash = 893571914)
    public DeviceInfo(Long id, String phoneCompany, String phoneModel,
            String phonePlatform, String phoneVersion, String imei, int screenWidth,
            int ScreenHeight, String phoneOperator) {
        this.id = id;
        this.phoneCompany = phoneCompany;
        this.phoneModel = phoneModel;
        this.phonePlatform = phonePlatform;
        this.phoneVersion = phoneVersion;
        this.imei = imei;
        this.screenWidth = screenWidth;
        this.ScreenHeight = ScreenHeight;
        this.phoneOperator = phoneOperator;
    }
    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPhoneCompany() {
        return this.phoneCompany;
    }
    public void setPhoneCompany(String phoneCompany) {
        this.phoneCompany = phoneCompany;
    }
    public String getPhoneModel() {
        return this.phoneModel;
    }
    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
    public String getPhonePlatform() {
        return this.phonePlatform;
    }
    public void setPhonePlatform(String phonePlatform) {
        this.phonePlatform = phonePlatform;
    }
    public String getPhoneVersion() {
        return this.phoneVersion;
    }
    public void setPhoneVersion(String phoneVersion) {
        this.phoneVersion = phoneVersion;
    }
    public String getImei() {
        return this.imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public int getScreenWidth() {
        return this.screenWidth;
    }
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }
    public int getScreenHeight() {
        return this.ScreenHeight;
    }
    public void setScreenHeight(int ScreenHeight) {
        this.ScreenHeight = ScreenHeight;
    }
    public String getPhoneOperator() {
        return this.phoneOperator;
    }
    public void setPhoneOperator(String phoneOperator) {
        this.phoneOperator = phoneOperator;
    }
}
