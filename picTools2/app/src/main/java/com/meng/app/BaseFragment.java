package com.meng.app;
import android.app.*;
import android.content.*;
import android.view.*;

public  class BaseFragment extends Fragment {

    /*
     *@author 清梦
     *@date 2024-04-19 09:45:07
     */
    public static final String TAG = "BaseFragment";   

    public  String getName(){
        return "";
    }
    
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.SELECT_FILE_REQUEST_CODE);
    }

    public void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, Constant.SELECT_FILE_REQUEST_CODE);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}
