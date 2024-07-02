package com.meng.customview;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.meng.toolset.mediatool.*;


public class MengColorPickerDialog extends Dialog {

    private EditText editText;
    private MengColorPicker mengColorPicker;

    public MengColorPickerDialog(Context context, EditText editText) {
        super(context);
        this.editText = editText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.meng_color_picker);
        final TextView tv = (TextView) findViewById(R.id.meng_color_textview);
        Button btnOK = (Button) findViewById(R.id.meng_color_picker_ok);
        Button btnCancal = (Button) findViewById(R.id.meng_color_picker_cancal);
        mengColorPicker = (MengColorPicker) findViewById(R.id.meng_color_picker);
        mengColorPicker.setOnColorBackListener(new MengColorPicker.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
                tv.setText(String.format("R：0x%H\nG：0x%H\nB：0x%H\n%s", r, g, b, mengColorPicker.getStrColor()));
                tv.setTextColor(Color.argb(a, r, g, b));
            }
        });
        btnOK.setOnClickListener(onClickListener);
        btnCancal.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.meng_color_picker_ok:
                    editText.setText(mengColorPicker.getStrColor());//no break
                case R.id.meng_color_picker_cancal:
                    hide();
            }
        }
    };
}
