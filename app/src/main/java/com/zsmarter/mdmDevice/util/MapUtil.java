package com.zsmarter.mdmDevice.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.zsmarter.mdmDevice.DeviceItem;
import com.zsmarter.mdmDevice.activity.MdmDeviceMapActivity;
import com.zsmarter.mdmDevice.clusterutil.clustering.ClusterManager;
import com.zsmarter.mdmDevice.network.UrlConfig;
import com.zsmarter.mdmDevice.network.bean.DeviceListResponseBody;
import com.zsmarter.mdmDevice.network.bean.DeviceLocationResponseBody;
import com.zsmarter.mdmdevice.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hecheng on 2018/7/10
 */
public class MapUtil implements OnGetDistricSearchResultListener, MKOfflineMapListener, BaiduMap.OnMapLoadedCallback {
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 5;
    private static final double DISTANCE = 0.00002;
    private static final LatLng GEO_CHENDE = new LatLng(40.959, 117.963);
    private static final float DEFAULT_ZOOM = 13.0f;

    private Activity activity;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private MKOfflineMap mOffline = null;
    private UiSettings mUiSettings;
    private DistrictSearch mDistrictSearch;
    private List<List<List<LatLng>>> polylines = new ArrayList<>();//地址围栏
    private ClusterManager<DeviceItem> mClusterManager;
    private List<DeviceItem> showDevices = new ArrayList<>();//显示设备的坐标（在历史选项中使用）
    private int amDrawable;
    private int noonDrawable;
    private int pmDrawable;
    private int outDrawable;
    private int fenceNum;
    private DistrictCallBack districtCallBack;

    private volatile boolean trackStact = false;
    private volatile boolean stopStact = false;

    public MapUtil(Activity activity) {
        this.activity = activity;
        initMap();
    }

    private void initMap() {

        noonDrawable = R.drawable.icon_mark4;
        amDrawable = R.drawable.icon_mark1;
        pmDrawable = R.drawable.icon_mark8;
        outDrawable = R.drawable.icon_grey;

        mOffline = new MKOfflineMap();
        mOffline.init(this);

        // 地图初始化
        mMapView = (MapView) activity.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
//        BaiduMapOptions options = new BaiduMapOptions();
//        MapStatus.Builder mapstatusBuilder = new MapStatus.Builder();
//        mapstatusBuilder.target(new LatLng(40.959272, 117.963851));
//        MapStatus mapStatus = mapstatusBuilder.build();
//        options.mapStatus(mapStatus);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setOverlookingGesturesEnabled(false);//俯视
        mUiSettings.setRotateGesturesEnabled(false);//旋转
//        MapStatusUpdate chengdeStatus = MapStatusUpdateFactory.newLatLng(GEO_CHENDE);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(DEFAULT_ZOOM);
        builder.target(GEO_CHENDE);
        MapStatusUpdate chengdeStatus = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.setMapStatus(chengdeStatus);
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);
    }

    /**
     * 开始绘制地址围栏（按照行政区绘制）
     */
    public void searchDistrict(String cityName, String districtName, int fenceNum) {
        if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(districtName)) {
            this.fenceNum = fenceNum;
            mDistrictSearch.searchDistrict(new DistrictSearchOption()
                    .cityName(cityName).districtName(districtName));
        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }

    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        int color = 0xAA00FF00;

//        mBaiduMap.clear();
        if (districtResult == null) {
            return;
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
//            this.polylines.clear();
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            this.polylines.add(polyLines);
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                        .points(polyline).dottedLine(true).color(color);
                mBaiduMap.addOverlay(ooPolyline11);
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                        .stroke(new Stroke(5, 0xAA00FF88)).fillColor(0xAAFFFF00);


                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }


            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));

            if (districtCallBack != null && this.polylines.size() == (fenceNum)) {
                districtCallBack.getDistrictResult(districtResult);
            }
        }

    }

    public void resetDeviceLocation() {
        if (mClusterManager != null) {
            mClusterManager.clearItems();
            mClusterManager = null;
            mBaiduMap.clear();
            notifyMapView(GEO_CHENDE, DEFAULT_ZOOM);
        }
    }

    public void resetDeviceTrack() {
        if (mClusterManager != null && showDevices != null) {
            mClusterManager.clearItems();
            mClusterManager = null;
            showDevices.clear();
            stopStact = true;
        }

        mBaiduMap.clear();
        notifyMapView(GEO_CHENDE, DEFAULT_ZOOM);

    }

    /**
     * 初始化显示坐标对象
     */
    public void initClusterManager(List<DeviceLocationResponseBody.Context.ResultItem> list,
                                   List<DeviceListResponseBody.Context.Result.RowsItem> chooseItems,
                                   boolean status) {
        try {
//            resetDeviceLocation();

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<DeviceItem>(activity, mBaiduMap);
            }

//            mClusterManager.clearItems();
            showDevices.clear();
            for (DeviceLocationResponseBody.Context.ResultItem item : list) {
                for (DeviceListResponseBody.Context.Result.RowsItem chooseItem : chooseItems) {
                    if (item.getImei().equals(chooseItem.getDeviceCode())) {
                        double latitude = Double.parseDouble(item.getLatitude());
                        double longitude = Double.parseDouble(item.getLongitude());
                        DeviceItem deviceItem = null;
                        if (status) {
                            deviceItem = new DeviceItem(new LatLng(latitude, longitude),
                                    chooseItem.getDeviceIcon(), item.getLocaltionTime());
                            showDevices.add(deviceItem);
                        } else {
                            if (polylines != null && polylines.size() > 0) {
                                List<Boolean> outList = new ArrayList<>();
                                for (List<List<LatLng>> polys : polylines) {
                                    for (List<LatLng> pol : polys) {
                                        outList.add(!SpatialRelationUtil.isPolygonContainsPoint(pol, new LatLng(latitude, longitude)));
                                    }
                                }
                                if (outList.size() > 0) {
                                    boolean isOut = outList.get(0);
                                    if (outList.size() > 1) {
                                        for (int i = 1; i < outList.size(); i++) {
                                            if (isOut && outList.get(i)) {
                                                isOut = true;
                                            } else {
                                                isOut = false;
                                            }
                                        }
                                    }
                                    item.setOut(isOut);
                                }
                            }


                            if (item.isOut()) {
                                deviceItem = new DeviceItem(new LatLng(latitude, longitude),
                                        outDrawable, item.getLocaltionTime());
                                showDevices.add(deviceItem);
                            } else {
                                if (StringUtil.refectTime(item.getLocaltionTime()).equals(StringUtil.AM)) {
                                    deviceItem = new DeviceItem(new LatLng(latitude, longitude),
                                            amDrawable, item.getLocaltionTime());
                                    showDevices.add(deviceItem);
                                } else if (StringUtil.refectTime(item.getLocaltionTime()).equals(StringUtil.NOON)) {
                                    deviceItem = new DeviceItem(new LatLng(latitude, longitude),
                                            noonDrawable, item.getLocaltionTime());
                                    showDevices.add(deviceItem);
                                } else if (StringUtil.refectTime(item.getLocaltionTime()).equals(StringUtil.PM)) {
                                    deviceItem = new DeviceItem(new LatLng(latitude, longitude),
                                            pmDrawable, item.getLocaltionTime());
                                    showDevices.add(deviceItem);
                                } else {
                                    deviceItem = new DeviceItem(new LatLng(latitude, longitude),
                                            item.getDeviceIcon(), item.getLocaltionTime());
                                    showDevices.add(deviceItem);
                                }
                            }

                        }
                    }
                }
            }


            Iterator iterator = showDevices.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                DeviceItem item = (DeviceItem) iterator.next();
                if (index<4) {
                    iterator.remove();
                    index++;
                }
            }

            polylines.clear();
            mClusterManager.addItems(showDevices);

        } catch (Exception e) {
            Log.i("hcb", "initClusterManager error====" + e.toString());
        }

    }

    private void addMapListener() {
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
    }

    /**
     * 显示位置信息
     */
    public void showLocationInfo(DeviceItem item) {
        OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
                .fontSize(20).fontColor(0xFFFF00FF).text(item.getTime()).rotate(0)
                .position(item.getPosition());
        mBaiduMap.addOverlay(ooText);

    }

    public void showMutiLocationInfo() {
        if (showDevices != null) {
            for (DeviceItem item : showDevices) {
                showLocationInfo(item);
            }
            addMapListener();
            notifyMapView(showDevices.get(0).getPosition(), 17.0f);
//            notifyMapView(GEO_CHENDE, DEFAULT_ZOOM);
        }
    }

    private void notifyMapView(LatLng latLng, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        builder.zoom(zoom);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public void showDeviceTrack() {
        if (showDevices != null && showDevices.size() > 0) {
            trackStact = true;
            stopStact = false;
            mClusterManager = new ClusterManager<DeviceItem>(activity, mBaiduMap);
            notifyMapView(showDevices.get(0).getPosition(), 17.0f);
            LatLng[] latlngs = new LatLng[showDevices.size()];
            for (int i = 0; i < showDevices.size(); i++) {
                latlngs[i] = showDevices.get(i).getPosition();
            }

            List<LatLng> polylines = new ArrayList<>();
            for (DeviceItem item : showDevices) {
                polylines.add(item.getPosition());
            }

//        mHandler = new Handler(Looper.getMainLooper());

            moveLooper(latlngs,drawPolyLine(polylines));
            mMapView.showZoomControls(false);

        }

    }


    private Marker drawPolyLine(List<LatLng> polylines) {
//        polylines.clear();
//        for (int index = 0; index < latlngs.length; index++) {
//            polylines.add(latlngs[index]);
//        }
//
//        polylines.add(latlngs[0]);
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
        textureList.add(mBlueTexture);
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(0);
//        textureIndexs.add(1);
//        textureIndexs.add(2);

        PolylineOptions polylineOptions = new PolylineOptions().width(20).points(polylines)
                .dottedLine(true).customTextureList(textureList).textureIndex(textureIndexs);

        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
        OverlayOptions markerOptions;
        markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).position(polylines.get(0))
                .rotate((float) getAngle(0, mPolyline));
        Marker mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
        return mMoveMarker;
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex, Polyline mPolyline) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }


    /**
     * 循环进行移动逻辑
     */
    public void moveLooper(final LatLng[] latlngs, final Marker moveMarker) {
        new Thread() {
            Marker mMoveMarker = moveMarker;
            public void run() {

                while (trackStact) {
                    Log.i("hcb", "trackStart===" + trackStact);
//                    if (stopStact) {
//                        trackStact = false;
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                btTrack.setEnabled(true);
////                                trackFragment.setTrackEnable(true);
////                                mHandler = null;
//                            }
//                        });
//
//                        break;
//                    }
                    for (int i = 0; i < latlngs.length - 1; i++) {

                        final LatLng startPoint = latlngs[i];
                        final LatLng endPoint = latlngs[i + 1];
                        mMoveMarker.setPosition(startPoint);
//                        runnable1 = new Runnable() {
//                            @Override
//                            public void run() {
                        // refresh marker's rotate
//                                if (mMapView == null) {
//                                    return;
//                                }
                        mMoveMarker.setRotate((float) getAngle(startPoint,
                                endPoint));
//                            }
//                        };

//                        mHandler.post(runnable1);
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);

                        double intercept = getInterception(slope, startPoint);

                        double xMoveDistance = isReverse ? getXMoveDistance(slope) :
                                -1 * getXMoveDistance(slope);


                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse);
                             j = j - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }

                            final LatLng finalLatLng = latLng;
//                             runnable2 = new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (mMapView == null) {
//                                        return;
//                                    }
                            mMoveMarker.setPosition(finalLatLng);
//                                }
//                            };
//                            mHandler.post(runnable2);

                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
//
        }.start();
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }



    public BaiduMap getmBaiduMap() {
        return mBaiduMap;
    }

    public MapView getmMapView() {
        return mMapView;
    }

    public DistrictCallBack getDistrictCallBack() {
        return districtCallBack;
    }

    public void setDistrictCallBack(DistrictCallBack districtCallBack) {
        this.districtCallBack = districtCallBack;
    }

    public interface DistrictCallBack {

        public void getDistrictResult(DistrictResult districtResult);
    }
}


