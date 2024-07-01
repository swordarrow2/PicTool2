package com.meng.toolset.electronic.calculate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.meng.app.BaseFragment;
import com.meng.toolset.mediatool.R;

public class DcdcBuckCalculateFragment extends BaseFragment {


    /*
    *@author 清梦
	*@date 2024-06-26 02:31:23
    */
    public static final String TAG = "DcdcCalculateFragment";

    private TabHost tab;

    private LinearLayout ll;
    private TextView title;
    private TextView result;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dcdc_buck_calculate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        tab = (TabHost) view.findViewById(android.R.id.tabhost);
        tab.setup();

        tab.addTab(tab.newTabSpec("tab1").setIndicator("电感感值", null).setContent(R.id.boost_inductor_select));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("电感电流", null).setContent(R.id.boost_inductor_current));
        tab.addTab(tab.newTabSpec("tab3").setIndicator("输入滤波电容", null).setContent(R.id.boost_input_capcitor));
        tab.addTab(tab.newTabSpec("tab4").setIndicator("输出滤波电容", null).setContent(R.id.boost_output_capcitor));
    }

}
