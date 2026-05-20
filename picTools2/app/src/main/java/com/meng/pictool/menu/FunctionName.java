package com.meng.pictool.menu;

import android.app.*;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import com.meng.pictool.*;
import com.meng.pictool.picture.*;
import com.meng.pictool.picture.barcode.*;
import com.meng.pictool.picture.gif.*;
import com.meng.pictool.picture.pixiv.*;
import com.meng.pictool.picture.saucenao.*;

public enum FunctionName {

    /*
     *@author 清梦
     *@date 2024-06-26 09:48:59
     */

    FUNCTION_PICTURE_BARCODE("条码", FunctionGroup.GROUP_PICTURE, new Runnable() {

        @Override
        public void run() {
            ListView lv = new ListView(MainActivity.instance);
            final AlertDialog ad = new AlertDialog.Builder(MainActivity.instance).setTitle("选择操作").setView(lv).show();
            lv.setAdapter(new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, MainActivity.instance.getResources().getStringArray(R.array.create_type)));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                    ad.dismiss();
                    MainActivity.instance.openLeftDrawer();
                    switch (p3) {
                        case 0:
                            MFragmentManager.getInstance().showFragment(BarcodeNormal.class);
                            break;
                        case 1:
                            MFragmentManager.getInstance().showFragment(BarcodeAwesome.class);
                            break;
                        case 2:
                            MFragmentManager.getInstance().showFragment(BarcodeAwesomeArb.class);
                            break;
                        case 3:
                            MFragmentManager.getInstance().showFragment(BarcodeAwesomeGif.class);
                            break;
                        case 4:
                            MFragmentManager.getInstance().showFragment(BarcodeAwesomeArbGif.class);
                            break;
                        case 5:
                            MFragmentManager.getInstance().showFragment(BarcodeReaderCamera.class);
                            break;
                        case 6:
                            MFragmentManager.getInstance().showFragment(BarcodeReaderGallery.class);
                            break;
                    }
                }
            });

        }
    }),
    FUNCTION_PICTURE_CRYPT("加密", FunctionGroup.GROUP_PICTURE, PictureCrypt.class),
    FUNCTION_PICTURE_GRAY("灰度图", FunctionGroup.GROUP_PICTURE, GrayImage.class),
    FUNCTION_PICTURE_ENCODE_GIF("合成GIF", FunctionGroup.GROUP_PICTURE, GIFCreator.class),
    FUNCTION_PICTURE_PIXIV_DOWNLOAD("PIXIV下载", FunctionGroup.GROUP_PICTURE, PixivDownloadMain.class),
    FUNCTION_PICTURE_SAUCENAO("SauceNAO搜图", FunctionGroup.GROUP_PICTURE, SauceNaoMain.class),

    FUNCTION_SYSTEM_SETTINGS("设置", FunctionGroup.GROUP_SYSTEM, new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent(MainActivity.instance, SettingsActivity.class);
            MainActivity.instance.startActivity(intent);
        }
    }),
    FUNCTION_SYSTEM_BACKGROUND_TASK("后台任务", FunctionGroup.GROUP_SYSTEM, new Runnable() {

        @Override
        public void run() {
            MainActivity.instance.openRightDrawer();
        }
    }),
    FUNCTION_SYSTEM_EXIT("退出", FunctionGroup.GROUP_SYSTEM, new Runnable() {

        @Override
        public void run() {
            MainActivity.instance.exit();
        }
    });

    public static final String TAG = "FunctionName";

    private String name = null;
    private FunctionGroup group = FunctionGroup.GROUP_DEFAULT;
    private Runnable runnable = null;
    private Class<? extends BaseFragment> clazz = null;

    FunctionName(String name, FunctionGroup group, Runnable runnable) {
        this.name = name;
        this.group = group;
        this.runnable = runnable;
    }

    FunctionName(String name, FunctionGroup group, Class<? extends BaseFragment> clazz) {
        this.name = name;
        this.group = group;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public FunctionGroup getGroup() {
        return group;
    }

    public void doAction() {
        if (clazz != null) {
            MFragmentManager.getInstance().showFragment(clazz);
        } else if (runnable != null) {
            runnable.run();
        } else {
            throw new IllegalStateException("class and runnable both are null");
        }
    }

}
