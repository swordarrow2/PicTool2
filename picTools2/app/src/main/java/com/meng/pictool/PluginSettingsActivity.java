package com.meng.pictool;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

public class PluginSettingsActivity extends AppCompatActivity {

    private Switch switchShowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("插件设置");
        }

        initViews();
    }

    private void initViews() {
        switchShowIcon = (Switch) findViewById(R.id.switch_show_icon);
        switchShowIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showLauncherIcon();
            } else {
                hideLauncherIcon();
            }
        });

        updateSwitchState();
    }

    private void updateSwitchState() {
        PackageManager pm = getPackageManager();
        ComponentName aliasName = new ComponentName(this, "com.meng.pictool.MainActivityAlias");
        int state = pm.getComponentEnabledSetting(aliasName);

        switchShowIcon.setChecked(state != PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    private void showLauncherIcon() {
        PackageManager pm = getPackageManager();
        ComponentName aliasName = new ComponentName(this, "com.meng.pictool.MainActivityAlias");
        pm.setComponentEnabledSetting(aliasName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void hideLauncherIcon() {
        PackageManager pm = getPackageManager();
        ComponentName aliasName = new ComponentName(this, "com.meng.pictool.MainActivityAlias");
        pm.setComponentEnabledSetting(aliasName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
