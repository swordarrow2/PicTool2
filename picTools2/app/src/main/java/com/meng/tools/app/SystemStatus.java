package com.meng.tools.app;

import android.content.Context;
import android.net.ConnectivityManager;

import com.meng.app.MainActivity;

/**
 * Created by SJF on 2024/6/29.
 */

public class SystemStatus {
    public static boolean isUsingWifi() {
        return ((ConnectivityManager) MainActivity.instance.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }
}
