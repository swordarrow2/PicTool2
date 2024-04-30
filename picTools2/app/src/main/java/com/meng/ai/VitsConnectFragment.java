package com.meng.ai;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.tools.MaterialDesign.*;
import com.meng.mediatool.tools.mengViews.*;
import java.io.*;
import java.net.*;

public class VitsConnectFragment extends BaseFragment implements OnClickListener {

    /*
     *@author 清梦
     *@date 2024-04-20 00:54:47
     */
    public static final String TAG = "VitsConnectFragment";

    private Button btnStart;
    private MDEditText etId;
    private MDEditText etContent;
    private TextView tvIdList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vits_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart = (Button) view.findViewById(R.id.vits_mainButton);
        etId = (MDEditText) view.findViewById(R.id.vits_mainMDEditTextId);
        etContent = (MDEditText) view.findViewById(R.id.vits_mainMDEditTextContent);
        tvIdList = (TextView) view.findViewById(R.id.vits_mainTextView);
        btnStart.setOnClickListener(this);

        ThreadPool.execute(new Runnable(){

                @Override
                public void run() {
                    final VitsTtsBean vb = GSON.fromJson(
                        MNetwork.httpGet("https://www.纯度.site/models"),
                        VitsTtsBean.class);
                    MainActivity.instance.runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                tvIdList.setText(vb.models.toString());
                            }
                        });
                }
            });
    }

    @Override
    public void onClick(View p1) {
        download(etContent.getString(), etId.getString());
    }

    public void download(String content, String id) {
        String fileName = String.format("VITS - %s - %s - %d.wav", id, Hash.MD5.getMd5().calculate(content), System.currentTimeMillis());
        DownloadManager downloadManager = (DownloadManager)getActivity(). getSystemService(Context.DOWNLOAD_SERVICE);
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


}
