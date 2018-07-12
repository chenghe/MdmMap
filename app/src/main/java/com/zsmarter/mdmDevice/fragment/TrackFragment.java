package com.zsmarter.mdmDevice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zsmarter.mdmDevice.activity.MdmDeviceActivity;
import com.zsmarter.mdmDevice.clusterutil.DeviceBean;
import com.zsmarter.mdmdevice.R;

public class TrackFragment extends BaseFragment{

    private MdmDeviceActivity activity;
    private Button btTrack;
    private Button btResetTrack;
    View.OnClickListener onClickListener;

    public static TrackFragment getInstance(String fragmentTitle, MdmDeviceActivity activity) {
        TrackFragment sf = new TrackFragment();
        sf.fragmentTitle = fragmentTitle;
        sf.activity = activity;
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trace,container,false);
        btTrack = (Button) view.findViewById(R.id.bt_track);
        btResetTrack = (Button) view.findViewById(R.id.bt_reset_track);
        btResetTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.resetDeviceTrack();
            }
        });
        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.mClusterManager != null){
                    activity.resetDeviceLocation();
                }
                int checkItem = activity.chooseItemNum(activity.deviceList);
                for (DeviceBean item : activity.deviceList) {
                    if (item.isCheck()) {
                        if (checkItem>1){
                            Toast.makeText(activity,"设备轨迹只能选择单个设备，不支持设备多选。",Toast.LENGTH_SHORT).show();
                        }else if (checkItem==1){
                            activity.showDeviceTrack(item.getItems());
                        }else {
                            Toast.makeText(activity,"请选择设备",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        return view;
    }


    public void setTrackEnable(boolean enable){
        if (btTrack !=null){
            btTrack.setEnabled(enable);
        }
    }





}
