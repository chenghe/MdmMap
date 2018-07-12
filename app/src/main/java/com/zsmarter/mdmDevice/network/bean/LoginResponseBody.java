package com.zsmarter.mdmDevice.network.bean;

/**
 * Created by hecheng on 2018/6/22
 */
public class LoginResponseBody extends BaseResponse{

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class Context{
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
