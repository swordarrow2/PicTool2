package com.meng.app;

import android.app.*;

import com.meng.toolset.mediatool.*;
import com.meng.toolset.picture.barcode.*;

import java.util.*;

public class MFragmentManager {

    /*
     *@author 清梦
     *@date 2024-04-19 09:55:53
     */
    public static final String TAG = "FragmentManager";
    private HashMap<String, BaseFragment> fragments = new HashMap<>();
    private Activity activity;
    private static MFragmentManager instance;
    private BaseFragment current;
    private SettingsPreference setting = new SettingsPreference();

    public static MFragmentManager getInstance() {
        if (instance == null) {
            instance = new MFragmentManager();
        }
        return instance;
    }

    public void init(Activity a) {
        activity = a;
        FragmentTransaction trans = a.getFragmentManager().beginTransaction();
        trans.add(R.id.fragment, setting);
        BarcodeAwesome frag = new BarcodeAwesome();
        fragments.put(BarcodeAwesome.class.getName(), frag);
        trans.add(R.id.fragment, frag);
        current = frag;
        trans.commit();
    }

    public <T extends BaseFragment> T getFragment(Class<T> c) {
        return (T) fragments.get(c.getName());
    }

    public SettingsPreference getSettingPreference() {
        return setting;
    }

    public <T extends BaseFragment> void showFragment(Class<T> c) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        BaseFragment frag = fragments.get(c.getName());
        if (frag == null) {
            try {
                Class<?> cls = Class.forName(c.getName());
                frag = (BaseFragment) cls.newInstance();
                fragments.put(c.getName(), frag);
                transaction.add(R.id.fragment, frag);
            } catch (Exception e) {
                throw new RuntimeException("反射爆炸");
            }
        }
        current = frag;
        hideFragment();
        transaction.show(frag);
        transaction.commit();
    }

    public void showSettingFragment() {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        hideFragment();
        transaction.show(setting);
        transaction.commit();
    }

    public void hideFragment() {
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        for (BaseFragment f : fragments.values()) {
            ft.hide(f);
        }
        ft.hide(setting);
        ft.commit();
    }

    public BaseFragment getCurrent() {
        return current;
    }
}
