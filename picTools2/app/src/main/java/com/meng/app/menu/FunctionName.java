package com.meng.app.menu;

import android.app.*;
import android.view.*;
import android.widget.*;

import com.meng.app.BaseFragment;
import com.meng.app.MFragmentManager;
import com.meng.app.MainActivity;
import com.meng.toolset.eleTool.UsbSerialFragment;
import com.meng.toolset.mediatool.*;
import com.meng.toolset.mediatool.picture.barcode.*;
import com.meng.toolset.mediatool.picture.*;
import com.meng.toolset.mediatool.picture.gif.*;
import com.meng.toolset.mediatool.picture.pixiv.*;
import com.meng.toolset.mediatool.picture.saucenao.*;
import com.meng.toolset.mediatool.wallpaper.*;
import com.meng.toolset.ai.*;
import com.meng.toolset.eleTool.calculate.*;

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
    //    FUNCTION_PICTURE_TENCENT_OCR("腾讯优图OCR",FunctionGroup.GROUP_PICTURE,OcrMain.class), //疼殉已弃用该API
    FUNCTION_VIDEO_FORMAT_CONVERT("视频格式转换", FunctionGroup.GROUP_VIDEO, FfmpegFragment.class),
    FUNCTION_VIDEO_RTMP_PUSH("RTMP推流", FunctionGroup.GROUP_VIDEO, RtmpFragment.class),
    FUNCTION_VIDEO_WALLPAPER("视频动态壁纸", FunctionGroup.GROUP_VIDEO, WallpaperMain.class),
    FUNCTION_AUDIO_ANDROID_TTS("安卓语音合成", FunctionGroup.GROUP_AUDIO, TtsFragment.class),
    FUNCTION_AUDIO_VITS_TTS("VITS语音合成", FunctionGroup.GROUP_AUDIO, VitsConnectFragment.class),
    FUNCTION_ELECTRONIC_SEARCH_SEMIEE("搜索半导小芯", FunctionGroup.GROUP_DEVELOPING, SearchSemieeFragment.class),
    FUNCTION_ELECTRONIC_BOOST_PART_CHOOSE("boost元件选型", FunctionGroup.GROUP_ELECTRONIC, DcdcBoostCalculateFragment.class),
    FUNCTION_ELECTRONIC_BUCK_PART_CHOOSE("buck元件选型", FunctionGroup.GROUP_ELECTRONIC, new Runnable() {

        @Override
        public void run() {
            ListView dcdcList = new ListView(MainActivity.instance);
            dcdcList.setAdapter(new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, MainActivity.instance.getResources().getStringArray(R.array.dcdc_cal_type)));
            final AlertDialog dcdcDialog = new AlertDialog.Builder(MainActivity.instance).setTitle("选择操作").setView(dcdcList).show();
            dcdcList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                    dcdcDialog.dismiss();
                    switch (p3) {
                        case 0:
                            MFragmentManager.getInstance().showFragment(BuckInductSelect.class);
                            break;
                        case 1:
                            MFragmentManager.getInstance().showFragment(BuckInductorCurrent.class);
                            break;
                        case 2:
                            MFragmentManager.getInstance().showFragment(BuckInputCapacitanceChoose.class);
                            break;
                        case 3:
                            MFragmentManager.getInstance().showFragment(BuckOutputCapacitanceChoose.class);
                            break;
                    }
                }
            });

        }
    }),
    FUNCTION_ELECTRONIC_FARAD_TEST("法拉电容估算", FunctionGroup.GROUP_ELECTRONIC, FaradCapacitanceCalculate.class),
    FUNCTION_ELECTRONIC_SERIAL_PORT("串行端口", FunctionGroup.GROUP_DEVELOPING, UsbSerialFragment.class),

    FUNCTION_SYSTEM_SETTINGS("设置", FunctionGroup.GROUP_SYSTEM, new Runnable() {

        @Override
        public void run() {
            MFragmentManager.getInstance().showSettingFragment();
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
