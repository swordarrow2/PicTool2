package com.meng.toolset.picture.pixiv;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.widget.*;

import com.meng.app.*;
import com.meng.customview.*;
import com.meng.tools.*;
import com.meng.tools.app.*;
import com.meng.app.database.*;
import com.meng.toolset.mediatool.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

public class DownloadRunnable implements Runnable {

    private String picUrl = "";
    private String absolutePath = "";
    private MengProgressBar mengProgressBar;
    private long imageSize = 0;
    private long downloadedFileSize = 0;
    private int filesNow = 0;
    private long filesCount = 0;
    private ListView listView;
    private TaskState taskState = TaskState.nothing;
    public PictureInfoJavaBean pictureInfoJavaBean;
    private String title = "pids";

    public DownloadRunnable(MengProgressBar mengProgressBar, String picUrl, String absolutePath, ListView listView, PictureInfoJavaBean pijb) {
        this.mengProgressBar = mengProgressBar;
        this.picUrl = picUrl;
        this.listView = listView;
        this.absolutePath = absolutePath;
        pictureInfoJavaBean = pijb;
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (taskState != TaskState.end) {
                    switch (taskState) {
                        case nothing:
                            break;
                        case connecting:
                        case downloading:
                            setDownloadProgress(downloadedFileSize, imageSize);
                            break;
                        case creatingGif:
                            setCreateGifProgress(filesNow, filesCount);
                            break;
                        case end:
                            break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        File file = new File(absolutePath);
        taskState = TaskState.connecting;
        try {
            URL u = new URL(picUrl);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Referer", picUrl);
            connection.setRequestProperty("cookie", SharedPreferenceHelper.getPixivCookie());
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
            connection.setReadTimeout(30000);
            imageSize = connection.getContentLength();
            setProgressBarFileName(file.getName());
            taskState = TaskState.downloading;
            if (file.exists()) {
                if (file.length() == imageSize) {
                    MainActivity.instance.showToast(mengProgressBar.context.getString(R.string.file_exist) + file.getName());
                } else {
                    MainActivity.instance.showToast(mengProgressBar.context.getString(R.string.file_exist) + "但似乎并不完整，正在重新下载");
                    InputStream is = connection.getInputStream();
                    taskState = TaskState.downloading;
                    if (is != null) {
                        FileOutputStream fos = new FileOutputStream(file);
                        byte buf[] = new byte[4096];
                        int len = 0;
                        while ((len = is.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                            downloadedFileSize += len;
                        }
                    }
                    is.close();
                }
            } else {
                InputStream is = connection.getInputStream();
                if (is != null) {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte buf[] = new byte[4096];
                    int len = 0;
                    while ((len = is.read(buf)) > 0) {
                        fos.write(buf, 0, len);
                        downloadedFileSize += len;
                    }
                }
                is.close();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            MainActivity.instance.showToast("下载异常" + e.toString());
            downloadEnd();
            return;
        }
        if (pictureInfoJavaBean.isAnimPicture) {
            String zipName = absolutePath.substring(absolutePath.lastIndexOf("/") + 1, absolutePath.lastIndexOf("."));
            setProgressBarFileName(pictureInfoJavaBean.id + ".gif");
            taskState = TaskState.creatingGif;
            try {
                filesCount = countFilesInZip(file);
                ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
                ZipEntry ze = zis.getNextEntry();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
                localAnimatedGifEncoder.start(baos);//start
                localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放

                List<AnimPicJavaBean.Body.Frames> lf = pictureInfoJavaBean.animPicJavaBean.body.frames;
                int i = 0;
                while (ze != null) {
                    filesNow++;
                    localAnimatedGifEncoder.setDelay(Integer.parseInt(lf.get(i).delay));
                    ++i;
                    localAnimatedGifEncoder.addFrame(BitmapFactory.decodeStream(zis));
                    ze = zis.getNextEntry();
                }
                localAnimatedGifEncoder.finish();
                File outputFile = FileTool.getAppFile(FunctionSavePath.awesomeQR, FileTool.FileType.gif_89a);
                FileOutputStream fos = new FileOutputStream(outputFile);
                baos.writeTo(fos);
                baos.flush();
                fos.flush();
                baos.close();
                fos.close();
                registImage(outputFile.getAbsolutePath());
            } catch (Exception e) {
                MainActivity.instance.showToast("生成gif出错");
                downloadEnd();
                return;
            }
            filesNow = 0;
            MainActivity.instance.showToast(zipName + ".gif" + "完成");
            if (SharedPreferenceHelper.isDeleteZipAfterGenenalGif()) {
                file.delete();
            }
        } else {
            registImage(file.getAbsolutePath());
        }
        taskState = TaskState.end;
        DataBaseHelper.getInstance(PixivDataBase.class).deleteFailed(picUrl);
        downloadEnd();
    }

    private void setProgressBarFileName(final String gifPath) {
        mengProgressBar.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mengProgressBar.setTitle(gifPath);
            }
        });
    }

    /*private void createGifJava(String folder, String fileName) {
     List<AnimPicJavaBean.Body.Frames> lf = mengProgressBar.pictureInfoJavaBean.animPicJavaBean.body.frames;
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
     localAnimatedGifEncoder.start(baos);//start
     localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放

     for (int i = 0; i < lf.size(); i++) {
     localAnimatedGifEncoder.setDelay(Integer.parseInt(lf.get(i).delay));
     localAnimatedGifEncoder.addFrame(BitmapFactory.decodeFile(folder + lf.get(i).file));
     filesNow = i;
     }
     localAnimatedGifEncoder.finish();
     String path = FileHelper.getFileAbsPath(fileName, FunctionName.pixivAnimate);
     try {
     FileOutputStream fos = new FileOutputStream(path);
     baos.writeTo(fos);
     baos.flush();
     fos.flush();
     baos.close();
     fos.close();
     } catch (IOException e) {
     LogTool.e("gif异常" + e.toString());
     }
     registImage(path);
     }
     */
    private int countFilesInZip(File zipFile) {
        int filesCount = 0;
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                filesCount++;
                ze = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesCount;
    }

    private void setDownloadProgress(final float downloadedSize, final float fileSize) {
        mengProgressBar.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float progress = downloadedSize / fileSize * 100;
                mengProgressBar.setProgress((int) progress);
                if (downloadedSize == 0) {
                    mengProgressBar.setStatusText("正在连接");
                } else {
                    mengProgressBar.setStatusText("正在下载");
                    mengProgressBar.setProgressText(MessageFormat.format("{0}B/{1}B ({2}%)", downloadedSize, fileSize, progress));
                }
            }
        });
    }

    private void setCreateGifProgress(final float gifedCount, final float fileCount) {
        mengProgressBar.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mengProgressBar.setProgress((int) (gifedCount / fileCount * 100));
                mengProgressBar.setStatusText("正在合成gif");
                mengProgressBar.setProgressText(MessageFormat.format("{0}/{1}", gifedCount, fileCount));
            }
        });
    }

    private void registImage(String path) {
        mengProgressBar.context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

    private void downloadEnd() {
        mengProgressBar.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] downloadedFilesName = FileTool.getAppFile(FunctionSavePath.pixivZIP, "").list();
                Arrays.sort(downloadedFilesName);
                listView.setAdapter(new ArrayAdapter<String>(mengProgressBar.context, android.R.layout.simple_list_item_1, downloadedFilesName));
                LinearLayout ll = (LinearLayout) mengProgressBar.getParent();
                ll.removeView(mengProgressBar);
                System.gc();
            }
        });
    }
    //使用系统下载器下载
    /*  private void systemDownload(String zipUrl){
     String expandName = zipUrl.substring(zipUrl.lastIndexOf(".")+1,zipUrl.length()).toLowerCase();
	 String fileName = zipUrl.substring(zipUrl.lastIndexOf("/")+1,zipUrl.lastIndexOf("."));
	 DownloadManager.Request request = new DownloadManager.Request(Uri.parse(zipUrl));
	 request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
	 request.setVisibleInDownloadsUi(true);
	 request.setTitle("Pixiv图片下载");
	 request.setDescription(fileName+"."+expandName);
	 request.setDestinationInExternalPublicDir(MainActivity.instence.getPixivZipPath(""),fileName+"."+expandName);
	 request.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
	 request.addRequestHeader("cookie",SharedPreferenceHelper.getValue(Data.preferenceKeys.cookieValue));
	 request.addRequestHeader("Referer",picUrl);
	 DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	 downloadManager.enqueue(request);
	 context.registerReceiver(new BroadcastReceiver() {
	 @Override
	 public void onReceive(Context context,Intent intent){
	 isDownloaded=true;
	 }
	 },new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	 }
	 */
}
