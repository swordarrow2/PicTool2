package com.meng.customview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.meng.toolset.mediatool.R;

public class SjfProgressBar extends LinearLayout {
    public Activity context;
    private TextView fileNameTextView;
    private TextView textViewStatus;
    private TextView textViewProgress;
    private ProgressBar progressBar;

    public SjfProgressBar(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.list_item_downloading, this);
        fileNameTextView = (TextView) findViewById(R.id.main_list_item_textview_title);
        textViewStatus = (TextView) findViewById(R.id.main_list_item_textview_statu);
        textViewProgress = (TextView) findViewById(R.id.main_list_item_textview_statu_progress_text);
        progressBar = (ProgressBar) findViewById(R.id.main_list_item_progressbar);
    progressBar.setVisibility(View.VISIBLE);
        }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public void setStatusText(String statusText) {
        textViewStatus.setText(statusText);
    }

    public void setProgressText(String progressText) {
        textViewProgress.setText(progressText);
    }

    public void setTitle(String fileName) {
        fileNameTextView.setText(fileName);
    }
}
