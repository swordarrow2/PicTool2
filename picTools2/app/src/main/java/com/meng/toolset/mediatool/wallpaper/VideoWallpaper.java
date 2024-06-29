package com.meng.toolset.mediatool.wallpaper;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.service.wallpaper.*;
import android.text.*;
import android.view.*;

import com.meng.app.Constant;

import java.io.*;

public class VideoWallpaper extends WallpaperService {
 //   private static final String TAG = VideoWallpaper.class.getName();
    private static String sVideoPath;
    /**
     * 设置静音
     * @param context
     */
    public static void setVoiceSilence(Context context) {
        Intent intent = new Intent(Constant.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(Constant.ACTION, Constant.ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    /**
     * 设置有声音
     * @param context
     */
    public static void setVoiceNormal(Context context) {
        Intent intent = new Intent(Constant.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(Constant.ACTION, Constant.ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    /**
     * 设置壁纸
     * @param context
     */
    public void setToWallPaper(Context context, String videoPath) {
        try {
            context.clearWallpaper();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sVideoPath = videoPath;
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, VideoWallpaper.class));
        context.startActivity(intent);
    }


    @Override
    public Engine onCreateEngine() {
        return new VideoWallpagerEngine();
    }

    class VideoWallpagerEngine extends Engine {
        private MediaPlayer mMediaPlayer;
        private BroadcastReceiver mVideoVoiceControlReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(Constant.VIDEO_PARAMS_CONTROL_ACTION);
            mVideoVoiceControlReceiver = new VideoVoiceControlReceiver();
            registerReceiver(mVideoVoiceControlReceiver, intentFilter);
        }

        @Override
        public void onDestroy() {
            unregisterReceiver(mVideoVoiceControlReceiver);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            //    holder.setFixedSize(1080,1920);

            if (TextUtils.isEmpty(sVideoPath)) {
                throw new NullPointerException("videoPath must not be null ");
            } else {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setSurface(holder.getSurface());
                try {
                    //  mMediaPlayer.setVideoScalingMode(1);
                    mMediaPlayer.setDataSource(sVideoPath);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.setVolume(0f, 0f);
                    if (Build.VERSION.SDK_INT >= 16) {
                        mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    }
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
        class VideoVoiceControlReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                int action = intent.getIntExtra(Constant.ACTION, -1);
                switch (action) {
                    case Constant.ACTION_VOICE_NORMAL:
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                        break;
                    case Constant.ACTION_VOICE_SILENCE:
                        mMediaPlayer.setVolume(0, 0);
                        break;
                }
            }
        }
    }


}
