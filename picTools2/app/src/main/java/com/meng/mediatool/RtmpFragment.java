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

public class RtmpFragment extends Fragment {

    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ffmpeg_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView)view.findViewById(R.id.ffmpeg_mainTextView1);
        tv.setOnClickListener(click);
        FFmpeg ffmpeg = FFmpeg.getInstance(this.getActivity());
        MainActivity.instance.showToast(ffmpeg.init(getActivity()) + "");
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ffmpeg_mainTextView1:
                    MainActivity.instance.selectVideo(RtmpFragment.this);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.instance.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
                try {
                    push(Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData()));
                } catch (IOException e) {
                    ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择文件");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void push(String inputPath) throws IOException {
        File ipt = new File(inputPath);
        SjfProgressBar spb = new SjfProgressBar(getActivity());
        spb.setTitle("推流:" + ipt.getName());
        ((LinearLayout) tv.getParent().getParent()).addView(spb, 0);
        Process process = Runtime.getRuntime().exec((getActivity().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg -re -i " + ipt.getAbsolutePath() + " -c copy -vcodec libx264 -acodec aac -f flv rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7&schedule=rtmp"));
        MainActivity.instance.threadPool.execute(new FfmpegFragment.ConverterRunnable(process, spb));
    }
}
