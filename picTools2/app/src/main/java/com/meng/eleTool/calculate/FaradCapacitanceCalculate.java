package com.meng.eleTool.calculate;

import android.view.*;
import android.view.View.*;
import com.meng.mediatool.*;
import com.meng.view.*;

public class FaradCapacitanceCalculate extends BaseDcdcCalculate {

    public void init() {
        setTitle("法拉电容容量估算");
        final SjfEditText si = addEditText("充电恒流(A)");
        final SjfEditText st = addEditText("充电时间(s)");
        final SjfEditText sv0 = addEditText("初始电压(V)");
        final SjfEditText sv1 = addEditText("结束电压(V)");

        setButtonClick(new OnClickListener(){

                @Override
                public void onClick(View p1) {
                    try { 
                        double result = calculate(sv0.getDouble(), sv1.getDouble(), si.getDouble(), st.getDouble());
                        setResult(String.format("容量约为:%.2fF", result));
                    } catch (NumberFormatException e) {
                        MainActivity.instance.showToast("请输入正确的数字");
                    }
                }
            });
    }

    private double calculate(double v0, double v1, double i, double t) {
        return i * t / (v1 - v0);
    }
}
