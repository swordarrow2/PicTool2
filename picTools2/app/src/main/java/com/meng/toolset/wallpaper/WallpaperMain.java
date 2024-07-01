package com.meng.toolset.wallpaper;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.meng.app.BaseFragment;
import com.meng.app.Constant;
import com.meng.app.MainActivity;
import com.meng.toolset.mediatool.*;
import com.meng.tools.*;

public class WallpaperMain extends BaseFragment {
    
    private VideoWallpaper mVideoWallpaper = new VideoWallpaper();
    private Button select,silence,voice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wallpaper_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        select = (Button) view.findViewById(R.id.wallpaper_mainButtonselect);
        silence = (Button) view.findViewById(R.id.wallpaper_mainButtonsilence);
        voice = (Button) view.findViewById(R.id.wallpaper_mainButtonvoice);
        select.setOnClickListener(onclick);
    }

    OnClickListener onclick = new OnClickListener(){

        @Override
        public void onClick(View p1) {
            switch (p1.getId()) {
                case R.id.wallpaper_mainButtonselect:
                    selectVideo();
                    break;
                case R.id.wallpaper_mainButtonsilence:
                    VideoWallpaper.setVoiceSilence(MainActivity.instance);
                    break;
                case R.id.wallpaper_mainButtonvoice:
                    VideoWallpaper.setVoiceNormal(MainActivity.instance);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getData() != null) {
            if (requestCode == Constant.SELECT_FILE_REQUEST_CODE) {
                final String path = Tools.ContentHelper.absolutePathFromUri(getActivity(), data.getData());
                if (path == null) {
                    MainActivity.instance.showToast("选择视频出错");
                    return;
                }
                mVideoWallpaper.setToWallPaper(MainActivity.instance, path);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择视频");
        } else {
            selectImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

