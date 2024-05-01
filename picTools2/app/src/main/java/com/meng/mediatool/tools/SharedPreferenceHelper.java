package com.meng.mediatool.tools;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceHelper {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public static void init(Context context, String preferenceName) {
        sp = context.getSharedPreferences(preferenceName, 0);
    }

    public static String getTheme() {
        return sp.getString("theme", "芳");
    }

    public static void setTheme(String v) {
        putString("theme", v);
    }

    public static String getVersion() {
        return sp.getString("newVersion", "0.0.0");
    }

    public static void setVersion(String v) {
        putString("newVersion", v);
    }

    public static void setSaveDebugLog(boolean b) {
        putBoolean("debugLog", b);
    }

    public static boolean isSaveDebugLog() {
        return sp.getBoolean("debugLog", false);
    }

    public static void setOpenDrawer(boolean b) {
        putBoolean("opendraw", b);
    }

    public static boolean isOpenDrawer() {
        return sp.getBoolean("opendraw", true);
    }

    public static boolean isExit0() {
        return sp.getBoolean("exitsettings", false);
    }

    public static void setExit0(boolean b) {
        putBoolean("exitsettings", b);
    }

    public static boolean isShowSJF() {
        return sp.getBoolean("showSJF", true);
    }

    public static void setShowSJF(boolean use) {
        putBoolean("showSJF", use);
    }
    
   
    
    
    public static void setPixivCookie(String cookie){
        putString("cookievalue",cookie);
    }
    
    public static String getPixivCookie(){
        return sp.getString("cookievalue","");
    }
    
    public static void setThreads(String t){
        putString("threads",t);
    }

    public static String getThreads(){
        return sp.getString("threads","3");
    }
    
    public static void setDownloadBigGif(boolean b){
        putBoolean("bigpicturegif",b);
    }

    public static boolean isDownloadBigGif(){
        return sp.getBoolean("bigpicturegif",false);
    }
    
    public static void setDownloadBigPic(boolean b){
        putBoolean("bigpicture",b);
    }

    public static boolean isDownloadBigPic(){
        return sp.getBoolean("bigpicture",false);
    }
    
    public static void setDeleteZipAfterGenenalGif(boolean b){
        putBoolean("deleteZipAfterMakeGif",b);
    }

    public static boolean isDeleteZipAfterGenenalGif(){
        return sp.getBoolean("deleteZipAfterMakeGif",false);
    }
   
    
    
    
    
    public static String getRtmpAddr() {
        return sp.getString("rtmpAddr", "0.0.0");
    }

    public static void setRtmpAddr(String v) {
        putString("rtmpAddr", v);
    }
    
    public static String getRtmpCode() {
        return sp.getString("rtmpCode", "0.0.0");
    }

    public static void setRtmpCode(String v) {
        putString("rtmpCode", v);
    }
    
    /*
     public static void set(boolean b){
     putBoolean("",b);
     }

     public static boolean isBigPicture(){
     return sp.getBoolean("",false);
     }
    */
    
    
    
    
    private static void putBoolean(String key, Boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    private static void putLong(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    
    private static void putString(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

