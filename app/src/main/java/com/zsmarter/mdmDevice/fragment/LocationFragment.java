package com.zsmarter.mdmDevice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zsmarter.mdmDevice.activity.MdmDeviceActivity;
import com.zsmarter.mdmdevice.R;

/**
 * 定位选择Fragment
 */
public class LocationFragment extends BaseFragment{

    private MdmDeviceActivity activity;

    public static LocationFragment getInstance(String fragmentTitle, MdmDeviceActivity activity) {
        LocationFragment sf = new LocationFragment();
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
        View view = inflater.inflate(R.layout.fragment_location,container,false);
        Button bt = (Button) view.findViewById(R.id.bt_location);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.mClusterManager == null) {
                    activity.prepareLocation();
                }else {
                    activity.resetDeviceLocation();
                    activity.prepareLocation();
                }
            }
        });
        return view;
    }
}
