package com.zsmarter.mdmDevice.network;

import com.zsmarter.mdmdevice.BuildConfig;

/**
 * Created by hecheng on 2018/6/22
 */
public class UrlConfig {

    public static String token = "";
//    public static final String LOGIN_URL = "framework/n/systemsecurity/login";
    /**
     * 登录msp获取token
     */
    public static final String LOGIN_URL = BuildConfig.WEB_MSP_URL_HEAD+ "/n/systemsecurity/login";

    /**
     * 获取用户信息
     */
    public static final String USERINFO_URL = BuildConfig.WEB_MSP_URL_HEAD+"/n/systemsecurity/mdmUserInfo";
//        public static final String USERINFO_URL = "framework/n/systemsecurity/mdmUserInfo";
    /**
     * 获取全部设备列表
     */
    public static final String DEVICELIST_URL =BuildConfig.WEB_MDM_URL_HEAD+ "/deviceBaseInfo/listBaseInfo";

    /**
     * 获取组织下设备列表
     */
    public static final String ORG_DEVICELIST_URL =BuildConfig.WEB_MDM_URL_HEAD+ "/pushMessage/queryAllRegisterDeviceByOrgID";

    /**
     * 获取设备当前坐标
     */
    public static final String DEVICELOCATION_URL = BuildConfig.WEB_MDM_URL_HEAD+"/location/queryDevicesLocation";

    /**
     * 获取设备不同时间坐标
     */
    public static final String DEVICETIMELOCATION_URL =BuildConfig.WEB_MDM_URL_HEAD+ "/location/queryDeviceLocation";

    /**
     * 获取组织列表
     */
//    public static final String ORGANIZE_LIST_URL = "framework/n/sysOrganize/list";
    public static final String ORGANIZE_LIST_URL = BuildConfig.WEB_MSP_URL_HEAD+"/n/sysOrganize/list";

    /**
     * 获取地址围栏列表
     */
    public static final String ADDRESS_FENCE_LIST_URL =BuildConfig.WEB_MDM_URL_HEAD+ "/addressFence/listAddressFence";

//    /**
//     * 根据组织id查找设备
//     */
////    public static final String ORGANIZE_DEVICE_LIST_URL = "framework/n/deviceTailbox/list";
//    public static final String ORGANIZE_DEVICE_LIST_URL = BuildConfig.WEB_MDM_URL_HEAD+"/n/deviceTailbox/list";

}
