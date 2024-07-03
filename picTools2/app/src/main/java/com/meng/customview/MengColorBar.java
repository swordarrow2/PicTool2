package com.meng.customview;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.meng.toolset.mediatool.*;

public class MengColorBar extends LinearLayout {

    private EditText etTrue;
    private TextView tvTrue;
    private EditText etFalse;
    private TextView tvFalse;

    public MengColorBar(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.meng_colorbar, this);
        tvTrue = (TextView) findViewById(R.id.meng_colorbar_textview_trueDot);
        tvFalse = (TextView) findViewById(R.id.meng_colorbar_textview_falseDot);
        etTrue = (EditText) findViewById(R.id.meng_colorbar_edittext_true);
        etFalse = (EditText) findViewById(R.id.meng_colorbar_edittext_false);
        Button btnTrue = (Button) findViewById(R.id.meng_colorbar_button_select_true_color);
        Button btnFalse = (Button) findViewById(R.id.meng_colorbar_button_select_false_color);
        ImageButton imageButton = (ImageButton) findViewById(R.id.meng_colorbar_imagebutton);
        etTrue.addTextChangedListener(tw);
        etFalse.addTextChangedListener(tw);
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.meng_colorbar_button_select_true_color:
                        Dialog dialog = new MengColorPickerDialog(getContext(), etTrue);
                        dialog.show();
                        break;
                    case R.id.meng_colorbar_button_select_false_color:
                        Dialog dialog2 = new MengColorPickerDialog(getContext(), etFalse);
                        dialog2.show();
                        break;
                    case R.id.meng_colorbar_imagebutton:
                        new AlertDialog.Builder(context).setTitle("").setMessage("真值点就是普通二维码中的黑色部分,其余部分为假值点").setPositiveButton("我知道了", null).show();
                        break;
                }
            }
        };
        btnTrue.setOnClickListener(clickListener);
        btnFalse.setOnClickListener(clickListener);
        imageButton.setOnClickListener(clickListener);
    }

    public int getTrueColor() {
        return etTrue.getText().toString().trim().length() == 0 ?
                Color.parseColor(etTrue.getHint().toString()) :
                Color.parseColor(etTrue.getText().toString());
    }

    public int getFalseColor() {
        return etFalse.getText().toString().trim().length() == 0 ?
                Color.parseColor(etFalse.getHint().toString()) :
                Color.parseColor(etFalse.getText().toString());
    }

    TextWatcher tw = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
        }

        @Override
        public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            try {
                tvTrue.setTextColor(getTrueColor());
            } catch (Exception e) {
                tvTrue.setTextColor(Color.BLACK);
            }
            try {
                tvFalse.setTextColor(getFalseColor());
            } catch (Exception e) {
                tvFalse.setTextColor(Color.BLACK);
            }
        }

        @Override
        public void afterTextChanged(Editable p1) {
        }
    };
}
