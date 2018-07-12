package com.zsmarter.mdmDevice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.zsmarter.mdmDevice.network.LoginUtil;
import com.zsmarter.mdmDevice.network.UrlConfig;
import com.zsmarter.mdmDevice.network.bean.LoginPostBody;
import com.zsmarter.mdmDevice.network.bean.LoginResponseBody;
import com.zsmarter.mdmDevice.network.bean.UserInfoResponseBody;
import com.zsmarter.mdmdevice.R;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String USER_INFO = "userInfo";
    public static final String USER_NAME = "userName";
    public static final String USER_ORG_NAME = "userOrgName";
    public static final String USER_ORG_ID = "userOrgName";
    public static final String USER_ORG_TYPE = "userOrgType";

    private Button btLogin;
    private EditText edUserName;
    private EditText edPassWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUserName = findViewById(R.id.et_username);
        edPassWord = findViewById(R.id.et_password);
        edUserName.setText("100314");
        edPassWord.setText("e10adc3949ba59abbe56e057f20f883e");

        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                showProgress("正在登录...");
                login(edUserName.getText().toString(), edPassWord.getText().toString());
                break;
        }
    }

    public void login(String userName, String passWord) {
        LoginUtil.getNetWork().login(new LoginPostBody(userName, passWord))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<LoginResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponseBody loginResponseBody) {

                        try {
                            if (!TextUtils.isEmpty(loginResponseBody.getContext().getToken())) {
                                UrlConfig.token = loginResponseBody.getContext().getToken();
                                Log.i("hcb", "onNext=======" + loginResponseBody.getContext().getToken());
                                getUserInfo(UrlConfig.token);
                            }
                        } catch (Exception e) {
                            Log.i("hcb", "onError=======" + e.toString());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "onError=======" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getUserInfo(String token) {
        LoginUtil.getNetWork().userInfo(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserInfoResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserInfoResponseBody userInfoResponseBody) {
                        try {
                            if (!TextUtils.isEmpty(userInfoResponseBody.getContext().getName())) {

                                Log.i("hcb", "onNext=======" + userInfoResponseBody.getContext().getName());
                                SharedPreferences sp = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(USER_NAME, userInfoResponseBody.getContext().getName());
                                editor.putString(USER_ORG_NAME, userInfoResponseBody.getContext().getOrgName());
                                editor.putString(USER_ORG_ID, userInfoResponseBody.getContext().getOrgId());
                                editor.putString(USER_ORG_TYPE, userInfoResponseBody.getContext().getOrgType());
                                editor.commit();

                            }
                        } catch (Exception e) {
                            Log.i("hcb", "onError=======" + e.toString());
                            dismissProgress();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgress();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MdmDeviceMapActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

}
