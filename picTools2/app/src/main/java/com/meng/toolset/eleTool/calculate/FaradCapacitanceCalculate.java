package com.meng.toolset.eleTool.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class FaradCapacitanceCalculate extends BaseDcdcCalculate implements OnClickListener {
    private SjfEditText si;
    private SjfEditText st;
    private SjfEditText sv0;
    private SjfEditText sv1;

    public void init() {
        setTitle("法拉电容容量估算");
        si = addEditText("充电恒流(A)");
        st = addEditText("充电时间(s)");
        sv0 = addEditText("初始电压(V)");
        sv1 = addEditText("结束电压(V)");

        setButtonClick(this);
    }

    @Override
    public void onClick(View p1) {
        try {
            double result = calculate(sv0.getDouble(), sv1.getDouble(), si.getDouble(), st.getDouble());
            setResult(String.format(Locale.CHINA, "容量约为:%.2fF", result));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double calculate(double v0, double v1, double i, double t) {
        return i * t / (v1 - v0);
    }
}
