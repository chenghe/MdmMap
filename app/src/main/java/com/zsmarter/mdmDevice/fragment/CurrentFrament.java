package com.zsmarter.mdmDevice.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zsmarter.mdmDevice.activity.MdmDeviceActivity;
import com.zsmarter.mdmdevice.R;

/**
 * 实时定位
 */
public class CurrentFrament extends BaseFragment{
    private MdmDeviceActivity activity;
    private Button btLocation;

    public static CurrentFrament getInstance(String fragmentTitle, MdmDeviceActivity activity){
        CurrentFrament sf = new CurrentFrament();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current,container,false);
        btLocation = (Button) view.findViewById(R.id.bt_location);
        btLocation.setOnClickListener(new View.OnClickListener() {
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
