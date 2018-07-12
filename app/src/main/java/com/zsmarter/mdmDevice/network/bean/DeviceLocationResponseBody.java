package com.zsmarter.mdmDevice.network.bean;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by hecheng on 2018/6/24
 */
public class DeviceLocationResponseBody extends BaseResponse{

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class Context{
        private List<ResultItem>result;

        public List<ResultItem> getResult() {
            return result;
        }

        public void setResult(List<ResultItem> result) {
            this.result = result;
        }

        public class ResultItem{
            private String imei;
            private String localtionTime;
            private String longitude;
            private String latitude;
            private String oid;
            private int deviceIcon;
            private LatLng latLng;
            private boolean isOut;

            public boolean isOut() {
                return isOut;
            }

            public void setOut(boolean out) {
                isOut = out;
            }

            public int getDeviceIcon() {
                return deviceIcon;
            }

            public void setDeviceIcon(int deviceIcon) {
                this.deviceIcon = deviceIcon;
            }

            public LatLng getLatLng() {
                return latLng;
            }

            public void setLatLng(LatLng latLng) {
                this.latLng = latLng;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getLocaltionTime() {
                return localtionTime;
            }

            public void setLocaltionTime(String localtionTime) {
                this.localtionTime = localtionTime;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }
        }
    }
}
