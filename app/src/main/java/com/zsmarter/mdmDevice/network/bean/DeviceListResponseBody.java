package com.zsmarter.mdmDevice.network.bean;

import java.util.List;

/**
 * Created by hecheng on 2018/6/22
 */
public class DeviceListResponseBody extends BaseResponse{

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class Context{

        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public class Result{
            private List<RowsItem>rows;

            public List<RowsItem> getRows() {
                return rows;
            }

            public void setRows(List<RowsItem> rows) {
                this.rows = rows;
            }

            public class RowsItem{
                private String deviceCode;
                private String deviceName;
                private String deviceOwnOrgName;
                private boolean isCheck;
                private int deviceIcon;
                private String addressFence = "1";
                private List<String> fenceCityName;
                private List<String> fencedistrictName;

                public String getAddressFence() {
                    return addressFence;
                }

                public void setAddressFence(String addressFence) {
                    this.addressFence = addressFence;
                }

                public List<String> getFenceCityName() {
                    return fenceCityName;
                }

                public void setFenceCityName(List<String> fenceCityName) {
                    this.fenceCityName = fenceCityName;
                }

                public List<String> getFencedistrictName() {
                    return fencedistrictName;
                }

                public void setFencedistrictName(List<String> fencedistrictName) {
                    this.fencedistrictName = fencedistrictName;
                }

                public int getDeviceIcon() {
                    return deviceIcon;
                }

                public void setDeviceIcon(int deviceIcon) {
                    this.deviceIcon = deviceIcon;
                }

                public boolean isCheck() {
                    return isCheck;
                }

                public void setCheck(boolean check) {
                    isCheck = check;
                }

                public String getDeviceCode() {
                    return deviceCode;
                }

                public void setDeviceCode(String deviceCode) {
                    this.deviceCode = deviceCode;
                }

                public String getDeviceName() {
                    return deviceName;
                }

                public void setDeviceName(String deviceName) {
                    this.deviceName = deviceName;
                }

                public String getDeviceOwnOrgName() {
                    return deviceOwnOrgName;
                }

                public void setDeviceOwnOrgName(String deviceOwnOrgName) {
                    this.deviceOwnOrgName = deviceOwnOrgName;
                }
            }
        }

    }
}
