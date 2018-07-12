package com.zsmarter.mdmDevice.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hecheng on 2018/7/4
 */
public class ToastUtil {

    public static void showToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
