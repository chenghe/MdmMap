package com.zsmarter.mdmDevice.network;

import android.text.TextUtils;

import com.zsmarter.mdmDevice.network.bean.BaseResponse;

/**
 * Created by hecheng on 2018/6/27
 */
public class ResponseUtil {

    public static boolean isTimeOut(BaseResponse response){
        if (response == null || response.getCode().equals("01")|| TextUtils.isEmpty(response.getCode())){
            return true;
        }else if (response.getCode().equals("00")){
            return false;
        }else {
            return true;
        }
    }
}
