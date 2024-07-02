package com.meng.tools.app;

import android.content.Context;
import android.net.ConnectivityManager;

import com.meng.app.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SJF on 2024/6/29.
 */

public class SystemStatus {
    public static boolean isUsingWifi() {
        return ((ConnectivityManager) MainActivity.instance.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getTime(long timeStamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStamp));
    }

    public static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getDate(long timeStamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
    }

    public static String formatDate(int y, int m, int d) {
        return getDate(new Date(y, m, d).getTime());
    }
}
