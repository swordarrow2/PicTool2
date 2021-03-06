package com.meng.mediatool.tools.mengViews;

import android.app.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.picture.pixivPictureDownloader.*;
import com.meng.mediatool.tools.*;
import java.io.*;

public class MengProgressBar extends LinearLayout {
    public Activity context;
    private TextView fileNameTextView;
    private TextView textViewStatus;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    public PictureInfoJavaBean pictureInfoJavaBean;

    public MengProgressBar(Activity context, ListView listView, PictureInfoJavaBean pictureInfoJavaBean, String picUrl) {
        super(context);
        this.pictureInfoJavaBean = pictureInfoJavaBean;
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.list_item_downloading, this);
        fileNameTextView = (TextView) findViewById(R.id.main_list_item_textview_filename);
        textViewStatus = (TextView) findViewById(R.id.main_list_item_textview_statu);
        textViewProgress = (TextView) findViewById(R.id.main_list_item_textview_statu_byte);
        progressBar = (ProgressBar) findViewById(R.id.main_list_item_progressbar);

        String fileAbsolutePath = "";
        String expandName = picUrl.substring(picUrl.lastIndexOf(".") + 1, picUrl.length()).toLowerCase();
        String fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1, picUrl.lastIndexOf("."));
        if (expandName.equalsIgnoreCase("zip")) {
            fileAbsolutePath = FileHelper.getFileAbsPath(fileName + "." + expandName, FileType.pixivZIP);
        } else {
            if(pictureInfoJavaBean.staticPicJavaBean.body.size()>1){
				File folder = new File(FileHelper.getFileAbsPath(pictureInfoJavaBean.id,FileType.pixivDynamic));
				if(!folder.exists()) folder.mkdirs();		   
                fileAbsolutePath=FileHelper.getFileAbsPath(pictureInfoJavaBean.id+"/"+fileName+"."+expandName,FileType.pixivDynamic);
			  }else{
				  fileAbsolutePath=FileHelper.getFileAbsPath(fileName+"."+expandName,FileType.pixivDynamic);
			  }
        }
        MainActivity.instance.getFragment(PixivDownloadMain.class).threadPool.execute(new DownloadRunnable(this, picUrl, fileAbsolutePath, listView));
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

    public void setFileName(String fileName) {
        fileNameTextView.setText(fileName);
    }

}
