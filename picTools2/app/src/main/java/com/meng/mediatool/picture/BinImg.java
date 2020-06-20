package com.meng.mediatool.picture;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import java.io.*;

public class BinImg extends Fragment {

	private ImageView imageView;
	private Bitmap grayBitmap;
	private RadioButton rbStatic;
	private RadioButton rbDynamic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bin_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		imageView = (ImageView)view.findViewById(R.id.bin_mainImageView);
		rbStatic = (RadioButton) view.findViewById(R.id.bin_mainRadioButton_static);
		rbDynamic = (RadioButton) view.findViewById(R.id.bin_mainRadioButton_dynamic);
		view.findViewById(R.id.bin_mainButton_read_gallery).setOnClickListener(click);
	}

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bin_mainButton_read_gallery:
                    MainActivity.instance.selectImage(BinImg.this);
                    break;
			}
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MainActivity.instance.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
				String path = Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData());
				generate(path);
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			MainActivity.instance.showToast("取消选择图片");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generate(final String path) {
		if (rbStatic.isChecked()) {
			MainActivity.instance.showToast("开始转换");
			MainActivity.instance.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						BitmapFactory.Options bo=new BitmapFactory.Options();
						bo.inMutable = true;
						grayBitmap = encodeAwesome(BitmapFactory.decodeFile(path, bo));
						String s = FileHelper.saveBitmap(grayBitmap, FileType.binPng);
						MainActivity.instance.showToast("已保存至" + s);
						getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));
						MainActivity.instance.runOnUiThread(new Runnable(){

								@Override
								public void run() {
									imageView.setImageBitmap(grayBitmap);
								}
							});
					}
				});
		} else if (rbDynamic.isChecked()) {
			MainActivity.instance.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						try {
							AnimatedGifDecoder gifDecoder = new AnimatedGifDecoder();
							FileInputStream fis = new FileInputStream(path);
							int statusCode = gifDecoder.read(fis, fis.available());
							if (statusCode != 0) {
								MainActivity.instance.showToast("读取发生错误:" + path);
								return;
							}
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
							localAnimatedGifEncoder.start(baos);//start
							localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
							for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
								gifDecoder.advance();
								localAnimatedGifEncoder.setDelay(gifDecoder.getNextDelay());
								localAnimatedGifEncoder.addFrame(encode(gifDecoder.getNextFrame()));
							}
							localAnimatedGifEncoder.finish();
							String filePath = FileHelper.getFileAbsPath(FileType.binGif).replace(".png", ".gif");
							FileOutputStream fos = new FileOutputStream(filePath);
							baos.writeTo(fos);
							baos.flush();
							fos.flush();
							baos.close();
							fos.close();
							getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
							MainActivity.instance.showToast("已保存至" + filePath);
						} catch (Exception e) {
							MainActivity.instance.showToast(e.toString());
						}
					}
				});
		}
    }
	
	private Bitmap encode(Bitmap nextFrame) {
		for (int i = 0;i < nextFrame.getWidth();i++) {
			for (int j =0;j < nextFrame.getHeight();j++) {
				int col = nextFrame.getPixel(i, j);
				int alpha = col & 0xFF000000;
				int R = (col & 0x00FF0000) >> 16;
				int G = (col & 0x0000FF00) >> 8;
				int B = (col & 0x000000FF);

				//	int Y = (int)(R * 0.299 + G * 0.587 + B * 0.114);
				int Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
				int newColor = alpha | (Y << 16) | (Y << 8) | Y;
				nextFrame.setPixel(i, j, newColor);
			}
		}
		return nextFrame;
	}

	private Bitmap encodeAwesome(Bitmap nextFrame) {
		Bitmap bitmapNew = nextFrame;//.copy(Bitmap.Config.ARGB_8888, true);
		for (int i = 0;i < bitmapNew.getWidth();i++) {
			for (int j =0;j < bitmapNew.getHeight();j++) {
				int col = bitmapNew.getPixel(i, j);
				int alpha = col & 0xFF000000;
				int red = (col & 0x00FF0000) >> 16;
				int green = (col & 0x0000FF00) >> 8;
				int blue = (col & 0x000000FF);
				int gray = (int)((float)red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
				int newColor = alpha | (gray << 16) | (gray << 8) | gray;
				bitmapNew.setPixel(i, j, newColor);
			}
		}
		return bitmapNew;
	}
}
