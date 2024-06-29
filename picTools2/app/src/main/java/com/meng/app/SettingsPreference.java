package com.meng.app;

import android.os.*;
import android.preference.*;

import com.meng.toolset.mediatool.R;

public class SettingsPreference extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("main");
        addPreferencesFromResource(R.xml.preference);
    }
}
