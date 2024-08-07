package com.meng.tools.app;

import android.content.*;
import android.net.*;
import android.os.*;

import com.meng.app.*;

import java.text.*;
import java.util.*;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by SJF on 2024/6/29.
 */

public class SystemTools {
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

    public static void doVibrate(Context context, long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(time);
        }
    }
}
