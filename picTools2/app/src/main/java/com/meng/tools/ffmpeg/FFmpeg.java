package com.meng.tools.ffmpeg;

import android.content.*;

import com.meng.app.*;
import com.meng.tools.*;

import java.io.*;

public class FFmpeg {

    private static FFmpeg instance = null;

    public void init(Context context) throws IOException {
        File ffmpegFile = new File(context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg");
        if (ffmpegFile.exists()) {
            return;
        }
        FileTool.copyAssetsToData(context, "ffmpeg");
        //   if (isFileCopied) {
        if (ffmpegFile.canExecute()) {
            return;
        }
        MainActivity.instance.showToast("FFmpeg is not executable, trying to make it executable ...");
        if (ffmpegFile.setExecutable(true)) {
            return;
        }
        //   } else {
        //      MainActivity.instance.showToast("FFmpeg executable ...");
        //       return true;
        //    }      
        throw new IllegalStateException("ffmpeg init failed!");
    }

    public static FFmpeg getInstance(Context context) {
        if (instance == null) {
            instance = new FFmpeg();
        }
        return instance;
    }
}
