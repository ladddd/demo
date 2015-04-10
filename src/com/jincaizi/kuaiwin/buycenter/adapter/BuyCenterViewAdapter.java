package com.jincaizi.kuaiwin.buycenter.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.jincaizi.common.IntentData;
import com.jincaizi.kuaiwin.buycenter.BuyFragment;
import com.jincaizi.kuaiwin.buycenter.OtherLotteryFragment;

/**
 * Created by chenweida on 2015/3/30.
 */
public class BuyCenterViewAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public BuyCenterViewAdapter(FragmentManager fm) {
        super(fm);

        fragments = new Fragment[4];

        for (int i = 0; i < 3; i++) {
            BuyFragment fragment = new BuyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(IntentData.BUY_TYPE, i);
            fragment.setArguments(bundle);
            fragments[i] = fragment;
        }

        OtherLotteryFragment fragment = new OtherLotteryFragment();
        fragments[3] = fragment;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return 4;
    }
}
