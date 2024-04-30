package com.meng.eleTool.calculate;

import android.view.*;
import android.view.View.*;
import com.meng.mediatool.*;
import com.meng.view.*;
import java.math.*;

public class BoostInductSelect extends BaseDcdcCalculate {

    public void init() {
        setTitle("电感值选择(测试)");
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
//                        cal(BigDecimal.valueOf(vi.getDouble()),
//                        BigDecimal.valueOf(vo.getDouble()),
//                        BigDecimal.valueOf(vd.getDouble()),
//                        BigDecimal.valueOf(io.getDouble()),
//                        BigDecimal.valueOf(f.getDouble()));
                    } catch (NumberFormatException e) {
                        MainActivity.instance.showToast("请输入正确的数字");
                    }
                }
            });                                          
    }

//    private void cal(BigDecimal vi,BigDecimal vo,BigDecimal vd,BigDecimal io,BigDecimal f){
//        BigDecimal p1,p2,p3,l;
//        p1=vi.divide(io.multiply(f).multiply(BigDecimal.valueOf(0.2)),20,BigDecimal.ROUND_CEILING);
//        p2=BigDecimal.valueOf(1).subtract(vi.divide(vo.add(vd),20,BigDecimal.ROUND_CEILING));
//        p3=vi.divide(vo.add(vd),20,BigDecimal.ROUND_CEILING);
//        l=p1.multiply(p2).multiply(p3);
//        MainActivity.instance.showToast(l.toString()+"H");
//    }

    private double[] calculateL(double vi, double vo, double vd, double io, double frequency) {
        return new double[]{
            (vi / (0.2f * frequency * io)) *
            (1 - (vi / (vo + vd))) * 
            (vi / (vo + vd)),
            (vi / (0.4f * frequency * io)) * 
            (1 - (vi / (vo + vd))) * 
            (vi / (vo + vd))
        };
    }
}
//L=(Ui/(X*f*Io))*(1-Ui/(Uo+Ud))*(Ui/(Uo+Ud))
