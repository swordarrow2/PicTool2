package com.meng.mediatool;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.task.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.tools.MaterialDesign.*;
import com.meng.mediatool.tools.ffmpeg.*;
import com.meng.mediatool.tools.mengViews.*;
import java.io.*;
import java.time.*;

import java.lang.Process;

public class FfmpegFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivThumb;
    private Button btnSelect;
    private Button btnStart;
    private File selectFile;
    private MDEditText etCommand;
    private TextView tvLog;

    private CommandBuilder cb;
    
    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ffmpeg_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        btnSelect = (Button) view.findViewById(R.id.ffmpeg_mainButton_select);
        btnStart = (Button) view.findViewById(R.id.ffmpeg_mainButton_start);
        ivThumb = (ImageView) view.findViewById(R.id.ffmpeg_mainImageView);
        etCommand = (MDEditText) view.findViewById(R.id.ffmpeg_mainMDEditText_output_name);
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
                    generate(cb.getInput(), cb.getOutput());
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

                String output =
                    selectFile.getParent() + "/" + 
                    selectFile.getAbsolutePath().substring(
                    selectFile.getAbsolutePath().lastIndexOf("/") + 1, 
                    selectFile.getAbsolutePath().lastIndexOf(".") + 1);

                cb = new CommandBuilder(selectFile);
                cb.coverExistFile();
                String build = cb.build(new File(output));
                etCommand.setText(build);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择文件");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generate(File ipt, File opt) throws IOException {

//        SjfProgressBar spb = new SjfProgressBar(getActivity());
//        spb.setTitle(ipt.getName() + " → " + opt.getName());
//        ((LinearLayout) tv.getParent().getParent()).addView(spb, 0);
//        ThreadPool.execute(new ConverterRunnable(Runtime.getRuntime().exec(cb.build(opt)), spb));
        //getActivity().getFilesDir().getAbsolutePath() + File.separator + "ffmpeg "+"-re -i /storage/emulated/0/01.flv -c copy -vcodec libx264 -acodec aac -f flv rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7&schedule=rtmp"
        String name = ipt.getName() + " → " + opt.getName();        
        tvLog = (TextView) mainView.findViewById(R.id.ffmpeg_mainTextView_log);        
        new FFmpegBackTask(Runtime.getRuntime().exec(etCommand.getString()), name, tvLog).setTitle(name).start();
    }    
}
