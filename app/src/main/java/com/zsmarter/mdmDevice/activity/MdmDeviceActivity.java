package com.zsmarter.mdmDevice.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.flyco.tablayout.SlidingTabLayout;
import com.zsmarter.mdmDevice.DeviceItem;
import com.zsmarter.mdmDevice.DeviceListAdapter;
import com.zsmarter.mdmDevice.DownloadProgressButton;
import com.zsmarter.mdmDevice.MyPageAdapter;
import com.zsmarter.mdmDevice.clusterutil.DeviceBean;
import com.zsmarter.mdmDevice.clusterutil.clustering.ClusterManager;
import com.zsmarter.mdmDevice.fragment.BaseFragment;
import com.zsmarter.mdmDevice.fragment.CurrentFrament;
import com.zsmarter.mdmDevice.fragment.HistoryFrament;
import com.zsmarter.mdmDevice.fragment.LocationFragment;
import com.zsmarter.mdmDevice.fragment.TrackFragment;
import com.zsmarter.mdmdevice.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MdmDeviceActivity extends BaseActivity implements MKOfflineMapListener, BaiduMap.OnMapLoadedCallback {

    private LatLng[] device1loca = new LatLng[]{
            new LatLng(40.957119, 117.969043),
            new LatLng(40.958454, 117.969169),
            new LatLng(40.956711, 117.974236),
            new LatLng(40.957664, 117.962162),
            new LatLng(40.957092, 117.973337),
            new LatLng(40.961083, 117.975224)
    };

    private LatLng[] device2loca = new LatLng[]{
            new LatLng(40.994076, 117.918343),
            new LatLng(40.998323, 117.925386),
            new LatLng(40.985472, 117.924811),
            new LatLng(41.00159, 117.912594),
            new LatLng(41.009429, 117.92093),
            new LatLng(40.982858, 117.947664)
    };

    private LatLng[] device3loca = new LatLng[]{
            new LatLng(40.973327, 117.978451),
            new LatLng(40.981061, 118.000873),
            new LatLng(40.996309, 118.018839),
            new LatLng(40.984656, 118.007053),
            new LatLng(40.973436, 118.038098),
            new LatLng(40.989121, 117.997423)
    };

    private LatLng[] device4loca = new LatLng[]{
            new LatLng(40.977358, 117.833141),
            new LatLng(40.985309, 117.81345),
            new LatLng(40.971257, 117.815319),
            new LatLng(40.991191, 117.843921),
            new LatLng(40.995547, 117.840615),
            new LatLng(40.995329, 117.838315)
    };

    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 5;
    private static final double DISTANCE = 0.00002;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private MKOfflineMap mOffline = null;
    private DownloadProgressButton downloadButton;
    private UiSettings mUiSettings;
    private static final LatLng GEO_CHENDE = new LatLng(40.959, 117.963);
    private static final float DEFAULT_ZOOM = 13.0f;
    private MapStatus ms;
    private Button btLocation;
    private Button btTrack;
    private Button btResetLocation;
    private Button btResetTrack;
    public Handler mHandler;
    private Polyline mPolyline;
    private Marker mMoveMarker;
    private LatLng[] latlngs;
    Runnable runnable1;
    Runnable runnable2;
    private volatile boolean trackStact = false;
    private volatile boolean stopStact = false;
    private DeviceBean device1;
    private DeviceBean device2;
    private DeviceBean device3;
    private DeviceBean device4;
    private RecyclerView recyclerView;
    private DeviceListAdapter adapter;

    private List<BaseFragment> fragments;
    private LocationFragment locationFragment;
    private TrackFragment trackFragment;
    private HistoryFrament historyFrament;
    private CurrentFrament currentFrament;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private MyPageAdapter myPageAdapter;
    private List<LatLng> polylines;
    private MdmDeviceActivity activity = MdmDeviceActivity.this;

    public ClusterManager<DeviceItem> mClusterManager;
    public List<DeviceBean> deviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm_device);
        initMap();
        initButton();
        device1 = new DeviceBean(R.drawable.icon_mark1, device1loca, "设备1");
        device2 = new DeviceBean(R.drawable.icon_mark2, device2loca, "设备2");
        device3 = new DeviceBean(R.drawable.icon_mark3, device3loca, "设备3");
        device4 = new DeviceBean(R.drawable.icon_mark4, device4loca, "设备4");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        deviceList = new ArrayList<>();
        deviceList.add(device1);
        deviceList.add(device2);
        deviceList.add(device3);
        deviceList.add(device4);
        adapter = new DeviceListAdapter(deviceList, this);
        recyclerView.setAdapter(adapter);
    }


    private void initMap() {
        mOffline = new MKOfflineMap();
        mOffline.init(this);

        downloadButton = (DownloadProgressButton) findViewById(R.id.download);
        downloadButton.setEnablePause(false);
//        downloadButton.setText("地图包已经最新");
//        downloadButton.setEnabled(false);
        downloadButton.setOnDownLoadClickListener(downLoadClickListener);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
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

        polylines = new ArrayList<>();
    }

    private void initButton() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setCurrentItem(2);
        currentFrament = CurrentFrament.getInstance("实时", activity);
//        trackFragment = TrackFragment.getInstance("轨迹", activity);
        historyFrament = HistoryFrament.getInstance("历史", activity);
        fragments = new ArrayList<>();
        fragments.add(currentFrament);
        fragments.add(historyFrament);
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("hcb", "onPageScrolled=====" + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("hcb", "onPageSelected=====" + position);
                adapter.clearChoose();
                switch (position) {
                    case 0:
                        resetDeviceTrack();
                        adapter.mutiChoose = false;
                        break;
                    case 1:
                        resetDeviceLocation();
                        if (runnable1 != null) {
                            historyFrament.setTrackEnable(true);
                        }
                        adapter.mutiChoose = true;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        tabLayout.setViewPager(viewPager);


        btLocation = (Button) findViewById(R.id.bt_location);
        btLocation.setOnClickListener(clickListener);
        btTrack = (Button) findViewById(R.id.bt_track);
        btTrack.setOnClickListener(clickListener);
        btResetTrack = (Button) findViewById(R.id.bt_reset_track);
        btResetTrack.setOnClickListener(clickListener);
        btResetLocation = (Button) findViewById(R.id.bt_reset_location);
        btResetLocation.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_location:
                    if (mClusterManager == null) {
                        prepareLocation();
                    } else {
                        resetDeviceLocation();
                        prepareLocation();
                    }
                    break;
                case R.id.bt_track:
                    int checkItem = chooseItemNum(deviceList);
                    for (DeviceBean item : deviceList) {
                        if (item.isCheck()) {
                            if (checkItem > 1) {
                                Toast.makeText(MdmDeviceActivity.this, "设备轨迹只能选择单个设备，不支持设备多选。", Toast.LENGTH_SHORT).show();
                            } else if (checkItem == 1) {
                                showDeviceTrack(item.getItems());
                            } else {
                                Toast.makeText(MdmDeviceActivity.this, "请选择设备", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case R.id.bt_reset_track:
                    resetDeviceTrack();
                    break;
                case R.id.bt_reset_location:
                    resetDeviceLocation();
                    break;
            }
        }
    };

    private void notifyMapView(LatLng latLng, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        builder.zoom(zoom);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    public void prepareLocation() {
        mClusterManager = new ClusterManager<DeviceItem>(MdmDeviceActivity.this, mBaiduMap);
        int checkItem = chooseItemNum(deviceList);
        for (DeviceBean item : deviceList) {
            if (item.isCheck()) {
                if (checkItem > 1) {
                    showDeviceLocation(item.getItems().get(0));
                } else if (checkItem == 1) {
                    showDeviceLocation(item.getItems());
                }
            }
        }
        notifyMapView(GEO_CHENDE, DEFAULT_ZOOM);
    }


    public void prepareLocation(List<DeviceItem> items) {
        mClusterManager = new ClusterManager<DeviceItem>(MdmDeviceActivity.this, mBaiduMap);

        showDeviceLocation(items);
        notifyMapView(GEO_CHENDE, DEFAULT_ZOOM);
    }

    public void showDeviceLocation(List<DeviceItem> items) {

        mClusterManager.addItems(items);
        for (DeviceItem item : items) {
            showLocationInfo(item);
        }

        addMapListener();
    }

    public void showDeviceLocation(DeviceItem item) {

        mClusterManager.addItem(item);
        showLocationInfo(item);
        addMapListener();
    }

    /**
     * 显示位置信息
     */
    private void showLocationInfo(DeviceItem item) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
                .fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
                .position(item.getPosition());
        mBaiduMap.addOverlay(ooText);
    }

    private void addMapListener() {
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
    }

    public int chooseItemNum(List<DeviceBean> list) {
        int checkItems = 0;
        for (DeviceBean item : list) {
            if (item.isCheck()) {
                checkItems++;
            }
        }
        return checkItems;
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
        if (mHandler != null) {
            mBaiduMap.clear();
            stopStact = true;
//        btTrack.setEnabled(false);
//        trackFragment.setTrackEnable(false);
            latlngs = new LatLng[]{};
            mHandler.removeCallbacks(runnable1);
            mHandler.removeCallbacks(runnable2);

//        mHandler = null;
            mPolyline = null;
//        mMoveMarker = null;
            notifyMapView(GEO_CHENDE, DEFAULT_ZOOM);
        }
    }

    public void showDeviceTrack(List<DeviceItem> items) {

        notifyMapView(items.get(0).getPosition(), 18.0f);
        latlngs = new LatLng[items.size()];
        for (int i = 0; i < items.size(); i++) {
            latlngs[i] = items.get(i).getPosition();
        }

        trackStact = true;
        stopStact = false;
        mHandler = new Handler(Looper.getMainLooper());
        drawPolyLine();
//        moveLooper();
        mMapView.showZoomControls(false);


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

    private void drawPolyLine() {
        polylines.clear();
        for (int index = 0; index < latlngs.length; index++) {
            polylines.add(latlngs[index]);
        }

        polylines.add(latlngs[0]);
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
        textureList.add(mBlueTexture);
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(0);
//        textureIndexs.add(1);
//        textureIndexs.add(2);

        PolylineOptions polylineOptions = new PolylineOptions().width(20).points(polylines)
                .dottedLine(true).customTextureList(textureList).textureIndex(textureIndexs);

        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
        OverlayOptions markerOptions;
        markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).position(polylines.get(0))
                .rotate((float) getAngle(0));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);

    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
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
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 循环进行移动逻辑
     */
    public void moveLooper() {
        new Thread() {

            public void run() {

                while (trackStact) {
                    Log.i("hcb", "trackStart===" + trackStact);
                    if (stopStact) {
                        trackStact = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                btTrack.setEnabled(true);
//                                trackFragment.setTrackEnable(true);
                                mHandler = null;
                                historyFrament.showTrack();
                            }
                        });

                        break;
                    }
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


    DownloadProgressButton.OnDownLoadClickListener downLoadClickListener = new DownloadProgressButton.OnDownLoadClickListener() {
        @Override
        public void clickDownload() {
            startDownLoad();
        }

        @Override
        public void clickPause() {

        }

        @Override
        public void clickResume() {

        }

        @Override
        public void clickFinish() {

        }
    };

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
//                    if (downloadButton.getState() == DownloadProgressButton.FINISH) {
//                        sub.unsubscribe();
//                        return;
//                    }
                    downloadButton.setProgress(downloadButton.getProgress() + update.status);
                }
            }
            break;

            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                downloadButton.setEnabled(true);
                downloadButton.setText("开始下载");
                break;

            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                break;

            default:
                break;
        }
    }

    /**
     * 开始下载
     */
    public void startDownLoad() {
        int cityid = 12;
        Log.i("hcb", "cityid=======" + cityid);
        mOffline.start(cityid);
//        clickLocalMapListButton(null);
//        updateView();
    }

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }
}
