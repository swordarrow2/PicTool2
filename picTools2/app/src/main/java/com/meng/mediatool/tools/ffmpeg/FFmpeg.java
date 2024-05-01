package com.meng.mediatool.tools.ffmpeg;

import android.content.Context;
import com.meng.mediatool.MainActivity;
import com.meng.mediatool.tools.FileTool;
import java.io.File;

public class FFmpeg {

    private static FFmpeg instance = null;

    public Boolean init(Context context) {
        File ffmpegFile = new File(context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg");
        if (ffmpegFile.exists()) {
            return true;
        }
        if (!ffmpegFile.exists()) {
            boolean isFileCopied = FileTool.copyAssetsToData(context, "ffmpeg");
            if (isFileCopied) {
                if (!ffmpegFile.canExecute()) {
                    MainActivity.instance.showToast("FFmpeg is not executable, trying to make it executable ...");
                    if (ffmpegFile.setExecutable(true)) {
                        return true;
                    }
                } else {
                    MainActivity.instance.showToast("FFmpeg executable ...");
                    return true;
                }
            }
        }
        return null;
    }

    public static FFmpeg getInstance(Context context) {
        if (instance == null) {
            instance = new FFmpeg();
        }
        return instance;
    }
}
