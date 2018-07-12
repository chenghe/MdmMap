package com.zsmarter.mdmDevice.clusterutil;

import com.baidu.mapapi.model.LatLng;
import com.zsmarter.mdmDevice.DeviceItem;

import java.util.ArrayList;
import java.util.List;

public class DeviceBean {
    private int deviceIcon;
    private List<DeviceItem> items;
    private LatLng[] locations;
    private String deviceName;
    private boolean isCheck;

    public DeviceBean(int deviceIcon, LatLng[] locations,String deviceName) {
        this.deviceIcon = deviceIcon;
        this.locations = locations;
        this.deviceName = deviceName;
        initData();
    }

    private void initData(){
        items = new ArrayList<DeviceItem>();
        for(int i=0;i<locations.length;i++){
            items.add(new DeviceItem(locations[i],deviceIcon));
        }
    }


    public int getDeviceIcon() {
        return deviceIcon;
    }

    public List<DeviceItem> getItems() {
        return items;
    }

    public LatLng[] getLocations() {
        return locations;
    }

    public String getDeviceName() {
        return deviceName;
    }


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
