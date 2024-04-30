package com.meng.eleTool.calculate;

import android.view.*;
import android.view.View.*;
import com.meng.mediatool.*;
import com.meng.view.*;

public class BuckInputCapacitanceChoose extends BaseDcdcCalculate {

    public void init() {
        setTitle("输入滤波电容选择");
        final SjfEditText svi = addEditText("输入电压(V)");
        final SjfEditText svo = addEditText("输出电压(V)");
        final SjfEditText svd = addEditText("二极管压降(V)");
        final SjfEditText sio = addEditText("输出电流(A)");
        final SjfEditText sdeltaV = addEditText("允许纹波(mV)");
        final SjfEditText sf = addEditText("开关频率(kHz)");
        final SjfEditText sl = addEditText("电感感值(uH)");

        setButtonClick(new OnClickListener(){

                @Override
                public void onClick(View p1) {
                    try {
                        double vi = svi.getDouble();
                        double vo = svo.getDouble();
                        double vd = svd.getDouble();
                        double deltaV = sdeltaV.getDouble() / 1000;
                        double io = sio.getDouble();
                        double f = sf.getDouble() * 1000;
                        double l = sl.getDouble() / 1000000;

                        double Ci = getCin(io, deltaV, vi, f, vo, vd);

                        double esr = getEsr(deltaV, io, vo, vd, f, l, vi);
                        setResult(String.format("使用陶瓷电容:容量不小于:%.2fuF\n使用电解电容:ESR不大于%.2fmΩ", Ci * 1000000, esr * 1000));
                    } catch (NumberFormatException e) {
                        MainActivity.instance.showToast("请输入正确的数字");
                    }
                }
            });
    }
    
    private double getEsr(double deltaV, double io, double vo, double vd, double f, double l, double vi) {
        return (deltaV /
            (
            io +
            ((vo + vd) / (2 * f * l)) *
            ((vi - vo) / (vi + vd))
            ));
    }

    private double getCin(double io, double deltaV, double vi, double f, double vo, double vd) {
        return ((io / (deltaV * vi * f)) *
            (vo + vd * ((vi - vo) / (vi + vd))) *
            ((vi - vo) / (vi + vd)));
    }
}
