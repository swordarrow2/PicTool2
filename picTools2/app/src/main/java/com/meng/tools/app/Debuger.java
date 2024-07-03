package com.meng.tools.app;

import android.os.*;

import com.meng.app.*;

import java.io.*;

/**
 * Created by SJF on 2024/6/29.
 */

public class Debuger {
    public static void initLogFile() {
        File logF = new File(Environment.getExternalStorageDirectory() + "/log.txt");
        File logFe = new File(Environment.getExternalStorageDirectory() + "/loge.txt");
        if (SharedPreferenceHelper.isSaveDebugLog()) {
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(logF));
                System.setOut(ps);
                PrintStream pse = new PrintStream(new FileOutputStream(logFe));
                System.setErr(pse);
            } catch (FileNotFoundException e) {
                MainActivity.instance.showToast("log文件创建失败");
            }
        } else {
            logF.delete();
            logFe.delete();
        }
    }
}
