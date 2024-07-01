package com.meng.toolset.audio;

import android.content.*;
import android.os.*;
import android.speech.tts.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.meng.app.BaseFragment;
import com.meng.app.Constant;
import com.meng.app.MFragmentManager;
import com.meng.app.MainActivity;
import com.meng.app.Welcome;
import com.meng.toolset.mediatool.*;
import com.meng.tools.MaterialDesign.*;
import com.meng.customview.MengSeekBar;

import java.text.*;
import java.util.*;

public class TtsFragment extends BaseFragment implements OnClickListener, TextToSpeech.OnInitListener {

    /*
     *@author 清梦
     *@date 2024-04-20 00:54:47
     */
    public static final String TAG = "VitsConnectFragment";

    private Button btnStart;
    private MengSeekBar msbPitch;
    private MengSeekBar msbSpeechRate;
    private MDEditText etContent;
    private TextToSpeech mTts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.android_tts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart = (Button) view.findViewById(R.id.android_tts_Button);
        etContent = (MDEditText) view.findViewById(R.id.android_tts_MDEditText);
        msbPitch = (MengSeekBar) view.findViewById(R.id.android_tts_MengSeekBar_1);
        msbSpeechRate = (MengSeekBar) view.findViewById(R.id.android_tts_MengSeekBar_2);
        msbPitch.setText("setPitch");
        msbSpeechRate.setText("setSpeechRate");
        msbPitch.setMax(200);
        msbPitch.setProgress(100);
        msbSpeechRate.setMax(200);
        msbSpeechRate.setProgress(100);
        btnStart.setOnClickListener(this);
        etContent.setText("这是一个TTS测试文本");
        msbPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                msbPitch.setText(MessageFormat.format("pitch:{0}", progress));
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        msbSpeechRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                msbPitch.setText(MessageFormat.format("rate:{0}", progress));
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        try {
            startActivityForResult(checkIntent, Constant.CHECK_TTS_REQUEST_CODE);
        } catch (ActivityNotFoundException ignore) {
        }
    }

    @Override
    public void onClick(View p1) {
        mTts.setPitch(0.01f * msbPitch.getProgress());
        mTts.setSpeechRate(0.01f * msbSpeechRate.getProgress());
        mTts.speak(etContent.getString(), TextToSpeech.QUEUE_ADD, null);
        MainActivity.instance.showToast(mTts.getVoices().toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.CHECK_TTS_REQUEST_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mTts = new TextToSpeech(getActivity(), this);
            } else if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL) {
                MainActivity.instance.showToast("Need language stuff:" + resultCode);
                Intent dataIntent = new Intent();
                dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                try {
                    startActivity(dataIntent);
                } catch (ActivityNotFoundException e) {
                    MainActivity.instance.showToast(getString(R.string.no_support_tts));
                    MFragmentManager.getInstance().showFragment(Welcome.class);
                }
            }
        } else {
//其他Intent返回的结果
        }
    }

    //实现TTS初始化接口
    @Override
    public void onInit(int status) {
// TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.CHINA);
//设置发音语言
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available");
                btnStart.setEnabled(false);
            } else {
                mTts.speak("This is an example of speech synthesis.", TextToSpeech.QUEUE_ADD, null);
                btnStart.setEnabled(true);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTts != null) {
            mTts.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTts.shutdown();
    }

}
