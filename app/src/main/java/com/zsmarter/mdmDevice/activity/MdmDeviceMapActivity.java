package com.zsmarter.mdmDevice.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zsmarter.mdmDevice.DeviceItem;
import com.zsmarter.mdmDevice.DownloadProgressButton;
import com.zsmarter.mdmDevice.adapter.MdmDeviceListAdapter;
import com.zsmarter.mdmDevice.network.LoginUtil;
import com.zsmarter.mdmDevice.network.ResponseUtil;
import com.zsmarter.mdmDevice.network.bean.AddressFenceResponseBody;
import com.zsmarter.mdmDevice.network.bean.DeviceListResponseBody;
import com.zsmarter.mdmDevice.network.bean.DeviceLocationResponseBody;
import com.zsmarter.mdmDevice.clusterutil.clustering.ClusterManager;
import com.zsmarter.mdmDevice.network.NetWorkUtil;
import com.zsmarter.mdmDevice.network.UrlConfig;
import com.zsmarter.mdmDevice.network.bean.OrganListBody;
import com.zsmarter.mdmDevice.network.bean.OrganListResponseBody;
import com.zsmarter.mdmDevice.network.bean.UserInfo;
import com.zsmarter.mdmDevice.util.MapUtil;
import com.zsmarter.mdmDevice.util.OrgChooseView;
import com.zsmarter.mdmDevice.util.PopDropDownMenu;
import com.zsmarter.mdmDevice.util.RadioGroup;
import com.zsmarter.mdmDevice.util.StringUtil;
import com.zsmarter.mdmDevice.util.ToastUtil;
import com.zsmarter.mdmdevice.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by hecheng on 2018/6/22
 */
public class MdmDeviceMapActivity extends BaseActivity implements MapUtil.DistrictCallBack, View.OnClickListener, MKOfflineMapListener, BaiduMap.OnMapLoadedCallback {

    private static final String LIST_DEFAULT = "0";
    private static final String LIST_ALL = "1";
    private Activity mActivity = MdmDeviceMapActivity.this;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private MKOfflineMap mOffline = null;
    private DownloadProgressButton downloadButton;
    private MapStatus ms;
    private DrawerLayout drawerLayout;
    private List<DeviceListResponseBody.Context.Result.RowsItem> devicesList;
    private Button btNow;
    private Button btHistory;
    private Button btStart;
    private Button btTrack;
    private Button btLogOut;
    private ImageView ivIcon;
    private RelativeLayout menu;
    private RelativeLayout rlFence;
    private CheckBox cbFence;
    private LinearLayout llHistory;
    private LinearLayout llDesc;
    private RadioGroup historyRadioGroup;
    private RecyclerView devicesListRV;
    private OrgChooseView orgChooseView;
    private PopDropDownMenu dropDownMenu;
    private MdmDeviceListAdapter mdmDeviceListAdapter;
    private List<Integer> iconList;
    private ClusterManager<DeviceItem> mClusterManager;
    private List<AddressFenceResponseBody.Context.Item> fenceList;//地址围栏列表
    private List<OrganListResponseBody.Context.Page.RowItem> orgList1;
    private List<OrganListResponseBody.Context.Page.RowItem> orgList2;
    private boolean ISNOW = true;
    private UserInfo userInfo;
    private QMUIPopup mNormalPopup;
    private int amDrawable;
    private int noonDrawable;
    private int pmDrawable;
    private int outDrawable;
    private int fenceNum;
    private Switch switchButton;

    private MapUtil mapUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm_device_map);

        initView();
        mapUtil = new MapUtil(mActivity);
        mapUtil.setDistrictCallBack(this);
        initData();

    }

    private void initData() {

        noonDrawable = R.drawable.icon_mark4;
        amDrawable = R.drawable.icon_mark1;
        pmDrawable = R.drawable.icon_mark8;
        outDrawable = R.drawable.icon_grey;

        iconList = new ArrayList<>();
//        iconList.add(R.drawable.icon_mark1);
        iconList.add(R.drawable.icon_mark2);
        iconList.add(R.drawable.icon_mark3);
//        iconList.add(R.drawable.icon_mark4);
        iconList.add(R.drawable.icon_mark5);
        iconList.add(R.drawable.icon_mark6);
        iconList.add(R.drawable.icon_mark7);
//        iconList.add(R.drawable.icon_mark8);
        iconList.add(R.drawable.icon_mark9);
        iconList.add(R.drawable.icon_mark10);
        iconList.add(R.drawable.icon_mark11);

        userInfo = new UserInfo();
        SharedPreferences sp = getSharedPreferences(LoginActivity.USER_INFO, MODE_PRIVATE);
        userInfo.setUserName(sp.getString(LoginActivity.USER_NAME, ""));
        userInfo.setOrgName(sp.getString(LoginActivity.USER_ORG_NAME, ""));
        userInfo.setOrgId(sp.getString(LoginActivity.USER_ORG_ID, ""));
        userInfo.setOrgType(sp.getString(LoginActivity.USER_ORG_TYPE, ""));
        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(userInfo.getUserName());
//        getDevicesList(UrlConfig.token, 1, 100);
        getAddressFence(UrlConfig.token);
//        getOrganize(UrlConfig.token, "90000000", LIST_DEFAULT);
    }

    private void initView() {
        showProgress("正在初始化地图...");
        menu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        btNow = findViewById(R.id.bt_now);
        btNow.setOnClickListener(this);
        btHistory = findViewById(R.id.bt_history);
        btHistory.setOnClickListener(this);
        btStart = findViewById(R.id.bt_start);
        btStart.setOnClickListener(this);
        btTrack = findViewById(R.id.bt_track);
        btTrack.setOnClickListener(this);
        btLogOut = findViewById(R.id.bt_logout);
        btLogOut.setOnClickListener(this);
        rlFence = findViewById(R.id.rl_fence);
        rlFence.setOnClickListener(this);
        cbFence = findViewById(R.id.cb_fence);

        llDesc = findViewById(R.id.ll_icon_desc);
        switchButton = findViewById(R.id.switch_button);
        switchButton.setOnClickListener(this);

        devicesListRV = findViewById(R.id.rl_device_list);
        devicesListRV.setLayoutManager(new LinearLayoutManager(this));
        llHistory = findViewById(R.id.ll_time);
        historyRadioGroup = findViewById(R.id.radiogroup);
        ivIcon = findViewById(R.id.iv_icon);
        ivIcon.setOnClickListener(this);
        orgChooseView = findViewById(R.id.org_choose_view);
        dropDownMenu = findViewById(R.id.drop_menu);
        dropDownMenu.setShowType(PopDropDownMenu.LEFT_ONLY);
        dropDownMenu.setMenuSelectedListener(new PopDropDownMenu.OnMenuSelectedListener() {
            @Override
            public void onSelected(View listview, int position, int type) {
                switch (type) {
                    case PopDropDownMenu.LEFT_LEFT_CLICK:
                        getOrganize(UrlConfig.token, orgList1.get(position).getOrgCode(), LIST_DEFAULT, true, false);
                        break;
                    case PopDropDownMenu.LEFT_RIGHT_CLICK:
                        getDevicesList(UrlConfig.token, "13081102", LIST_DEFAULT);
                        break;

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.switch_button:
                break;
            case R.id.iv_icon:
                showPortrait(view);
                break;
            case R.id.rl_fence:
                cbFence.setChecked(!cbFence.isChecked());
                break;
            case R.id.bt_logout:
                showBasicDialog("您确定要退出当前账号？", "确定", "取消", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        logOut(MdmDeviceMapActivity.this);
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        dissmissDialog();
                    }
                });

                break;
            case R.id.bt_now:
                showNowMenu();
                break;
            case R.id.bt_history:
                showHisMenu();
                break;
            case R.id.bt_start:
                if (mdmDeviceListAdapter != null && mdmDeviceListAdapter.getChooseItems().size() > 0) {
                    mapUtil.resetDeviceLocation();
                    drawerLayout.closeDrawer(menu);
                    if (!TextUtils.isEmpty(getChooseDevicesImei())) {
                        showProgress("正在获取数据...");
                        Log.i("hcb", "imei=======" + getChooseDevicesImei());
                        if (ISNOW) {
                            llDesc.setVisibility(View.GONE);
                            getDeviceLocation(UrlConfig.token, getChooseDevicesImei());
                        } else {
                            llDesc.setVisibility(View.VISIBLE);
                            if (switchButton.isChecked()) {
                                showDistrict(mdmDeviceListAdapter.getChooseItems());
                            } else {
                                getHistoryDeviceLocation(UrlConfig.token, getChooseDevicesImei(), calculateStartTime(), calculateEndTime());
                            }
                        }

                    }
                }
                break;
            case R.id.bt_track:
                if (mdmDeviceListAdapter != null && mdmDeviceListAdapter.getChooseItems().size() > 0) {
                    llDesc.setVisibility(View.VISIBLE);
                    mapUtil.resetDeviceTrack();
                    drawerLayout.closeDrawer(menu);
                    showProgress("正在获取数据...");
                    if (switchButton.isChecked()) {
                        showDistrict(mdmDeviceListAdapter.getChooseItems());
                    }
                    getHistoryDeviceTrack(UrlConfig.token, getChooseDevicesImei(), calculateStartTime(), calculateEndTime());
                }
                break;
        }
    }

    private void showNowMenu() {
        ISNOW = true;
        llHistory.setVisibility(View.GONE);
        btTrack.setVisibility(View.GONE);
        drawerLayout.openDrawer(menu);
        if (mdmDeviceListAdapter != null) {
            mdmDeviceListAdapter.setMutiChoose(ISNOW);
        }
    }

    private void showHisMenu() {
        ISNOW = false;
        llHistory.setVisibility(View.VISIBLE);
        btTrack.setVisibility(View.VISIBLE);
        drawerLayout.openDrawer(menu);
        if (mdmDeviceListAdapter != null) {
            mdmDeviceListAdapter.setMutiChoose(ISNOW);
        }
    }

    /**
     * 获取组织列表
     *
     * @param token
     * @param orgParentID
     * @param orgRange    0 为下级列表，1为全部列表（当获取全部时传1）
     */
    private void getOrganize(final String token, String orgParentID, String orgRange, final boolean getNextOrg, final boolean getDevice) {
        OrganListBody body = new OrganListBody(orgParentID, orgRange);
        LoginUtil.getNetWork().organList(token, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<OrganListResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(OrganListResponseBody organListResponseBody) {
                        Log.i("hcb", "getOrganize=======onNext");
                        try {
                            if (ResponseUtil.isTimeOut(organListResponseBody)) {
                                logOut(MdmDeviceMapActivity.this);
                            } else {
                                if (getNextOrg) {
                                    orgList2 = organListResponseBody.getContext().getPage().getRows();
                                    dropDownMenu.setmMenuLeftList2(orgList2);

                                    orgChooseView.notifyRight(orgList2);
                                    orgChooseView.setRightItemClickListenter(new OrgChooseView.ItemClickListenter() {
                                        @Override
                                        public void itemClick(int position, String dataId) {
                                            getDevicesList(UrlConfig.token, "13081102", LIST_DEFAULT);
                                        }
                                    });

                                } else {
                                    orgList1 = organListResponseBody.getContext().getPage().getRows();
                                    dropDownMenu.setmMenuLeftList1(orgList1);

                                    orgChooseView.notifyLeft(orgList1);
                                    orgChooseView.setLeftItemClickListenter(new OrgChooseView.ItemClickListenter() {
                                        @Override
                                        public void itemClick(int position, String dataId) {
                                            getOrganize(UrlConfig.token, dataId, LIST_DEFAULT, true, false);
                                        }
                                    });

                                }

                            }
                        } catch (Exception e) {
                            Log.i("hcb", "getOrganize=======onError====" + e.toString());
                            ToastUtil.showToast(mActivity, "获取信息失败");
                            logOut(mActivity);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getOrganize=======onError====" + e.toString());
                        ToastUtil.showToast(mActivity, "获取信息失败");
                        logOut(mActivity);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("hcb", "getOrganize=======onComplete");
                        dismissProgress();
                    }
                });

    }

    /**
     * 获取地址围栏列表
     */
    private void getAddressFence(String token) {
        NetWorkUtil.getNetWork().addressFenceList(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<AddressFenceResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddressFenceResponseBody addressFenceResponseBody) {
                        try {
                            if (addressFenceResponseBody.getContext() != null) {
                                fenceList = addressFenceResponseBody.getContext().getAddressFences();
                            }

                        } catch (Exception e) {
                            ToastUtil.showToast(mActivity, "获取信息失败");
                            logOut(mActivity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(mActivity, "获取信息失败");
                        logOut(mActivity);
                    }

                    @Override
                    public void onComplete() {
                        getOrganize(UrlConfig.token, "90000000", LIST_DEFAULT, false, false);
                    }
                });
    }


    /**
     * 获取设备列表
     *
     * @param token
     * @param orgId
     * @param orgRange
     */
    private void getDevicesList(String token, String orgId, String orgRange) {
        showProgress();
        NetWorkUtil.getNetWork().orgDeviceList(token, orgId, orgRange)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DeviceListResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeviceListResponseBody deviceListResponseBody) {
                        try {
                            devicesList = deviceListResponseBody.getContext().getResult().getRows();
                            devicesList = reFactorDeviceList(devicesList);
                            mdmDeviceListAdapter = new MdmDeviceListAdapter(MdmDeviceMapActivity.this, devicesList);
                            devicesListRV.setAdapter(mdmDeviceListAdapter);
                            Log.i("hcb", "onNext========" + devicesList.toString());
                        } catch (Exception e) {
                            Log.i("hcb", "onError=====" + e.toString());
                            ToastUtil.showToast(mActivity, "获取信息失败");
                            logOut(mActivity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "onError=====" + e.toString());
                        ToastUtil.showToast(mActivity, "获取信息失败");
                        logOut(mActivity);
                    }

                    @Override
                    public void onComplete() {
                        dismissProgress();
                    }
                });
    }

    /**
     * 获取设备坐标
     *
     * @param token
     * @param imei
     */
    private void getDeviceLocation(String token, String imei) {
        NetWorkUtil.getNetWork().deviceLovation(token, imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeviceLocationResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeviceLocationResponseBody deviceLocationResponseBody) {
                        Log.i("hcb", "getDeviceLocation=====onNext");
                        try {
                            if (deviceLocationResponseBody.getContext().getResult() != null) {
//                                initClusterManager(deviceLocationResponseBody.getContext().getResult());
                                mapUtil.initClusterManager(deviceLocationResponseBody.getContext().getResult(),
                                        mdmDeviceListAdapter.getChooseItems(), ISNOW);
                            }
                        } catch (Exception e) {
                            ToastUtil.showToast(mActivity, "获取信息失败");
                            logOut(mActivity);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getDeviceLocation=====e" + e.toString());
                        ToastUtil.showToast(mActivity, "获取信息失败");
                        logOut(mActivity);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("hcb", "getDeviceLocation=====onComplete");
                        mapUtil.showMutiLocationInfo();
                        dismissProgress();
                    }
                });
    }

    /**
     * 获取设备历史时间坐标
     *
     * @param token
     * @param imei
     * @param startDate
     * @param endDate
     */
    private void getHistoryDeviceLocation(String token, String imei, String startDate, String endDate) {
        NetWorkUtil.getNetWork().deviceHistoryLocation(token, imei, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeviceLocationResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeviceLocationResponseBody deviceLocationResponseBody) {
                        Log.i("hcb", "getHistoryDeviceLocation=====onNext");
                        try {
                            if (deviceLocationResponseBody.getContext().getResult() != null) {
//                                initClusterManager(deviceLocationResponseBody.getContext().getResult());
                                mapUtil.initClusterManager(deviceLocationResponseBody.getContext().getResult(),
                                        mdmDeviceListAdapter.getChooseItems(), ISNOW);
                            }
                        } catch (Exception e) {
                            ToastUtil.showToast(mActivity, "获取信息失败");
                            logOut(mActivity);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(mActivity, "获取信息失败");
                        logOut(mActivity);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("hcb", "getHistoryDeviceLocation=====onComplete");
                        mapUtil.showMutiLocationInfo();
                        dismissProgress();
                    }
                });
    }

    /**
     * 获取设备历史坐标并绘制轨迹
     *
     * @param token
     * @param imei
     * @param startDate
     * @param endDate
     */
    private void getHistoryDeviceTrack(String token, String imei, String startDate, String endDate) {
        NetWorkUtil.getNetWork().deviceHistoryLocation(token, imei, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeviceLocationResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeviceLocationResponseBody deviceLocationResponseBody) {
                        Log.i("hcb", "getHistoryDeviceLocation=====onNext");
                        try {
                            if (deviceLocationResponseBody.getContext().getResult() != null) {
                                mapUtil.initClusterManager(deviceLocationResponseBody.getContext().getResult(), mdmDeviceListAdapter.getChooseItems(), ISNOW);
                            }
                        } catch (Exception e) {
                            ToastUtil.showToast(mActivity, "获取信息失败");
                            logOut(mActivity);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(mActivity, "获取信息失败");
                        logOut(mActivity);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("hcb", "getHistoryDeviceLocation=====onComplete");

                        mapUtil.showDeviceTrack();
                        dismissProgress();
                    }
                });
    }


    private String getChooseDevicesImei() {
        String imei = "";
        mdmDeviceListAdapter.getChooseItems();
        for (DeviceListResponseBody.Context.Result.RowsItem item : mdmDeviceListAdapter.getChooseItems()) {
            if (TextUtils.isEmpty(imei)) {
                imei = item.getDeviceCode();
            } else {
                imei += "," + item.getDeviceCode();
            }
        }
        return imei;
    }

    private List<DeviceListResponseBody.Context.Result.RowsItem> reFactorDeviceList(List<DeviceListResponseBody.Context.Result.RowsItem> list) {
        //设置图钉
        int iconIndex = 0;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            DeviceListResponseBody.Context.Result.RowsItem item = (DeviceListResponseBody.Context.Result.RowsItem) iterator.next();
            if (iconIndex < iconList.size()) {
                item.setDeviceIcon(iconList.get(iconIndex));
                iconIndex++;
            } else {
                iconIndex = 0;
            }
        }

        //设置地址围栏
        if (fenceList != null && fenceList.size() > 0) {
            List<String> cityList = new ArrayList<>();
            List<String> districtList = new ArrayList<>();

            for (DeviceListResponseBody.Context.Result.RowsItem deviceItem : list) {
                cityList.clear();
                districtList.clear();
                for (AddressFenceResponseBody.Context.Item fenceItem : fenceList) {
                    if (!TextUtils.isEmpty(deviceItem.getAddressFence())) {
                        String[] addressID = StringUtil.addressFenceSplit(deviceItem.getAddressFence());
                        for (int i = 0; i < addressID.length; i++) {
                            if (addressID[i].equals(fenceItem.getOid())) {
                                cityList.add(fenceItem.getCityName());
                                districtList.add(fenceItem.getDistrictName());
                            }
                        }
                    }
                }
                deviceItem.setFenceCityName(cityList);
                deviceItem.setFencedistrictName(districtList);
            }

        }


        return list;
    }


    private String calculateStartTime() {
        String time = "";
        switch (historyRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_first:
                time = "2018-06-22 16:46:31";
                break;
            case R.id.rb_second:
                time = "2018-06-22 13:46:31";
                break;
            case R.id.rb_third:
                time = "2018-06-22 7:46:31";
                break;
            case R.id.rb_fourth:
                time = "2018-06-21 19:46:31";
                break;
        }
        return time;
    }

    private String calculateEndTime() {
        String time = "2018-06-22 19:46:31";
        return time;
    }

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }


    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }


    /**
     * 开始绘制地址围栏（按照行政区绘制）
     *
     * @param chooseDevices 设备列表
     */
    private void showDistrict(List<DeviceListResponseBody.Context.Result.RowsItem> chooseDevices) {
        Log.i("hcb", "showDistrict");
        for (DeviceListResponseBody.Context.Result.RowsItem item : chooseDevices) {
            if (item.getFenceCityName() != null && item.getFenceCityName().size() > 0) {
                fenceNum = item.getFenceCityName().size();
                for (int i = 0; i < item.getFenceCityName().size(); i++) {
                    mapUtil.searchDistrict(item.getFenceCityName().get(i), item.getFencedistrictName().get(i), fenceNum);
                }
            }
        }
    }


    @Override
    public void getDistrictResult(DistrictResult districtResult) {
        getHistoryDeviceLocation(UrlConfig.token, getChooseDevicesImei(), calculateStartTime(), calculateEndTime());

    }

    /**
     * 显示头像内容
     *
     * @param v 锚点view
     */
    private void showPortrait(View v) {

        mNormalPopup = new QMUIPopup(this, QMUIPopup.DIRECTION_NONE);
        View content = LayoutInflater.from(this).inflate(R.layout.popup_portrait, null);
        content.setLayoutParams(mNormalPopup.generateLayoutParam(
                QMUIDisplayHelper.dp2px(this, 250),
                QMUIDisplayHelper.dp2px(this, 100)
        ));
        TextView tvName = content.findViewById(R.id.tv_name);
        tvName.setText(userInfo.getUserName());
        Button btLogout = content.findViewById(R.id.bt_logout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBasicDialog("您确定要退出当前账号？", "确定", "取消", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        logOut(MdmDeviceMapActivity.this);
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        dissmissDialog();
                    }
                });
            }
        });
        mNormalPopup.setContentView(content);
        mNormalPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mNormalPopup.show(v);
    }


}
