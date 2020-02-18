package com.meng.pt2.qrCode.creator;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.meng.pt2.*;
import java.io.*;

public class CropActivity extends Activity implements View.OnClickListener {
    private ClipSquareImageView imageView;

    private LinearLayout[] btns = new LinearLayout[5];
    private View[] v = new View[5];
    private TextView[] t = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_main);

        Bitmap bmp = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));
        imageView = (ClipSquareImageView) findViewById(R.id.clipSquareIV);
        imageView.setImageBitmap(bmp);

        btns[0] = (LinearLayout) findViewById(R.id.btn1);
        btns[1] = (LinearLayout) findViewById(R.id.btn2);
        btns[2] = (LinearLayout) findViewById(R.id.btn3);
        btns[3] = (LinearLayout) findViewById(R.id.btn4);
        btns[4] = (LinearLayout) findViewById(R.id.btn5);

        v[0] = findViewById(R.id.v1);
        v[1] = findViewById(R.id.v2);
        v[2] = findViewById(R.id.v3);
        v[3] = findViewById(R.id.v4);
        v[4] = findViewById(R.id.v5);

        t[0] = (TextView) findViewById(R.id.t1);
        t[1] = (TextView) findViewById(R.id.t2);
        t[2] = (TextView) findViewById(R.id.t3);
        t[3] = (TextView) findViewById(R.id.t4);
        t[4] = (TextView) findViewById(R.id.t5);
        for (LinearLayout btn: btns) {
            btn.setOnClickListener(this);
        }
        findViewById(R.id.doneBtn).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// 此处获取剪裁后的bitmap
					// 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					imageView.clip().compress(Bitmap.CompressFormat.JPEG, 100, baos);
					Intent intent = new Intent();
					intent.putExtra("bitmap", baos.toByteArray());
					setResult(RESULT_OK, intent);
					finish();
				}
			});
    }

    private int selectedIndex = 2;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                clickItem(0);
                break;
            case R.id.btn2:
                clickItem(1);
                break;
            case R.id.btn3:
                clickItem(2);
                break;
            case R.id.btn4:
                clickItem(3);
                break;
            case R.id.btn5:
                clickItem(4);
                break;
        }
    }

    private void clickItem(int index) {
        if (selectedIndex == index) return;
        selectedItem(selectedIndex, false);
        selectedIndex = index;
        selectedItem(selectedIndex, true);
    }

    private void selectedItem(int index, boolean selected) {
        if (selected) {
            v[index].setBackgroundResource(R.drawable.rect_red_border);
            t[index].setTextColor(Color.RED);
        } else {
            v[index].setBackgroundResource(R.drawable.rect_border);
            t[index].setTextColor(Color.BLACK);
            return;
        }
        int widthWeight = 1;
        int heightWeight = 1;
        switch (index) {
            case 0:
                widthWeight = 9;
                heightWeight = 16;
                break;
            case 1:
                widthWeight = 3;
                heightWeight = 4;
                break;
            case 3:
                widthWeight = 4;
                heightWeight = 3;
                break;
            case 4:
                widthWeight = 16;
                heightWeight = 9;
                break;
        }
        imageView.setBorderWeight(widthWeight, heightWeight);
    }
}

