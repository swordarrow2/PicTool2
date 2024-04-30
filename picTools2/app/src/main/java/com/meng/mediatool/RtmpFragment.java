package com.meng.mediatool;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.meng.mediatool.MainActivity;
import com.meng.mediatool.R;
import com.meng.mediatool.picture.FfmpegFragment;
import com.meng.mediatool.tools.ExceptionCatcher;
import com.meng.mediatool.tools.Tools;
import com.meng.mediatool.tools.ffmpeg.FFmpeg;
import com.meng.mediatool.tools.mengViews.SjfProgressBar;
import java.io.File;
import java.io.IOException;
import android.widget.EditText;
import android.widget.Button;
import com.meng.mediatool.tools.SharedPreferenceHelper;
import com.meng.mediatool.tools.*;

public class RtmpFragment extends BaseFragment {

    private EditText etRtmpServer;
    private EditText etPushCode;
    private Button btnSelectFile;
    private Button btnStart;
    private LinearLayout root;

    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rtmp_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etRtmpServer = (EditText) view.findViewById(R.id.rtmp_mainEditTextUrl);
        etPushCode = (EditText)view.findViewById(R.id.rtmp_mainEditTextCode);
        btnSelectFile = (Button)view.findViewById(R.id.rtmp_mainButtonSelect);
        btnStart = (Button)view.findViewById(R.id.rtmp_mainButtonStart);
        root = (LinearLayout) view.findViewById(R.id.rtmp_mainLinearLayout);
        btnStart.setOnClickListener(click);
        btnSelectFile.setOnClickListener(click);
        FFmpeg ffmpeg = FFmpeg.getInstance(this.getActivity());
        MainActivity.instance.showToast(ffmpeg.init(getActivity()) + "");
        String rtmp = SharedPreferenceHelper.getString("rtmp");
        if (rtmp != null) {
            etRtmpServer.setText(rtmp);
        }
        String pushCode = SharedPreferenceHelper.getString("code");
        if (pushCode != null) {
            etPushCode.setText(pushCode);
        }
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rtmp_mainButtonSelect:
                    selectVideo();
                    v.setEnabled(true);
                    break;
                case R.id.rtmp_mainButtonStart:
                    String rtmp = etRtmpServer.getText().toString();
                    String pushCode = etPushCode.getText().toString();
                    v.setEnabled(false);
                    if (!rtmp.startsWith("rtmp://")) {
                        MainActivity.instance.showToast("不是合法的rtmp地址");
                        return;
                    }
                    if (rtmp.equals("rtmp://")) {
                        MainActivity.instance.showToast("地址不能为空");
                        return;
                    }
                    if (pushCode.equals("")) {
                        MainActivity.instance.showToast("推流码不能为空");
                        return;
                    } 
                    SharedPreferenceHelper.putString("rtmp", rtmp);
                    SharedPreferenceHelper.putString("code", pushCode);
                    try {
                        push(rtmp + pushCode);
                        MainActivity.instance.showToast("开始向" + rtmp + pushCode + "推流");
                    } catch (IOException e) {
                        ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == StaticVars.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
                file = new File(Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData()));
                btnSelectFile.setText("已选择" + file.getName());
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择文件");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void push(String addr) throws IOException {
        SjfProgressBar spb = new SjfProgressBar(getActivity());
        spb.setTitle("推流:" + file.getName());
        root.addView(spb);
        Process process = Runtime.getRuntime().exec(
            getActivity().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg -re -i " + file.getAbsolutePath() +
            " -c copy -acodec aac -f flv " +
            addr  // "rtmp://live-push.bilivideo.com/live-bvc/" + "?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7&schedule=rtmp"
        );
        ThreadPool.execute(new FfmpegFragment.ConverterRunnable(process, spb));
    }
}
