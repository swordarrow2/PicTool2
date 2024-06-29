package com.meng.app.menu;

import java.util.*;

import android.view.*;

import com.meng.toolset.mediatool.*;

import com.meng.tools.app.SharedPreferenceHelper;

public class MenuManager {

    /*
     *@author 清梦
     *@date 2024-06-26 13:33:47
     */
    public static final String TAG = "MenuManager";

    private static MenuManager instance;

    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }

    private LinkedHashMap<FunctionGroup, LinkedList<FunctionName>> menuEntry = new LinkedHashMap<>();

    public void init(Menu menu) {
        menu.clear();
        for (FunctionGroup group : FunctionGroup.values()) {
            menuEntry.put(group, new LinkedList<FunctionName>());
        }
        for (FunctionName name : FunctionName.values()) {
            menuEntry.get(name.getGroup()).add(name);
        }
        if (SharedPreferenceHelper.isShowGroupName()) {
            for (Map.Entry<FunctionGroup, LinkedList<FunctionName>> entry : menuEntry.entrySet()) {
                if (entry.getKey() == FunctionGroup.GROUP_DEVELOPING && !SharedPreferenceHelper.isDebugMode()) {
                    continue;
                }
                SubMenu subMenu = menu.addSubMenu(0, entry.getKey().ordinal(), 0, entry.getKey().getName());
                for (FunctionName fn : entry.getValue()) {
                    MenuItem item = subMenu.add(0, fn.ordinal(), 0, fn.getName());
                    item.setIcon(R.drawable.ic_menu);
                }
            }
        } else {
            for (Map.Entry<FunctionGroup, LinkedList<FunctionName>> entry : menuEntry.entrySet()) {
                if (entry.getKey() == FunctionGroup.GROUP_DEVELOPING && !SharedPreferenceHelper.isDebugMode()) {
                    continue;
                }
                for (FunctionName fn : entry.getValue()) {
                    MenuItem item = menu.add(fn.getGroup().ordinal(), fn.ordinal(), 0, fn.getName());
                    item.setIcon(R.drawable.ic_menu);
                }
            }
        }
    }
}
