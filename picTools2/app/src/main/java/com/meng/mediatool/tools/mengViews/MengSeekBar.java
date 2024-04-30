package com.meng.mediatool.tools.mengViews;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import java.text.*;

public class MengSeekBar extends LinearLayout {
    private TextView textView;
    private SeekBar seekBar;
    public MengSeekBar(Context context) {
        super(context);
        afterCreate(context);
    }
    public MengSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        afterCreate(context);
    }
    private void afterCreate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.meng_seekbar_view, this);
        textView = (TextView) findViewById(R.id.progress_view_tv);
        seekBar = (SeekBar) findViewById(R.id.progress_view_sb);
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
}
