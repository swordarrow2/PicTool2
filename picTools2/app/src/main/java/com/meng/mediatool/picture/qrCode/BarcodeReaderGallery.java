package com.meng.mediatool.picture.qrCode;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.google.zxing.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;

public class BarcodeReaderGallery extends Fragment implements View.OnClickListener {
    private Button btnCreateAwesomeQR;
    private TextView tvResult;
    private TextView tvFormat;

	public void setResult(String resultString, String format) {
		handleResult(resultString, format);
	}

	public String getLastResult() {
		return tvResult.getText().toString();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.read_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnOpenGallery = (Button) view.findViewById(R.id.read_galleryButton);
        tvResult = (TextView) view.findViewById(R.id.read_galleryTextView_result);
        tvFormat = (TextView) view.findViewById(R.id.read_galleryTextView_format);
        btnCreateAwesomeQR = (Button) view.findViewById(R.id.read_galleryButton_createAwesomeQR);
		btnOpenGallery.setOnClickListener(this);
        btnCreateAwesomeQR.setOnClickListener(this);		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.read_galleryButton:
				MainActivity.instance.selectImage(BarcodeReaderGallery.this);
				break;
			case R.id.read_galleryButton_createAwesomeQR:
				MainActivity.instance.showFragment(BarcodeAwesome.class);
				MainActivity.instance.getFragment(BarcodeAwesome.class).setDataStr(tvResult.getText().toString());
				break;
		}
	}

    public void handleDecode(Result result, Bitmap barcode) {
        String resultString = result.getText();
        MainActivity.instance.doVibrate(200L);
        handleResult(resultString, result.getBarcodeFormat().toString());
    }

    protected void handleResult(String resultString, String format) {
        if (resultString.equals("")) {
		} else {
            tvFormat.setText(String.format("二维码类型%s", format));
            tvResult.setText(resultString);
            btnCreateAwesomeQR.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == MainActivity.instance.SELECT_FILE_REQUEST_CODE) {
            Uri inputUri = data.getData();
            String path = Tools.ContentHelper.absolutePathFromUri(getActivity(), inputUri);
            if (!TextUtils.isEmpty(path)) {
                Result result = QrUtils.decodeImage(path);
                if (result != null) {
                    handleDecode(result, null);
                } else {
                    MainActivity.instance.showToast("此图片无法识别");
                }
            } else {
                MainActivity.instance.showToast("图片路径未找到");
            }
        }
    }
}
