package com.meng.mediatool.tools.mengViews;

import android.app.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.picture.pixiv.*;
import com.meng.mediatool.tools.*;
import java.io.*;

public class MengProgressBar extends LinearLayout {
    public Activity context;
    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvProgress;
    private ProgressBar progressBar;   

    public MengProgressBar(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.list_item_downloading, this);
        tvTitle = (TextView) findViewById(R.id.main_list_item_textview_title);
        tvStatus = (TextView) findViewById(R.id.main_list_item_textview_statu);
        tvProgress = (TextView) findViewById(R.id.main_list_item_textview_statu_progress_text);
        progressBar = (ProgressBar) findViewById(R.id.main_list_item_progressbar);
    }

    public void bindingPixivDownload(ListView listView, PictureInfoJavaBean pictureInfoJavaBean, String picUrl) {     
        String fileAbsolutePath = "";
        String extendName = picUrl.substring(picUrl.lastIndexOf(".") + 1, picUrl.length()).toLowerCase();
        String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1, picUrl.lastIndexOf("."));
        if (extendName.equalsIgnoreCase("zip")) {
            fileAbsolutePath = FileTool.getSaveFileAbsPath(fileName, FileType.pixivZIP, FileFormat.FileType.zip);
        } else if (pictureInfoJavaBean.staticPicJavaBean.body.size() > 1) {
            fileAbsolutePath = FileTool.getSaveFileAbsPath(pictureInfoJavaBean.id + "/" + fileName, FileType.pixivDynamic, extendName);            
            File folder = new File(fileAbsolutePath).getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }		   
        } else {
            fileAbsolutePath = FileTool.getSaveFileAbsPath(fileName, FileType.pixivDynamic, extendName);
        }        
        MFragmentManager.getInstance().getFragment(PixivDownloadMain.class).threadPool.execute(new DownloadRunnable(this, picUrl, fileAbsolutePath, listView, pictureInfoJavaBean));
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public int getProgress() {
        return progressBar.getProgress();
    }

    public void setStatusText(String statusText) {
        tvStatus.setText(statusText);
    }

    public String getStatusText() {
        return tvStatus.getText().toString();
    }

    public void setProgressText(String progressText) {
        tvProgress.setText(progressText);
    }

    public String getProgressText() {
        return tvProgress.getText().toString();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }   

    public String getTitle() {
        return tvTitle.getText().toString();
    }

}
