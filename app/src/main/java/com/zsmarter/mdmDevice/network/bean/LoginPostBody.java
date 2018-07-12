package com.zsmarter.mdmDevice.network.bean;

/**
 * Created by hecheng on 2018/6/22
 */
public class LoginPostBody {
    private String userAccount;
    private String userPassword;

    public LoginPostBody(String userAccount, String userPassword) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
