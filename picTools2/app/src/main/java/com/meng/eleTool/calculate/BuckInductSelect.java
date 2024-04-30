package com.meng.eleTool.calculate;

import android.view.*;
import android.view.View.*;
import com.meng.mediatool.*;
import com.meng.view.*;

public class BuckInductSelect extends BaseDcdcCalculate {

    public void init() {
        setTitle("电感值选择");
        final SjfEditText vi = addEditText("输入电压(V)");
        final SjfEditText vo = addEditText("输出电压(V)");
        final SjfEditText vd = addEditText("二极管压降(V)");
        final SjfEditText io = addEditText("输出电流(A)");
        final SjfEditText f = addEditText("开关频率(kHz)");

        setButtonClick(new OnClickListener(){

                @Override
                public void onClick(View p1) {
                    try {
                        double[] resultRange = calculateL(vi.getDouble(), vo.getDouble(), vd.getDouble(), io.getDouble(), f.getDouble() * 1000);
                        setResult(String.format("合适的感值:%.2fuH~%.2fuH", resultRange[1] * 1000000, resultRange[0] * 1000000));

                    } catch (NumberFormatException e) {
                        MainActivity.instance.showToast("请输入正确的数字");
                    }
                }
            });
    }

    private double[] calculateL(double vi, double vo, double vd, double io, double f) {
        return new double[]{
            ((vo + vd) / (f * 0.2 * io)) * ((vi - vo) / (vi + vd)),
            ((vo + vd) / (f * 0.4 * io)) * ((vi - vo) / (vi + vd))
        };
    }
}
