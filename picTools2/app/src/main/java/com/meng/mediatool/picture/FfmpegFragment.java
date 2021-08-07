package com.meng.mediatool.picture;

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
import com.meng.mediatool.tools.ExceptionCatcher;
import com.meng.mediatool.tools.Tools;
import com.meng.mediatool.tools.ffmpeg.FFmpeg;
import com.meng.mediatool.tools.ffmpeg.CommandBuilder;
import com.meng.mediatool.tools.mengViews.SjfProgressBar;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;

public class FfmpegFragment extends Fragment {

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
                    MainActivity.instance.selectVideo(FfmpegFragment.this);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.instance.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
                String path = Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData());
                try {
                    generate(path, path.substring(0, path.lastIndexOf(".")) + ".mp4");
                } catch (IOException e) {
                    ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择文件");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generate(String inputPath, String outputPath) throws IOException {
        File ipt = new File(inputPath);
        CommandBuilder cb = new CommandBuilder(ipt);
        File opt = new File(outputPath);
        cb.coverExistFile();
        SjfProgressBar spb = new SjfProgressBar(getActivity());
        spb.setTitle(ipt.getName() + " → " + opt.getName());
        ((LinearLayout) tv.getParent().getParent()).addView(spb, 0);
        MainActivity.instance.threadPool.execute(new ConverterRunnable(Runtime.getRuntime().exec(cb.build(opt)), spb));
        //getActivity().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg "+"-re -i /storage/emulated/0/01.flv -c copy -vcodec libx264 -acodec aac -f flv rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7&schedule=rtmp"
    }

    public static class ConverterRunnable implements Runnable {

        private Process process;
        private SjfProgressBar progressBar;
        private boolean isStart = false;
        private int lasttimestamp = 1;

        public ConverterRunnable(Process process, SjfProgressBar progressBar) {
            this.process = process;
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            try {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    final String ffmpegOutput = line.trim();
                    if (ffmpegOutput.contains("time=")) {
                        isStart = true;
                        final int total = getSeconed(ffmpegOutput.substring(ffmpegOutput.indexOf("time=") + 5, ffmpegOutput.indexOf(" bitrate")));
                        MainActivity.instance.runOnUiThread(new Runnable(){

                                @Override
                                public void run() {  
                                    progressBar.setStatusText("正在进行");
                                    progressBar.setProgress(total * 100 / lasttimestamp);
                                    progressBar.setProgressText(String.format("%.2f%%", total * 100f / lasttimestamp));
                                }
                            });
                    } else if (ffmpegOutput.startsWith("Duration")) {
                        lasttimestamp = getSeconed(ffmpegOutput.substring(ffmpegOutput.indexOf(":") + 1, ffmpegOutput.indexOf(",")).trim()); 
                    } else if (!ffmpegOutput.contains("time=") && isStart) {
                        MainActivity.instance.runOnUiThread(new Runnable(){

                                @Override
                                public void run() {  
                                    progressBar.setStatusText("完成");
                                    progressBar.setProgress(100);
                                    progressBar.setProgressText("100%");
                                }
                            });
                        isStart = false;
                    }
                }  
            } catch (Exception e) {
                ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
            }
        }


        private int getSeconed(String time) {
            //00:11:22.33
            return LocalTime.parse(time).getSecond();
        }

    }
}
