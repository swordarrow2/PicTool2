package com.meng.app.tester;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meng.app.BaseFragment;
import com.meng.tools.app.DataBaseHelper;
import com.meng.tools.app.DataBaseHelperOld;
import com.meng.toolset.mediatool.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by SJF on 2024/6/30.
 */

public class DBTester extends BaseFragment {
    private TextView textView;

    public DBTester() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.aboutTextView);
        //  DataBaseHelperOld.init(getActivity());
        //  DataBaseHelperOld.insertData(String.valueOf(1234567));
        //textView.setText(DataBaseHelperOld.searchFailedPic().toString());
        final DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(DataBaseHelper.DatabaseName.NAME_PIXIV);
//        dataBaseHelper.init(getActivity(), DataBaseHelper.DatabaseName.NAME_PIXIV, "record", "_id", new LinkedHashMap<String, String>() {
//            {
//                put("_pid", "text");
//            }
//        });
        dataBaseHelper.init(getActivity(), DataBaseHelper.DatabaseName.NAME_PIXIV, "op_log", "_id", new LinkedHashMap<String, String>() {
            {
                put("_op", "varchar(20)");
                put("_time", "datetime");
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("_op", String.valueOf(System.currentTimeMillis() / 1000));
                values.put("_time", Calendar.getInstance(Locale.CHINA).getTime().toString());
                dataBaseHelper.insertData(values);
                ArrayList<ContentValues> res = dataBaseHelper.getAllData();
                textView.setText(res.toString());
            }
        });
    }
}
