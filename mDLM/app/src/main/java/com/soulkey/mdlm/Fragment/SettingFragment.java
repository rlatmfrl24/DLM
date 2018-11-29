package com.soulkey.mdlm.Fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;

import com.soulkey.mdlm.R;

public class SettingFragment extends PreferenceFragmentCompat {

    public SettingFragment(){
    }

    public static SettingFragment newInstance(){
        return new SettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference);
    }
}
