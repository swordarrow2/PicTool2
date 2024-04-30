package com.meng.mediatool.task;
import com.meng.mediatool.*;

public abstract class BackgroundTask implements Runnable {

    /*
     *@author 清梦
     *@date 2024-04-19 01:47:19
     */
    public static final String TAG = "BackgroundTask";
    private String title;
    private String status;
    private String progressText;
    private int progress;
    private int maxProgress = 100;

    public BackgroundTask setMaxProgress(int max) {
        maxProgress = max;
        return this;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public BackgroundTask setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BackgroundTask setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BackgroundTask setProgressText(String progressText) {
        this.progressText = progressText;
        return this;
    }

    public String getProgressText() {
        return progressText;
    }

    public BackgroundTask setProgress(final int progress) {
        this.progress = progress;
        MainActivity.instance.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    if (progress >= maxProgress) {
                        BackgroundTaskAdapter.getInstance().remove(BackgroundTask.this);
                    }                  
                    BackgroundTaskAdapter.getInstance().notifyDataSetChanged();
                }
            });
        return this;
    }

    public int getProgress() {
        return progress;
    } 

    @Override
    public abstract void run();

    public void start() {
        MainActivity.instance.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    BackgroundTaskAdapter.getInstance().addTask(BackgroundTask.this);
                }
            });
    }
}
