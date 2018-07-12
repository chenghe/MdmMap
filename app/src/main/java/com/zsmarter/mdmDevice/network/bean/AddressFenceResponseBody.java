package com.zsmarter.mdmDevice.network.bean;

import java.util.List;

/**
 * Created by hecheng on 2018/6/27
 */
public class AddressFenceResponseBody extends BaseResponse {

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class Context {
        private List<Item> addressFences;

        public List<Item> getAddressFences() {
            return addressFences;
        }

        public void setAddressFences(List<Item> addressFences) {
            this.addressFences = addressFences;
        }

        public class Item {
            private String oid;
            private String cityName;
            private String districtName;

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getDistrictName() {
                return districtName;
            }

            public void setDistrictName(String districtName) {
                this.districtName = districtName;
            }
        }
    }
}
