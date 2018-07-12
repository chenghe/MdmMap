package com.zsmarter.mdmDevice;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.zsmarter.mdmDevice.clusterutil.clustering.ClusterItem;
import com.zsmarter.mdmdevice.R;

/**
 * 用于百度地图显示坐标对象
 */
public class DeviceItem implements ClusterItem {
    private final LatLng mPosition;
    private String time;
    private int iconID;

    public DeviceItem(LatLng mPosition) {
        this.mPosition = mPosition;

    }

    public DeviceItem(LatLng mPosition,int iconID){
        this.mPosition = mPosition;
        this.iconID = iconID;
    }
    public DeviceItem(LatLng mPosition,int iconID,String time){
        this.mPosition = mPosition;
        this.iconID = iconID;
        this.time = time;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        if (iconID == 0){
            iconID = R.drawable.icon_gcoding;
        }
        return BitmapDescriptorFactory
                .fromResource(iconID);
    }

}
