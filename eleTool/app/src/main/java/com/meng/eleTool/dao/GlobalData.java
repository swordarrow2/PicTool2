package com.meng.eleTool.dao;

import android.content.Context;

public abstract class GlobalData {

    private static RecordDAO recordDao;
    private static ModelDAO modelDao;

    public static ModelDAO getModelDao() {
        return modelDao;
    }

    public static RecordDAO getRecordDao() {
        return recordDao;
    }

    public static void init(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        recordDao = new RecordDAO(helper);
        modelDao = new ModelDAO(helper);
    }
}
