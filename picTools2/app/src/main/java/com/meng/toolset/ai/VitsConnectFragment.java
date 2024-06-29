package com.meng.toolset.ai;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.meng.app.BaseFragment;
import com.meng.app.FunctionSavePath;
import com.meng.app.MainActivity;
import com.meng.tools.app.MNetwork;
import com.meng.toolset.mediatool.*;
import com.meng.tools.*;
import com.meng.tools.MaterialDesign.*;
import com.meng.tools.app.ThreadPool;
import com.meng.tools.hash.MD5;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Locale;

import com.meng.toolset.mediatool.task.*;
import com.meng.customview.MengSeekBar;

public class VitsConnectFragment extends BaseFragment implements OnClickListener {

    /*
     *@author 清梦
     *@date 2024-04-20 00:54:47
     */
    public static final String TAG = "VitsConnectFragment";

    private Button btnStart;
    private MDEditText etContent;
    private Spinner spCharaId;
    private MengSeekBar sbLength;
    private MengSeekBar sbNoice;
    private MengSeekBar sbNoicew;

    @Override
    public String getName() {
        return "vits语音合成";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vits_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart = (Button) view.findViewById(R.id.vits_mainButton);
        etContent = (MDEditText) view.findViewById(R.id.vits_mainMDEditTextContent);
        spCharaId = (Spinner) view.findViewById(R.id.vits_mainSpinner_charaId);
        sbLength = (MengSeekBar) view.findViewById(R.id.vits_main_seekbar_length);
        sbNoice = (MengSeekBar) view.findViewById(R.id.vits_main_seekbar_noice);
        sbNoicew = (MengSeekBar) view.findViewById(R.id.vits_main_seekbar_noicew);
        sbLength.setProgress(10);
        sbLength.setText("长度");
        sbNoice.setProgress(20);
        sbNoice.setText("情感起伏");
        sbNoicew.setProgress(91);
        sbNoicew.setText("音素长度");

        btnStart.setOnClickListener(this);

        ThreadPool.execute(new Runnable() {

            @Override
            public void run() {
                final VitsTtsBean vitsTtsBean = GSON.fromJson(MNetwork.httpGet("https://www.纯度.site/models"), VitsTtsBean.class);
                MainActivity.instance.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        spCharaId.setAdapter(new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, vitsTtsBean.models));
                        for (int i = 0; i < vitsTtsBean.models.size(); i++) {
                            if (vitsTtsBean.models.get(i).contains("素裳")) {
                                spCharaId.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View p1) {
        ThreadPool.execute(new Runnable() {

            @Override
            public void run() {
                downloadBySelf(etContent.getString(), spCharaId.getSelectedItemPosition() + "", sbLength.getProgress() / 10f, sbNoice.getProgress() / 100f, sbNoicew.getProgress() / 100f);
            }
        });

    }

    public void download(String content, String id) {
        String fileName = String.format(Locale.CHINA, "VITS - %s - %d - %s.wav", id, System.currentTimeMillis(), MD5.getMd5().calculate(content));
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.format("https://www.xn--wxtz62e.site/run?text=%s&id_speaker=%s", URLEncoder.encode(content), id)));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        request.allowScanningByMediaScanner();
        request.setMimeType("video/*");
        //request.addRequestHeader("Referer", "");
        request.addRequestHeader("User-Agent", "Android apk");
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName);
        long downloadId = downloadManager.enqueue(request);

    }

    public void downloadBySelf(final String content, final String id, final float length, final float noice, final float noicew) {

        new BackgroundTask() {

            @Override
            public void run() {
                try {
                    setStatus("正在连接");
                    long fileLength = -1;
                    String fileUrl = String.format("https://www.xn--wxtz62e.site/run?text=%s&id_speaker=%s&length=%s&noice=%s&noicew=%s", URLEncoder.encode(content), id, length, noice, noicew);
                    MainActivity.instance.showToast(fileUrl);
                    if (true) {
                        //  return;
                    }
                    URL url = new URL(fileUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.addRequestProperty("User-Agent", "Android apk");
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String contentLength = connection.getHeaderField("Content-Length");
                        if (contentLength != null) {
                            fileLength = Long.parseLong(contentLength);
                            setMaxProgress((int) fileLength);
                            setStatus("正在下载");
                        }
                    }
                    String fname = String.format(Locale.CHINA, "VITS - %s - %d - %s", id, System.currentTimeMillis(), MD5.getMd5().calculate(content));
                    File savedVoice = FileTool.getAppFile(FunctionSavePath.TtsVoice, fname, FileFormat.FileType.wav);
                    try (FileOutputStream output = new FileOutputStream(savedVoice)) {
                        try (InputStream input = connection.getInputStream()) {
                            setProgress(0);
                            int index;
                            byte buffer[] = new byte[4 * 1024];
                            while ((index = input.read(buffer)) != -1) {
                                addProgress(index);
                                output.write(buffer, 0, index);
                            }
                            output.flush();
                        }
                    }
                } catch (Exception e) {
                    setStatus("合成失败");
                    MainActivity.instance.showToast(e.toString());
                }
            }
        }.setTitle("VITS声音合成:" + spCharaId.getSelectedItem().toString()).start();

    }

    class VitsTtsBean {
        //  public int index;
        List<String> models;
        // public int role;
    }

}
