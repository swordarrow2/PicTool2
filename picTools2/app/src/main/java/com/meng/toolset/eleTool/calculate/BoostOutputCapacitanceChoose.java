package com.meng.toolset.eleTool.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class BoostOutputCapacitanceChoose extends BaseDcdcCalculate implements OnClickListener {
    private SjfEditText svi;
    private SjfEditText svo;
    private SjfEditText sio;
    private SjfEditText svd;
    private SjfEditText sdeltaV;
    private SjfEditText sf;
    private SjfEditText sl;

    public void init() {
        setTitle("输出滤波电容选择");
        svi = addEditText("输入电压(V)");
        svo = addEditText("输出电压(V)");
        sio = addEditText("输出电流(A)");
        svd = addEditText("二极管压降(V)");
        sdeltaV = addEditText("允许纹波(mV)");
        sf = addEditText("开关频率(kHz)");
        sl = addEditText("电感感值(uH)");

        setButtonClick(this);
    }

    @Override
    public void onClick(View p1) {
        try {
            double vi = svi.getDouble();
            double vo = svo.getDouble();
            double vd = svd.getDouble();
            double io = sio.getDouble();
            double deltaV = sdeltaV.getDouble() / 1000;
            double f = sf.getDouble() * 1000;
            double l = sl.getDouble() / 1000000;

            double Ci = getCapacity(io, f, deltaV, vi, vo, vd);
            double esr = getEsr(deltaV, vo, vd, vi, io, f, l);
            setResult(String.format(Locale.CHINA, "使用陶瓷电容:容量不小于:%.2fuF\n使用电解电容:ESR不大于%.2fmΩ", Ci * 1000 * 1000, esr * 1000));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double getEsr(double deltaV, double vo, double vd, double vi, double io, double f, double l) {
        return (deltaV /
                (((vo + vd) / vi) * io +
                        (vi / (2 * f * l)) *
                                (1 - (vi / (vo + vd)))
                ));
    }

    private double getCapacity(double io, double f, double deltaV, double vi, double vo, double vd) {
        return ((io / (f * deltaV)) * (1 - (vi / (vo + vd))));
    }
}
