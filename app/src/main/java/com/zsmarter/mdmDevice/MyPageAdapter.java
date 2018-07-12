package com.zsmarter.mdmDevice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zsmarter.mdmDevice.fragment.BaseFragment;

import java.util.List;

public class MyPageAdapter extends FragmentPagerAdapter{


    private List<?extends BaseFragment> fragments;
    public MyPageAdapter(FragmentManager fm ,List<?extends BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).fragmentTitle;
    }
}
