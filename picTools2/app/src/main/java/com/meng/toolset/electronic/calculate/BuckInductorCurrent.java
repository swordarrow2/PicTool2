package com.meng.toolset.electronic.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class BuckInductorCurrent extends BaseDcdcCalculate implements OnClickListener {

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

    @Override
    public void onClick(View p1) {
        try {
            double vi = svi.getDouble();
            double vo = svo.getDouble();
            double vd = svd.getDouble();
            double io = sio.getDouble();
            double f = sf.getDouble() * 1000;
            double l = sl.getDouble() / 1000000;

            double delta = getDelta(vo, vd, f, l, vi);
            double max = getMax(io, delta);
            setResult(String.format(Locale.CHINA, "电感电流纹波:%.2fA\n电感峰值电流:%.2fA", delta, max));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double getMax(double io, double delta) {
        return (io + delta / 2);
    }

    private double getDelta(double vo, double vd, double f, double l, double vi) {
        return (((vo + vd) / (f * l)) * ((vi - vo) / (vi + vd)));
    }
}
