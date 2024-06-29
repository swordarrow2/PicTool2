package com.meng.toolset.eleTool.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class BoostInductorCurrent extends BaseDcdcCalculate implements OnClickListener {
    private SjfEditText svi;
    private SjfEditText svo;
    private SjfEditText svd;
    private SjfEditText sio;
    private SjfEditText sf;
    private SjfEditText sl;

    public void init() {
        setTitle("电感电流计算");
        svi = addEditText("输入电压(V)");
        svo = addEditText("输出电压(V)");
        svd = addEditText("二极管压降(V)");
        sio = addEditText("输出电流(A)");
        sf = addEditText("开关频率(kHz)");
        sl = addEditText("电感感值(uH)");
        setButtonClick(this);
    }

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
            setResult(String.format(Locale.CHINA, "电感平均电流:%.2fA\n电感电流纹波:%.2fA\n电感峰值电流:%.2fA", average, delta, max));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double getMax(double average, double delta) {
        return average + delta / 2;
    }

    private double getDelta(double vi, double f, double l, double vo, double vd) {
        return (vi / (f * l)) * (1 - (vi / (vo + vd)));
    }

    private double getAverage(double vo, double vd, double vi, double io) {
        return (((vo + vd) / vi) * io);
    }
};
