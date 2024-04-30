package com.meng.mediatool.task;

import com.meng.mediatool.*;
import com.meng.mediatool.task.*;

public class TestTask extends BackgroundTask {

    /*
     *@author 清梦
     *@date 2024-04-19 09:39:36
     */
    public static final String TAG = "TestTask";

    @Override
    public void run() {
        setMaxProgress(40);
        for (int i=0;i <= 40;i++) {
            final int ii = i;
            MainActivity.instance.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        setProgressText(ii + "G/40G");
                        setProgress(ii);            
                    }
                });
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
