package com.meng.mediatool.picture.barcode;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import com.meng.mediatool.*;
import java.io.*;

public class CropActivity extends Activity {
    private ClipSquareImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_main);
        Bitmap bmp = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));
        imageView = (ClipSquareImageView) findViewById(R.id.clipSquareIV);
        imageView.setImageBitmap(bmp);
		new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {}
					runOnUiThread(new Runnable(){

							@Override
							public void run() {
								imageView.setBorderWeight(1, 1);
							}
						});
				}
			}).start();
        findViewById(R.id.doneBtn).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Bitmap bmp=imageView.clip();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					byte[] bs=baos.toByteArray();
					Intent intent = new Intent();
					intent.putExtra("bitmap", bs);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
    }
}

