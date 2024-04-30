package com.meng.eleTool.calculate;

import android.view.*;
import android.view.View.*;
import com.meng.mediatool.*;
import com.meng.view.*;

public class BoostInductorCurrent extends BaseDcdcCalculate {

    public void init() {
        setTitle("电感电流计算");
        final SjfEditText svi = addEditText("输入电压(V)");
        final SjfEditText svo = addEditText("输出电压(V)");
        final SjfEditText svd = addEditText("二极管压降(V)");
        final SjfEditText sio = addEditText("输出电流(A)");
        final SjfEditText sf = addEditText("开关频率(kHz)");
        final SjfEditText sl = addEditText("电感感值(uH)");

        setButtonClick(new OnClickListener(){

                @Override
                public void onClick(View p1) {
                    try {
                        double vi = svi.getDouble();
                        double vo = svo.getDouble();
                        double vd = svd.getDouble();
                        double io = sio.getDouble();
                        double f = sf.getDouble() * 1000;
                        double l = sl.getDouble() / 1000000;

                        double average = getAverage(vo, vd, vi, io);
                        double delta = getDelta(vi, f, l, vo, vd);
                        double max = getMax(average, delta);
                        setResult(String.format("电感平均电流:%.2fA\n电感电流纹波:%.2fA\n电感峰值电流:%.2fA", average, delta, max));
                    } catch (NumberFormatException e) {
                        MainActivity.instance.showToast("请输入正确的数字");
                    }
                }

            });
    }

    private double getMax(double average, double delta) {
        double max = average + delta / 2;
        return max;
    }

    private double getDelta(double vi, double f, double l, double vo, double vd) {
        double delta = (vi / (f * l)) *
            (1 - (vi / (vo + vd)));
        return delta;
    }

    private double getAverage(double vo, double vd, double vi, double io) {
        return (((vo + vd) / vi) * io);
    }
};
