package com.zsmarter.mdmDevice.network.bean;

/**
 * Created by hecheng on 2018/6/25
 */
public class UserInfo {

    public static final String ORG_TYPE_HEADOFFICE = "headoffice";
    public static final String ORG_TYPE_BRANCH  = "branch";
    public static final String ORG_TYPE_SUBBRANCH = "subbranch";

    private String userName;
    private String orgName;//组织名
    private String orgId;//组织ID
    private String orgType;

    public static String getOrgTypeHeadoffice() {
        return ORG_TYPE_HEADOFFICE;
    }

    public static String getOrgTypeBranch() {
        return ORG_TYPE_BRANCH;
    }

    public static String getOrgTypeSubbranch() {
        return ORG_TYPE_SUBBRANCH;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
