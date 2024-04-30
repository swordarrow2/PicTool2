package com.meng.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class SjfEditText extends EditText {

    /*
     *@author 清梦
     *@date 2024-04-19 19:47:49
     */
    public static final String TAG = "SjfEditText";
    
    public SjfEditText(Context context) {
        super(context);
    }


    public SjfEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SjfEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SjfEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public double getDouble() throws NumberFormatException {
        double d = Double.parseDouble(getText().toString());
        if (Double.isNaN(d)) {
            throw new NumberFormatException("NaN");
        }
        return d;
    }
}
