package com.meng.pt2;

import android.os.*;
import android.preference.*;

public class SettingsPreference extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("main");
        addPreferencesFromResource(R.xml.preference);
    }
}
