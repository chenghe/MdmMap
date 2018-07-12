package com.zsmarter.mdmDevice.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.zsmarter.mdmDevice.DeviceItem;
import com.zsmarter.mdmDevice.activity.MdmDeviceActivity;
import com.zsmarter.mdmDevice.clusterutil.DeviceBean;
import com.zsmarter.mdmdevice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史位置
 */
public class HistoryFrament extends BaseFragment implements View.OnClickListener {

    public static final int HOUR_3 = 1;
    public static final int HOUR_5 = 2;
    public static final int HOUR_12 = 3;
    public static final int DAY_1 = 4;
    private MdmDeviceActivity activity;
    private RadioGroup radioGroup;
    private Button btLocation;
    private Button btTrack;
    private Button btResetTrack;
    private int timeType;


    public static HistoryFrament getInstance(String fragmentTitle, MdmDeviceActivity activity) {
        HistoryFrament sf = new HistoryFrament();
        sf.activity = activity;
        sf.fragmentTitle = fragmentTitle;
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        btLocation = (Button) view.findViewById(R.id.bt_location);
        btLocation.setOnClickListener(this);
        btTrack = (Button) view.findViewById(R.id.bt_track);
        btTrack.setOnClickListener(this);
        btResetTrack = (Button) view.findViewById(R.id.bt_reset_track);
        btResetTrack.setOnClickListener(this);
        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_first:
                        timeType = HOUR_3;
                        break;
                    case R.id.rb_second:
                        timeType = HOUR_5;
                        break;
                    case R.id.rb_third:
                        timeType = HOUR_12;
                        break;
                    case R.id.rb_fourth:
                        timeType = DAY_1;
                        break;
                }

            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_location:
                int chooseNum = activity.chooseItemNum(activity.deviceList);
                if (chooseNum>0){
                    for (DeviceBean item : activity.deviceList) {
                        if (item.isCheck()) {
                            List<DeviceItem> deviceItems = prepareTrace(timeType, item.getItems());
                            if (activity.mClusterManager != null) {
                                activity.resetDeviceLocation();
                            }
                            activity.prepareLocation(deviceItems);

                        }
                    }
                }else {
                    if (activity.mClusterManager != null) {
                        activity.resetDeviceLocation();
                    }
                }
                break;
            case R.id.bt_track:
                activity.resetDeviceTrack();
                showTrack();
//                if (activity.mClusterManager != null) {
//                    activity.resetDeviceLocation();
//                }else if (activity.mHandler!=null){
//                    activity.resetDeviceTrack();
//                }else {
//                    showTrack();
//                }
                break;
            case R.id.bt_reset_track:
                activity.resetDeviceTrack();
                break;
        }
    }

    public void showTrack(){
        int checkItem = activity.chooseItemNum(activity.deviceList);
        for (DeviceBean item : activity.deviceList) {
            if (item.isCheck()) {
                List<DeviceItem> deviceItems = prepareTrace(timeType, item.getItems());
                activity.showDeviceTrack(deviceItems);
            }
        }
    }


    private List<DeviceItem> prepareTrace(int type, List<DeviceItem> items) {
        List<DeviceItem> deviceItems = null;
        switch (type) {
            case HOUR_3:
                deviceItems = prepareTraceData(3, items);
                break;
            case HOUR_5:
                deviceItems = prepareTraceData(4, items);
                break;
            case HOUR_12:
                deviceItems = prepareTraceData(5, items);
                break;
            case DAY_1:
                deviceItems = prepareTraceData(6, items);
                break;
        }

        return deviceItems;
    }

    private List<DeviceItem> prepareTraceData(int num, List<DeviceItem> items) {
        List<DeviceItem> deviceItems = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (items.get(i) != null) {
                deviceItems.add(items.get(i));
            }
        }
        return deviceItems;
    }


    public void setTrackEnable(boolean enable) {
        if (btTrack != null) {
            btTrack.setEnabled(enable);
        }
    }
}
