package com.meng.toolset.audio;

import java.util.*;

public class VitsTtsBean {

    /*
     *@author 清梦
     *@date 2024-04-20 00:52:48
     */
    public static final String TAG = "VitsTtsBean";      

    //  public int index;

    public List<String> models;

    // public int role;

    @Override
    public String toString() {
        return "[" + getClass().getCanonicalName() + "]"
            //  + "\nindex=" + index
            + ",\nmodels=" + models;
        //    + ",\nrole=" + role;
    }

}
