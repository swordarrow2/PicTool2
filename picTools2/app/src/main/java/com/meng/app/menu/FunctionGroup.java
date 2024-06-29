package com.meng.app.menu;

public enum FunctionGroup {

    /*
     *@author 清梦
     *@date 2024-06-26 09:48:49
     */

    GROUP_DEVELOPING("正在开发"),
    GROUP_PICTURE("图片处理"),
    GROUP_VIDEO("视频"),
    GROUP_AUDIO("声音"),
    GROUP_ELECTRONIC("电子开发"),
    GROUP_SYSTEM("系统"),
    GROUP_DEFAULT("默认");

    public static final String TAG = "FunctionGroup";

    private String name;

    FunctionGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
