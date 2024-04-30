package com.meng.ai;

import android.content.*;
import android.os.*;
import android.speech.tts.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.MaterialDesign.*;
import com.meng.mediatool.tools.mengViews.*;
import java.text.*;
import java.util.*;

public class TtsFragment extends BaseFragment implements OnClickListener ,TextToSpeech.OnInitListener {

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
                    seekBar.setProgress(progress)                    ;
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
                    seekBar.setProgress(progress)                    ;
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
        startActivityForResult(checkIntent, StaticVars.CHECK_TTS_REQUEST_CODE);       
    }



    @Override
    public void onClick(View p1) {
        mTts.setPitch(0.01f * msbPitch.getProgress());
        mTts.setSpeechRate(0.01f * msbSpeechRate.getProgress());
        mTts.speak(etContent.getString(), TextToSpeech.QUEUE_ADD, null);
        MainActivity.instance.showToast(mTts.getVoices().toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticVars.CHECK_TTS_REQUEST_CODE) {
            switch (resultCode) {
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
//TTS可用                    
                    mTts = new TextToSpeech(getActivity(), this);

                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
//需要的语音数据已损坏
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
//缺少需要语言的语音数据
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
//缺少需要语言的发音数据
                    {
//这三种情况都表明数据有错,重新下载安装需要的数据
                        MainActivity.instance.showToast("Need language stuff:" + resultCode);
                        Intent dataIntent = new Intent();
                        dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(dataIntent);
                    }
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
//检查失败
                default:
                    MainActivity.instance.showToast("Got a failure. TTS apparently not available");
                    break;
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
