package com.meng.customview;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.meng.toolset.mediatool.*;

public class MengSeekBar extends LinearLayout {
    private TextView textView;
    private SeekBar seekBar;
    public MengSeekBar(Context context) {
        super(context);
        afterCreate(context, null);
    }
    public MengSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        afterCreate(context, attributeSet);
    }
    private void afterCreate(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.meng_seekbar_view, this);
        textView = (TextView) findViewById(R.id.progress_view_tv);
        seekBar = (SeekBar) findViewById(R.id.progress_view_sb);
//        if (attributeSet != null) {
//            MainActivity.instance.showToast(attributeSet.toString());
//            textView.setText(attributeSet.getAttributeValue("android", "text"));
//            seekBar.setProgress(attributeSet.getAttributeIntValue("android", "progress", 0));
//        }
    }
    public void setMax(int max) {
        seekBar.setMax(max);
    }
    public int getMax() {
        return seekBar.getMax();
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        seekBar.setOnSeekBarChangeListener(listener);        
    }
    
    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
            
        }

        @Override
        public void onStartTrackingTouch(SeekBar p1) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar p1) {
        }
    };
}
