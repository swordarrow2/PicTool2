package com.meng.mediatool.task;

import android.os.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import java.io.*;
import java.nio.charset.*;
import java.time.*;

import java.lang.Process;
import android.widget.*;

public class FFmpegBackTask extends BackgroundTask {

    /*
     *@author 清梦
     *@date 2024-05-02 08:36:34
     */
    public static final String TAG = "FFmpegBackTask";

    private Process process;            
    private boolean isStart = false;
    private int lasttimestamp = 1;

    private PrintStream ps;
    private PrintStream pse;

    private TextView tvLog;


    public FFmpegBackTask(Process process, String taskName) {
        this.process = process;
        File logF = new File(Environment.getExternalStorageDirectory() + "/log/log" + taskName + ".txt");
        File logFe = new File(Environment.getExternalStorageDirectory() + "/log/loge" + taskName + ".txt");

        try {
            ps = new PrintStream(new FileOutputStream(logF));
            pse = new PrintStream(new FileOutputStream(logFe));
        } catch (FileNotFoundException e) {
            MainActivity.instance.showToast("log文件创建失败", e.toString());
        }
    }      

    public FFmpegBackTask(Process process, String taskName, TextView tv) {
        this(process, taskName);
        tvLog = tv;       
    }

    @Override
    public void run() {
        try {            
            setMaxProgress(Integer.MAX_VALUE);
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                final String ffmpegOutput = line.trim();
                print(ffmpegOutput);
                if (ffmpegOutput.contains("time=")) {
                    isStart = true;
                    final int total = getSeconed(ffmpegOutput.substring(ffmpegOutput.indexOf("time=") + 5, ffmpegOutput.indexOf(" bitrate")));
                    setStatus("正在进行");
                    setProgress(total);
                    setProgressText(String.format("%.2f%%", total * 100f / lasttimestamp));                            
                } else if (ffmpegOutput.startsWith("Duration")) {
                    lasttimestamp = getSeconed(ffmpegOutput.substring(ffmpegOutput.indexOf(":") + 1, ffmpegOutput.indexOf(",")).trim()); 
                    setMaxProgress(lasttimestamp);
                } else if (!ffmpegOutput.contains("time=") && isStart) {
                    setStatus("完成");
                    setProgress(100);
                    setProgressText("100%");                                                 
                    isStart = false;
                }
            }  
        } catch (Exception e) {
            ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
        }  
    }

    private int getSeconed(String time) {
        //00:11:22.33
        return LocalTime.parse(time).getSecond();
    }

    private void print(final String s) throws IOException {        
        if (tvLog != null) {
            MainActivity.instance.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        tvLog.setText(tvLog.getText() + s + "\n"); 
                    }
                });
        }
        if (!SharedPreferenceHelper.isSaveDebugLog()) {
            return;
        }
        ps.write((s + "\n").getBytes(StandardCharsets.UTF_8));
        ps.flush();
    }

    private void printE(final String s) throws IOException {
        if (tvLog != null) {
            MainActivity.instance.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        tvLog.setText(tvLog.getText() + s);
                    }
                });
        }
        if (!SharedPreferenceHelper.isSaveDebugLog()) {
            return;
        }
        pse.write((s + "\n").getBytes(StandardCharsets.UTF_8));
        pse.flush();
    }
}
