package com.meng.tools.app;

import android.os.Environment;

import com.meng.app.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
