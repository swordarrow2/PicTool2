package com.meng.mediatool.picture.barcode;

import android.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.tools.MaterialDesign.*;
import com.meng.mediatool.tools.mengViews.*;
import java.io.*;
import java.text.*;

import android.support.v7.app.AlertDialog;
import com.meng.mediatool.R;

public class BarcodeAwesomeArb extends BaseFragment implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener {

    private ImageView qrCodeImageView;
    private MDEditText mengEtDotScale, mengEtContents;
    private Button btGenerate;
    private CheckBox ckbAutoColor;
    private Button btnSave;
    private TextView imgPathTextView;
    private MengColorBar mColorBar;
    private MengScrollView sv;

    private MengSeekBar mengSeekBar;
    private Bitmap finallyBmp = null;
    private String selectedBmpPath = "";
    private int qrSize;
    private int selectedBmpWidth = 0;
    private int selectedBmpHeight = 0;
    private float screenW;
    private float screenH;
    private MengSelectRectView mengSelectView;
    private String[] PERMISSIONS_STORAGE = {
		Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.arb_awesome_qr, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sv = (MengScrollView) view.findViewById(R.id.awesomeqr_main_scrollView);
        mengSelectView = (MengSelectRectView) view.findViewById(R.id.arb_awesome_qrselectRectView);
        mColorBar = (MengColorBar) view.findViewById(R.id.gif_arb_qr_main_colorBar);
        qrCodeImageView = (ImageView) view.findViewById(R.id.awesomeqr_main_qrcode);
        mengEtContents = (MDEditText) view.findViewById(R.id.awesomeqr_main_content);
        mengEtDotScale = (MDEditText) view.findViewById(R.id.awesomeqr_main_dotScale);
        btGenerate = (Button) view.findViewById(R.id.awesomeqr_main_generate);
        ckbAutoColor = (CheckBox) view.findViewById(R.id.awesomeqr_main_autoColor);
        btnSave = (Button) view.findViewById(R.id.awesomeqr_mainButton_save);
        imgPathTextView = (TextView) view.findViewById(R.id.awesomeqr_main_imgPathTextView);
        mengSeekBar = (MengSeekBar) view.findViewById(R.id.awesomeqr_mainMengSeekBar);
        ((Button) view.findViewById(R.id.awesomeqr_main_backgroundImage)).setOnClickListener(this);
        btGenerate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        sv.setSelectView(mengSelectView);
        ckbAutoColor.setOnCheckedChangeListener(this);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.awesomeqr_main_backgroundImage:
                qrCodeImageView.setVisibility(View.GONE);
                btGenerate.setEnabled(true);
                btnSave.setVisibility(View.GONE);
                selectImage();
                break;
            case R.id.awesomeqr_main_generate:
                generate();
                mengSeekBar.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                qrCodeImageView.setVisibility(View.VISIBLE);
                mengSelectView.setVisibility(View.GONE);
                break;
            case R.id.awesomeqr_mainButton_save:
                String s = FileTool.saveToFile(finallyBmp, FileType.awesomeQR);
                if (s == null) {
                    MainActivity.instance.showToast("保存出错");
                    break;
                }
                MainActivity.instance.showToast("已保存至" + s);
                getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));//更新图库
                btnSave.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mColorBar.setVisibility(View.GONE);
        } else {
            mColorBar.setVisibility(View.VISIBLE);
            MainActivity.instance.showToast("如果颜色搭配不合理,二维码将会难以识别");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticVars.SELECT_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imgPathTextView.setVisibility(View.VISIBLE);
            Uri uri = data.getData();
            selectedBmpPath = Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), uri);
            imgPathTextView.setText(MessageFormat.format("当前文件：{0}", selectedBmpPath));
			final Bitmap selectedBmp = BitmapFactory.decodeFile(selectedBmpPath);
            selectedBmpWidth = selectedBmp.getWidth();
            selectedBmpHeight = selectedBmp.getHeight();
            final MengSeekBar msb = new MengSeekBar(getActivity());
            int maxProg = Math.min(selectedBmpWidth, selectedBmpHeight);
            msb.setMax(maxProg);
            msb.setProgress(maxProg / 3);
            new AlertDialog.Builder(getActivity())
				.setTitle("输入要添加的二维码大小(像素)")
				.setView(msb)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p1, int p2) {
						mengSeekBar.setVisibility(View.VISIBLE);
						mengSeekBar.setMax(msb.getMax());
						mengSeekBar.setProgress(msb.getProgress());
                        mengSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    progress = progress % 2 == 1 ? progress - 1 : progress;
                                    progress = progress < 1 ? 2 : progress;
                                    mengSeekBar.setText(MessageFormat.format("二维码大小:{0}像素", progress));
                                    seekBar.setProgress(progress);
                                    mengSelectView.setSize(qrSize = mengSeekBar.getProgress());
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });
						qrSize = msb.getProgress();
						mengSelectView.setup(selectedBmp, screenW, screenH, qrSize);
						ViewGroup.LayoutParams para = mengSelectView.getLayoutParams();
						para.height = (int) (screenW / selectedBmpWidth * selectedBmpHeight);
						mengSelectView.setLayoutParams(para);
						mengSelectView.setVisibility(View.VISIBLE);
						if (para.height > screenH * 2 / 3) MainActivity.instance.showToast("可使用音量键滚动界面");
						sv.post(new Runnable() {
                                public void run() {
                                    sv.fullScroll(View.FOCUS_DOWN);
                                }
                            });
					}
				}).show();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("用户取消了操作");
        } else {
            selectImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generate() {
        finallyBmp = QrUtils.generate(
			mengEtContents.getString(),
			Float.parseFloat(mengEtDotScale.getString()),
			ckbAutoColor.isChecked() ? Color.BLACK : mColorBar.getTrueColor(),
			ckbAutoColor.isChecked() ? Color.WHITE : mColorBar.getFalseColor(),
			ckbAutoColor.isChecked(),
			between(mengSelectView.getSelectLeft() / mengSelectView.getXishu(), 0, selectedBmpWidth - qrSize),
			between(mengSelectView.getSelectTop() / mengSelectView.getXishu(), 0, selectedBmpHeight - qrSize),
			qrSize,
			BitmapFactory.decodeFile(selectedBmpPath).copy(Bitmap.Config.ARGB_8888, true));
        qrCodeImageView.setImageBitmap(QrUtils.scaleBitmap(finallyBmp, mengSelectView.getXishu()));
    }

    private int between(float a, int min, int max) {
        if (a < min) a = min;
        if (a > max) a = max;
        return (int) a;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            sv.post(new Runnable() {
					public void run() {
						sv.scrollBy(0, 0xffffff9c);//(0xffffff9c)16=(-100)10
					}
				});
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            sv.post(new Runnable() {
					public void run() {
						sv.scrollBy(0, 100);
					}
				});
            return true;
        }
        return false;
    }
}
