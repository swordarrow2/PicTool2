package com.meng.toolset.electronic.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class BuckOutputCapacitanceChoose extends BaseDcdcCalculate implements OnClickListener {

    private MengEditText svi;
    private MengEditText svo;
    private MengEditText svd;
    private MengEditText sdeltaV;
    private MengEditText sf;
    private MengEditText sl;

    public void init() {
        setTitle("输出滤波电容选择");
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

            double Ci = getCout(vo, vd, f, l, deltaV, vi);
            double esr = getEsr(deltaV, f, l, vi, vd, vo);
            setResult(String.format(Locale.CHINA, "使用陶瓷电容:容量不小于:%fuF\n使用电解电容:ESR不大于%fmΩ", Ci * 1000000, esr * 1000));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double getEsr(double deltaV, double f, double l, double vi, double vd, double vo) {
        return ((deltaV * f * l * (vi + vd)) / ((vo + vd) * (vi - vo)));
    }

    private double getCout(double vo, double vd, double f, double l, double deltaV, double vi) {
        return (((vo + vd) / (8 * f * f * l * deltaV)) * ((vi - vo) / (vi + vd)));
    }
}
