package com.meng.toolset.picture.barcode;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.meng.app.*;
import com.meng.customview.*;
import com.meng.tools.*;
import com.meng.tools.MaterialDesign.*;
import com.meng.toolset.mediatool.*;

import java.io.*;

public class BarcodeAwesomeGif extends BaseFragment {

    private boolean coding = false;
    private Button btnSelectImage;
    private CheckBox cbAutoColor;
    private MDEditText mengEtDotScale;
    private MDEditText mengEtTextToEncode;
    private CheckBox cbAutoSize;
    private MDEditText mengEtSize;
    private ProgressBar pbCodingProgress;
    private String strSelectedGifPath = "";
    private TextView tvImagePath;
    private MengColorBar mColorBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gif_qr_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorBar = (MengColorBar) view.findViewById(R.id.gif_arb_qr_main_colorBar);
        Button btnEncodeGif = (Button) view.findViewById(R.id.gif_arb_qr_button_encode_gif);
        btnSelectImage = (Button) view.findViewById(R.id.gif_arb_qr_button_selectImg);
        cbAutoColor = (CheckBox) view.findViewById(R.id.gif_arb_qr_checkbox_autocolor);
        mengEtDotScale = (MDEditText) view.findViewById(R.id.gif_arb_qr_mengEdittext_dotScale);
        mengEtTextToEncode = (MDEditText) view.findViewById(R.id.gif_arb_qr_mainmengTextview_content);
        mengEtSize = (MDEditText) view.findViewById(R.id.gif_qr_mainEditText_size);
        cbAutoSize = (CheckBox) view.findViewById(R.id.gif_qr_mainCheckbox_size);
        pbCodingProgress = (ProgressBar) view.findViewById(R.id.gif_arb_qr_mainProgressBar);
        tvImagePath = (TextView) view.findViewById(R.id.gif_arb_qr_selected_path);
        cbAutoSize.setOnCheckedChangeListener(check);
        cbAutoColor.setOnCheckedChangeListener(check);
        btnSelectImage.setOnClickListener(listenerBtnClick);
        btnEncodeGif.setOnClickListener(listenerBtnClick);
    }

    CompoundButton.OnCheckedChangeListener check = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.gif_qr_mainCheckbox_size:
                    mengEtSize.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                    break;
                case R.id.gif_arb_qr_checkbox_autocolor:
                    mColorBar.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                    if (!isChecked) {
                        MainActivity.instance.showToast("如果颜色搭配不合理,二维码将会难以识别");
                    }
                    break;
            }
        }
    };
    OnClickListener listenerBtnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gif_arb_qr_button_selectImg:
                    selectImage();
                    break;
                case R.id.gif_arb_qr_button_encode_gif:
                    if (coding) {
                        MainActivity.instance.showToast("正在执行操作");
                    } else {
                        btnSelectImage.setEnabled(false);
                        encodeGIF(strSelectedGifPath);
                    }
                    break;
            }
        }
    };

    private void encodeGIF(final String oldGifPath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    coding = true;
                    AnimatedGifDecoder gifDecoder = new AnimatedGifDecoder();
                    File gifFile = new File(oldGifPath);
                    FileInputStream fis = new FileInputStream(gifFile);
                    int statusCode = 0;
                    statusCode = gifDecoder.read(fis, fis.available());
                    if (statusCode != 0) {
                        MainActivity.instance.showToast("读取出错:" + oldGifPath);
                        return;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
                    localAnimatedGifEncoder.start(baos);//start
                    localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
                    for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                        float pro = ((float) gifDecoder.getCurrentFrameIndex()) / gifDecoder.getFrameCount() * 100;
                        setProgress((int) pro);
                        gifDecoder.advance();
                        localAnimatedGifEncoder.setDelay(gifDecoder.getNextDelay());
                        localAnimatedGifEncoder.addFrame(encodeAwesome(gifDecoder.getNextFrame()));
                    }
                    localAnimatedGifEncoder.finish();
                    File outputFile = FileTool.getAppFile(FunctionSavePath.awesomeQR, FileTool.FileType.gif_89a);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    baos.writeTo(fos);
                    baos.flush();
                    fos.flush();
                    baos.close();
                    fos.close();
                    getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
                    MainActivity.instance.showToast("完成 : " + outputFile.getAbsolutePath());
                } catch (Exception e) {
                    MainActivity.instance.showToast(e.toString());
                }
                coding = false;
                System.gc();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSelectImage.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private Bitmap encodeAwesome(Bitmap bg) {
        int size = bg.getWidth();
        return AwesomeQRCode.create(
                mengEtTextToEncode.getString(),
                cbAutoSize.isChecked() ? size : mengEtSize.getInt(),
                (int) (size * 0.025f),
                Float.parseFloat(mengEtDotScale.getString()),
                mColorBar.getTrueColor(),
                cbAutoColor.isChecked() ? Color.WHITE : mColorBar.getFalseColor(),
                bg,
                false,
                cbAutoColor.isChecked(),
                false,
                128);
    }

    private void setProgress(final int p) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (p == 100) {
                    pbCodingProgress.setVisibility(View.GONE);
                    MainActivity.instance.showToast("完成");
                } else {
                    pbCodingProgress.setProgress(p);
                    if (pbCodingProgress.getVisibility() == View.GONE) {
                        pbCodingProgress.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.SELECT_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data.getData() != null) {
            try {
                if (coding) {
                    MainActivity.instance.showToast("正在执行操作");
                } else {
                    strSelectedGifPath = AndroidContent.absolutePathFromUri(getActivity().getApplicationContext(), data.getData());
                    tvImagePath.setText(strSelectedGifPath);
                }
            } catch (Exception e) {
                MainActivity.instance.showToast(e.toString());
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("用户取消了操作");
        } else {
            selectImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
