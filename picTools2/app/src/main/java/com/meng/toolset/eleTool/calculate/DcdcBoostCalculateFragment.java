package com.meng.toolset.eleTool.calculate;

import android.os.*;
import android.view.*;
import android.widget.*;

import com.meng.app.BaseFragment;
import com.meng.tools.MaterialDesign.MDEditText;
import com.meng.toolset.mediatool.*;

public class DcdcBoostCalculateFragment extends BaseFragment {


    /*
    *@author 清梦
	*@date 2024-06-26 02:31:23
    */
    public static final String TAG = "DcdcCalculateFragment";

    private TabHost tabHost;

    private LinearLayout ll;
    private TextView title;
    private TextView result;
    private Button btn;
    private MDEditText tab1vi, tab1vo, tab1vd, tab1oi, tab1fr,
            tab2vi, tab2vo, tab2vd, tab2oi, tab2fr, tab2l,
            tab3vi, tab3vo, tab3vd, tab3dv, tab3fr, tab3l,
            tab4vi, tab4vo, tab4vd, tab4oi, tab4fr, tab4l, tab4dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dcdc_boost_calculate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        result = (TextView) view.findViewById(R.id.boost_calculateTextViewResult);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("电感感值", null).setContent(R.id.boost_inductor_select));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("电感电流", null).setContent(R.id.boost_inductor_current));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("输入滤波电容", null).setContent(R.id.boost_input_capcitor));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("输出滤波电容", null).setContent(R.id.boost_output_capcitor));

    }

}
