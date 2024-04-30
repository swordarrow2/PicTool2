package com.meng.eleTool.model;

import com.meng.eleTool.tools.GSON;

public class BasePojo{

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
