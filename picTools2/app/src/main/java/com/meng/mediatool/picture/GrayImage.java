package com.meng.mediatool.picture;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import java.io.*;
import com.meng.mediatool.task.*;

public class GrayImage extends BaseFragment implements View.OnClickListener {

	private ImageView imageView;
    private Button btnSelect;
    private Button btnStart;

    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bin_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		imageView = (ImageView)view.findViewById(R.id.bin_mainImageView);
        btnStart = (Button) view.findViewById(R.id.binmainButton_start);
        btnSelect = (Button) view.findViewById(R.id.binmainButton_select);
		btnSelect.setOnClickListener(this);
        btnStart.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.binmainButton_select:
                selectImage();                    
                btnStart.setEnabled(true);
                break;
            case R.id.binmainButton_start:
                try {
                    generate(path);
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
                path = Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData());
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            }
		} else if (resultCode == Activity.RESULT_CANCELED) {
			MainActivity.instance.showToast("取消选择图片");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generate(final String path) throws IOException {
        MainActivity.instance.showToast("开始转换");        
        final FileFormat.FileType type = FileFormat.getFileType(new File(path)).describe;
        if (type == FileFormat.FileType.gif_87a || type == FileFormat.FileType.gif_89a) {

            new BackgroundTask(){

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
                        int frameCount = gifDecoder.getFrameCount();
                        setMaxProgress(frameCount);
                        for (int i = 0; i < frameCount; i++) {
                            gifDecoder.advance();
                            localAnimatedGifEncoder.setDelay(gifDecoder.getNextDelay());
                            localAnimatedGifEncoder.addFrame(encode(gifDecoder.getNextFrame()));
                            setProgress(i);
                        }
                        localAnimatedGifEncoder.finish();
                        String filePath = FileTool.getSaveFileAbsPath(FileType.gray8picture, FileFormat.FileType.gif_89a);
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
            }.setTitle("转换灰度图").setStatus("正在进行").start();
        } else {
            BitmapFactory.Options bo = new BitmapFactory.Options();
            bo.inMutable = true;
            final Bitmap grayBitmap = encode(BitmapFactory.decodeFile(path, bo));
            String s = FileTool.saveToFile(grayBitmap, FileType.gray8picture);
            MainActivity.instance.showToast("已保存至" + s);
            getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));
            MainActivity.instance.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        imageView.setImageBitmap(grayBitmap);
                    }
                });
        }  
    }

	private Bitmap encode(Bitmap bmp) {
		for (int i = 0;i < bmp.getWidth();i++) {
			for (int j =0;j < bmp.getHeight();j++) {
				int col = bmp.getPixel(i, j);
				int alpha = col & 0xFF000000;
				int R = (col & 0x00FF0000) >> 16;
				int G = (col & 0x0000FF00) >> 8;
				int B = (col & 0x000000FF);

				//	int Y = (int)(R * 0.299 + G * 0.587 + B * 0.114);
				int Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
				int newColor = alpha | (Y << 16) | (Y << 8) | Y;
				bmp.setPixel(i, j, newColor);
			}
		}
		return bmp;
	}

	private Bitmap encode2(Bitmap frame) {
		Bitmap bitmapNew = frame;//.copy(Bitmap.Config.ARGB_8888, true);
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
