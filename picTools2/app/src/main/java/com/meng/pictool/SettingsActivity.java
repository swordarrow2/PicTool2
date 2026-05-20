package com.meng.pictool;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.*;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsPreference())
                .commit();
    }

    public static class SettingsPreference extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName("main");
            addPreferencesFromResource(R.xml.preference);
        }
    }
}