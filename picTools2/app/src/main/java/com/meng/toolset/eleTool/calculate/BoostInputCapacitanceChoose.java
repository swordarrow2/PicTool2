package com.meng.toolset.eleTool.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class BoostInputCapacitanceChoose extends BaseDcdcCalculate implements OnClickListener {
    private SjfEditText svi;
    private SjfEditText svo;
    private SjfEditText svd;
    private SjfEditText sdeltaV;
    private SjfEditText sf;
    private SjfEditText sl;

    public void init() {
        setTitle("输入滤波电容选择");
        svi = addEditText("输入电压(V)");
        svo = addEditText("输出电压(V)");
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
            double deltaV = sdeltaV.getDouble() / 1000;
            double f = sf.getDouble() * 1000;
            double l = sl.getDouble() / 1000000;

            double Ci = getCin(vi, f, l, deltaV, vo, vd);
            double esr = getEsr(deltaV, f, l, vi, vo, vd);

            setResult(String.format(Locale.CHINA, "使用陶瓷电容:容量不小于:%.2fuF\n使用电解电容:ESR不大于%.2fmΩ", Ci * 1000000, esr * 1000));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double getEsr(double deltaV, double f, double l, double vi, double vo, double vd) {
        return (((deltaV * f * l) / vi) * ((vo + vd) / (vo + vd - vi)));
    }

    private double getCin(double vi, double f, double l, double deltaV, double vo, double vd) {
        return ((vi / (8 * f * f * l * deltaV)) * (1 - (vi / (vo + vd))));
    }
}
