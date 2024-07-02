package com.meng.toolset.electronic.calculate;

import android.view.*;
import android.view.View.*;

import com.meng.app.MainActivity;
import com.meng.customview.*;

import java.util.Locale;

public class BuckInductSelect extends BaseDcdcCalculate implements OnClickListener {

    private MengEditText vi;
    private MengEditText vo;
    private MengEditText vd;
    private MengEditText io;
    private MengEditText f;

    public void init() {
        setTitle("电感值选择");
        vi = addEditText("输入电压(V)");
        vo = addEditText("输出电压(V)");
        vd = addEditText("二极管压降(V)");
        io = addEditText("输出电流(A)");
        f = addEditText("开关频率(kHz)");

        setButtonClick(this);
    }

    @Override
    public void onClick(View p1) {
        try {
            double[] resultRange = calculateL(vi.getDouble(), vo.getDouble(), vd.getDouble(), io.getDouble(), f.getDouble() * 1000);
            setResult(String.format(Locale.CHINA, "合适的感值:%.2fuH~%.2fuH", resultRange[1] * 1000000, resultRange[0] * 1000000));
        } catch (NumberFormatException e) {
            MainActivity.instance.showToast("请输入正确的数字");
        }
    }

    private double[] calculateL(double vi, double vo, double vd, double io, double f) {
        return new double[]{
                ((vo + vd) / (f * 0.2 * io)) * ((vi - vo) / (vi + vd)),
                ((vo + vd) / (f * 0.4 * io)) * ((vi - vo) / (vi + vd))
        };
    }
}
