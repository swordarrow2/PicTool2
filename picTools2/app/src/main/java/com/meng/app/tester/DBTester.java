package com.meng.app.tester;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meng.app.BaseFragment;
import com.meng.app.database.BoxDataBase;
import com.meng.app.database.DataBaseHelper;
import com.meng.app.database.PixivDataBase;
import com.meng.toolset.mediatool.R;

import java.util.ArrayList;

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
        final PixivDataBase pdb = DataBaseHelper.getInstance(PixivDataBase.class);

        pdb.init(getActivity());

        BoxDataBase dataBase = DataBaseHelper.getInstance(BoxDataBase.class);
        dataBase.init(getActivity());
        ContentValues cv = new ContentValues();
        cv.put("_time", 1111111);
        cv.put("_action", "some");
        dataBase.insertData("_op_log", cv);


        ContentValues cv2 = new ContentValues();
        cv2.put("_thing_id", 1111111);
        cv2.put("_name", "some");
        cv2.put("_describe", 1111111);
        cv2.put("_slot_id", "some");
        cv2.put("_picture_file", new byte[]{'9', '9', '6', '1'});
        dataBase.insertData("_data_main", cv2);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdb.addFailed(String.valueOf(System.currentTimeMillis() / 1000));
                ArrayList<String> res = pdb.getAllFailed();
                textView.setText(res.toString());
            }
        });
    }
}
