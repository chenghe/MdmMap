package com.zsmarter.mdmDevice.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.MaterialDialog;

public class BaseActivity extends FragmentActivity{

    private MaterialDialog progressDialog;
    private MaterialDialog dialog;

    protected void logOut(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
        activity.finish();
    }

    protected void showProgress() {
        progressDialog =  new MaterialDialog.Builder(this)
                .content("正在加载数据...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .show();
    }

    protected void showProgress(String progress) {
        progressDialog =  new MaterialDialog.Builder(this)
                .content(progress)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .show();
    }

    protected void showBasicDialog(String content,String positiveTitle,String negativeTitle,
                                 MaterialDialog.SingleButtonCallback positiveCallback,MaterialDialog.SingleButtonCallback negativeCallback) {
        dialog = new MaterialDialog.Builder(this)
                .content(content)
                .positiveText(positiveTitle)
                .negativeText(negativeTitle)
                .onPositive(positiveCallback)
                .onNegative(negativeCallback)
                .show();
    }

    protected void dissmissDialog(){
        if (dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }

    protected void dismissProgress(){
        if (progressDialog!=null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}
