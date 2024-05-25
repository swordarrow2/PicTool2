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
import com.meng.mediatool.task.*;

public class VitsConnectFragment extends BaseFragment implements OnClickListener {

    /*
     *@author 清梦
     *@date 2024-04-20 00:54:47
     */
    public static final String TAG = "VitsConnectFragment";

    private Button btnStart;
    private MDEditText etContent;
    private Spinner spCharaId;

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
        btnStart.setOnClickListener(this);

        ThreadPool.execute(new Runnable(){

                @Override
                public void run() {
                    final VitsTtsBean vb = GSON.fromJson(MNetwork.httpGet("https://www.纯度.site/models"), VitsTtsBean.class);
                    MainActivity.instance.runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                spCharaId.setAdapter(new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, vb.models));
                                int index = 0;
                                for (String m: vb.models) {                                    
                                    if (m.contains("素裳")) {
                                        break;
                                    }
                                    ++index;
                                }
                                spCharaId.setSelection(index);
                            }
                        });                  
                }
            });
    }

    @Override
    public void onClick(View p1) {
        ThreadPool.execute(new Runnable(){

                @Override
                public void run() {
                    try {
                        downloadBySelf(etContent.getString(), spCharaId.getSelectedItemPosition() + "");
                    } catch (Exception e) {
                        e.printStackTrace();System.out.println(e.toString());
                    }
                }
            });

    }

    public void download(String content, String id) {
        String fileName = String.format("VITS - %s - %d - %s.wav", id, System.currentTimeMillis(), Hash.MD5.getMd5().calculate(content));
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

    public void downloadBySelf(final String content, final String id) throws MalformedURLException, IOException {

        new BackgroundTask(){

            @Override
            public void run() {
                try {
                    setStatus("正在连接");
                    long fileLength=-1;
                    String fileUrl = String.format("https://www.xn--wxtz62e.site/run?text=%s&id_speaker=%s", URLEncoder.encode(content), id);
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
                            setMaxProgress((int)fileLength);
                            setStatus("正在下载");
                        }
                    }
                    String fname = String.format("VITS - %s - %d - %s", id, System.currentTimeMillis(), Hash.MD5.getMd5().calculate(content));
                    File savedVoice = new File(FileTool.getSaveFileAbsPath(fname, FileType.TtsVoice, FileFormat.FileType.wav));        
                    FileOutputStream output = new FileOutputStream(savedVoice);
                    try {          
                        InputStream input = connection.getInputStream(); 
                        setProgress(0);
                        int index;
                        byte buffer [] = new byte[4 * 1024];  
                        while ((index = input.read(buffer)) != -1) {  
                            addProgress(index);
                            output.write(buffer, 0, index); 
                        }  
                        output.flush();  
                    } catch (Exception e) {  
                        e.printStackTrace(); 
                        setStatus("合成失败");
                    } finally {  
                        try {  
                            output.close();  
                        } catch (Exception e) {  
                            e.printStackTrace();  
                            setStatus("出现错误");
                        }  
                    }  

                } catch (Exception e) {
                    MainActivity.instance.showToast(e.toString());
                } 
            }
        }.setTitle("VITS声音合成:" + spCharaId.getSelectedItem().toString()).start();

    }

}
