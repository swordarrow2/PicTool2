package com.meng.pictool;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;

public class FragmentWrapperActivity extends Activity {

    private static int hostVersionCode = 0;

    public static int getHostVersionCode() {
        return hostVersionCode;
    }

    public static void setHostVersionCode(int versionCode) {
        hostVersionCode = versionCode;
    }

    private static final String ENTRY_BARCODE_GENERATOR = "BarcodeGenerator";
    private static final String ENTRY_BARCODE_READER = "BarcodeReader";

    private String[] generatorFunctions = {
            "普通二维码",
            "AwesomeQR",
            "随机彩虹AwesomeQR",
            "AwesomeQR(GIF)",
            "随机彩虹AwesomeQR(GIF)"
    };

    private String[] generatorFragmentClasses = {
            ".picture.barcode.BarcodeNormal",
            ".picture.barcode.BarcodeAwesome",
            ".picture.barcode.BarcodeAwesomeArb",
            ".picture.barcode.BarcodeAwesomeGif",
            ".picture.barcode.BarcodeAwesomeArbGif"
    };

    private String[] readerFunctions = {
            "扫码",
            "相册扫码"
    };

    private String[] readerFragmentClasses = {
            ".picture.barcode.BarcodeReaderCamera",
            ".picture.barcode.BarcodeReaderGallery"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setHostVersionCode(getIntent().getIntExtra("host_version_code", 0));
        checkLaunchMode();

        if (savedInstanceState == null) {
            handleEntryPoint();
        }
    }

    private void handleEntryPoint() {
        String aliasName = getComponentName().getShortClassName();
        
        if (aliasName == null || aliasName.isEmpty()) {
            loadFragmentFromMetaData();
            return;
        }

        String entryType = determineEntryType(aliasName);
        switch (entryType) {
            case ENTRY_BARCODE_GENERATOR:
                showBarcodeGeneratorDialog();
                break;
            case ENTRY_BARCODE_READER:
                showBarcodeReaderDialog();
                break;
            default:
                loadFragmentFromMetaData();
                break;
        }
    }

    private String determineEntryType(String aliasName) {
        if (aliasName.contains(ENTRY_BARCODE_GENERATOR)) {
            return ENTRY_BARCODE_GENERATOR;
        }
        if (aliasName.contains(ENTRY_BARCODE_READER)) {
            return ENTRY_BARCODE_READER;
        }
        return null;
    }

    private void loadFragmentFromMetaData() {
        String fragmentClassName = getFragmentClassNameFromMetaData();
        if (fragmentClassName != null) {
            loadFragment(fragmentClassName);
        } else {
            finish();
        }
    }

    private String getFragmentClassNameFromMetaData() {
        ActivityInfo activityInfo = getActivityInfo();
        if (activityInfo != null && activityInfo.metaData != null) {
            return activityInfo.metaData.getString("fragment_class");
        }
        return null;
    }

    private ActivityInfo getActivityInfo() {
        try {
            return getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showBarcodeGeneratorDialog() {
        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generatorFunctions));

        final AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("选择条码生成功能")
                .setView(lv)
                .show();

        lv.setOnItemClickListener((parent, view, position, id) -> {
            ad.dismiss();
            loadFragment(generatorFragmentClasses[position]);
        });
    }

    private void showBarcodeReaderDialog() {
        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readerFunctions));

        final AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("选择条码读取功能")
                .setView(lv)
                .show();

        lv.setOnItemClickListener((parent, view, position, id) -> {
            ad.dismiss();
            loadFragment(readerFragmentClasses[position]);
        });
    }

    private void loadFragment(String fragmentClassName) {
        if (fragmentClassName == null || fragmentClassName.isEmpty()) {
            finish();
            return;
        }

        String className = getPackageName() + fragmentClassName;
        try {
            Class<?> fragmentClass = Class.forName(className);
            BaseFragment fragment = (BaseFragment) fragmentClass.getDeclaredConstructor().newInstance();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void checkLaunchMode() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        if (action != null && action.startsWith("com.meng.intent.action.PLUGIN")) {
            showHideIconDialog();
        }
    }

    private void showHideIconDialog() {
        SharedPreferences prefs = getSharedPreferences("pictool2_prefs", MODE_PRIVATE);
        boolean alreadyAsked = prefs.getBoolean("pref_ask_hide_icon", false);

        if (alreadyAsked) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("隐藏桌面图标")
                .setMessage("检测到您是从宿主软件启动的此应用。是否隐藏桌面图标，仅通过主软件访问？")
                .setPositiveButton("隐藏", (dialog, which) -> {
                    hideLauncherIcon();
                    prefs.edit().putBoolean("pref_ask_hide_icon", true).apply();
                })
                .setNegativeButton("不隐藏", (dialog, which) -> {
                    prefs.edit().putBoolean("pref_ask_hide_icon", true).apply();
                })
                .setNeutralButton("以后再问", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void hideLauncherIcon() {
        PackageManager pm = getPackageManager();
        ComponentName aliasName = new ComponentName(this, "com.meng.pictool.MainActivityAlias");
        pm.setComponentEnabledSetting(aliasName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}