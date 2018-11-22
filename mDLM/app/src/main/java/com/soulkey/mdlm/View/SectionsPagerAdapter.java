package com.soulkey.mdlm.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("muta", "fragment created.."+String.valueOf(position+1));
        return PlaceholderFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 3;
    }
}