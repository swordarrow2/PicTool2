package com.meng.eleTool.calculate;

import android.view.*;
import android.view.View.*;
import com.meng.mediatool.*;
import com.meng.view.*;

public class BuckInductorCurrent extends BaseDcdcCalculate {

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

                        double delta = getDelta(vo, vd, f, l, vi);
                        double max = getMax(io, delta);
                        setResult(String.format("电感电流纹波:%.2fA\n电感峰值电流:%.2fA", delta, max));
                    } catch (NumberFormatException e) {
                        MainActivity.instance.showToast("请输入正确的数字");
                    }
                }
            });
    }

    private double getMax(double io, double delta) {
        return (io + delta / 2);
    }

    private double getDelta(double vo, double vd, double f, double l, double vi) {
        return (((vo + vd) / (f * l)) * ((vi - vo) / (vi + vd)));
    }
}
