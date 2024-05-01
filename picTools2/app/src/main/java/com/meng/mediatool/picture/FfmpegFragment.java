package com.meng.mediatool.picture;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.tools.ffmpeg.*;
import com.meng.mediatool.tools.mengViews.*;
import java.io.*;
import java.time.*;

import java.lang.Process;
import android.media.*;
import android.provider.*;
import com.meng.mediatool.task.*;
import com.meng.mediatool.tools.MaterialDesign.*;

public class FfmpegFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivThumb;
    private Button btnSelect;
    private Button btnStart;
    private File selectFile;
    private MDEditText etOutputName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ffmpeg_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSelect = (Button) view.findViewById(R.id.ffmpeg_mainButton_select);
        btnStart = (Button) view.findViewById(R.id.ffmpeg_mainButton_start);
        ivThumb = (ImageView) view.findViewById(R.id.ffmpeg_mainImageView);
        etOutputName = (MDEditText) view.findViewById(R.id.ffmpeg_mainMDEditText_output_name);
        btnStart.setOnClickListener(this);
        btnSelect.setOnClickListener(this);
        FFmpeg ffmpeg = FFmpeg.getInstance(this.getActivity());
        if (!ffmpeg.init(getActivity())) {
            MainActivity.instance.showToast("ffmpeg加载失败");
            MFragmentManager.getInstance().showFragment(Welcome.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ffmpeg_mainButton_select:
                selectVideo();
                break;
            case R.id.ffmpeg_mainButton_start:
                try {
                    File file = new File(etOutputName.getString());
                 //   System.out.println(file.getAbsolutePath());
                    generate(selectFile, file);
                    
                } catch (IOException e) {
                    MainActivity.instance.showToast(e.toString());  
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == StaticVars.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
                selectFile = new File(Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData()));    
                ivThumb.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectFile.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND));
                btnStart.setEnabled(true);
                etOutputName.setVisibility(View.VISIBLE);
                etOutputName.setText(
                    selectFile.getParent() + "/" + 
                    selectFile.getAbsolutePath().substring(
                        selectFile.getAbsolutePath().lastIndexOf("/") + 1, 
                        selectFile.getAbsolutePath().lastIndexOf(".") + 1));
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择文件");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generate(File ipt, File opt) throws IOException {
        CommandBuilder cb = new CommandBuilder(ipt);
        cb.coverExistFile();
//        SjfProgressBar spb = new SjfProgressBar(getActivity());
//        spb.setTitle(ipt.getName() + " → " + opt.getName());
//        ((LinearLayout) tv.getParent().getParent()).addView(spb, 0);
//        ThreadPool.execute(new ConverterRunnable(Runtime.getRuntime().exec(cb.build(opt)), spb));
        //getActivity().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg "+"-re -i /storage/emulated/0/01.flv -c copy -vcodec libx264 -acodec aac -f flv rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7&schedule=rtmp"
        new Converter(Runtime.getRuntime().exec(cb.build(opt))).setTitle(ipt.getName() + " → " + opt.getName()).start();
    }

    private class Converter extends BackgroundTask {

        private Process process;            
        private boolean isStart = false;
        private int lasttimestamp = 1;

        public Converter(Process process) {
            this.process = process;
        }      

        @Override
        public void run() {
            try {
                setMaxProgress(Integer.MAX_VALUE);
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    final String ffmpegOutput = line.trim();
                    System.out.println(ffmpegOutput);
                    if (ffmpegOutput.contains("time=")) {
                        isStart = true;
                        final int total = getSeconed(ffmpegOutput.substring(ffmpegOutput.indexOf("time=") + 5, ffmpegOutput.indexOf(" bitrate")));
                        MainActivity.instance.runOnUiThread(new Runnable(){

                                @Override
                                public void run() {  
                                    setStatus("正在进行");
                                    setProgress(total);
                                    setProgressText(String.format("%.2f%%", total * 100f / lasttimestamp));
                                }
                            });
                    } else if (ffmpegOutput.startsWith("Duration")) {
                        lasttimestamp = getSeconed(ffmpegOutput.substring(ffmpegOutput.indexOf(":") + 1, ffmpegOutput.indexOf(",")).trim()); 
                        setMaxProgress(lasttimestamp);
                    } else if (!ffmpegOutput.contains("time=") && isStart) {
                        MainActivity.instance.runOnUiThread(new Runnable(){

                                @Override
                                public void run() {  
                                    setStatus("完成");
                                    setProgress(100);
                                    setProgressText("100%");
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

    };

    
}
