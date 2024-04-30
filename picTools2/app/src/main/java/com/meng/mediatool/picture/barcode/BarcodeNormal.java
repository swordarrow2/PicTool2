package com.meng.mediatool.picture.barcode;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.zxing.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.tools.MaterialDesign.*;
import com.meng.mediatool.tools.mengViews.*;
import java.io.*;

public class BarcodeNormal extends BaseFragment {
    private ScrollView scrollView;
    private ImageView qrcodeImageView,tipImageView;
    private MDEditText mengEtContent;
    private MDEditText mengEtSize;
    private TextView tvImgPath;
    private Button btnSave;
    private Bitmap bmpQRcode = null;
    public Bitmap logoImage = null;
    private CheckBox cbAutoColor;
    private CheckBox cbCrop;
    private MengColorBar mColorBar;
    private LinearLayout forQr;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.barcode_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorBar = (MengColorBar) view.findViewById(R.id.gif_arb_qr_main_colorBar);
        qrcodeImageView = (ImageView) view.findViewById(R.id.qr_imageview);
        mengEtContent = (MDEditText) view.findViewById(R.id.qr_mengEditText_content);
        mengEtSize = (MDEditText) view.findViewById(R.id.qr_mengEditText_size);
        scrollView = (ScrollView) view.findViewById(R.id.qr_mainScrollView);
        cbAutoColor = (CheckBox) view.findViewById(R.id.qr_main_autoColor);
        cbCrop = (CheckBox) view.findViewById(R.id.qr_main_crop);
        btnSave = (Button) view.findViewById(R.id.qr_ButtonSave);
        tvImgPath = (TextView) view.findViewById(R.id.qr_main_imgPathTextView);
        tipImageView = (ImageView)view.findViewById(R.id.barcode_mainImageView_tip);
        spinner = (Spinner) view.findViewById(R.id.qr_main_spinner);
        forQr = (LinearLayout)view.findViewById(R.id.barcode_mainLinearLayout_for_qr);
        tipImageView.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1) {
                    MainActivity.instance.showToast(getResources().getString(R.string.format_tip));
                }
            });
        ((Button) view.findViewById(R.id.qr_ButtonSelectImage)).setOnClickListener(click);
        ((Button) view.findViewById(R.id.qr_ButtonRemoveImage)).setOnClickListener(click);
        ((Button) view.findViewById(R.id.qr_ButtonCreate)).setOnClickListener(click);
		btnSave.setOnClickListener(click);
        cbAutoColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mColorBar.setVisibility(isChecked ? View.GONE : View.VISIBLE);
					if (!isChecked) MainActivity.instance.showToast("如果颜色搭配不合理,二维码将会难以识别");
				}
			});

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					String barcodeFormat = ((TextView) view).getText().toString();
					if (btnSave.getVisibility() == View.VISIBLE) {
						createBarcode(barcodeFormat);
					}
                    forQr.setVisibility(barcodeFormat.equals("QRcode") ?View.VISIBLE: View.GONE);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
    }

    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.qr_ButtonSelectImage:
                    selectImage();
                    break;
                case R.id.qr_ButtonRemoveImage:
                    logoImage = null;
                    tvImgPath.setText("未选择图片，将会生成普通二维码");
                    break;
                case R.id.qr_ButtonCreate:
                    createBarcode(spinner.getSelectedItem().toString());
                    btnSave.setVisibility(View.VISIBLE);
                    break;
                case R.id.qr_ButtonSave:
                    String s = FileTool.saveToFile(bmpQRcode, FileType.barcode);
                    if (s == null) {
                        MainActivity.instance.showToast("保存出错");
                        break;
                    }
                    MainActivity.instance.showToast("已保存至" + s);
                    getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));//更新图库
                    break;
            }
        }
    };

    private void createBarcode(String barcodeFormat) {
        bmpQRcode = QrUtils.flex(
			QrUtils.createBarcode(
				mengEtContent.getString(),
				switchFormat(barcodeFormat),
				cbAutoColor.isChecked() ? Color.BLACK : mColorBar.getTrueColor(),
				cbAutoColor.isChecked() ? Color.WHITE : mColorBar.getFalseColor(),
				500,
				logoImage),
			mengEtSize.getInt());
        scrollView.post(new Runnable() {
				@Override
				public void run() {
					scrollView.fullScroll(View.FOCUS_DOWN);
				}
			});
        qrcodeImageView.setImageBitmap(bmpQRcode);
    }

    private BarcodeFormat switchFormat(String s) {
        switch (s) {
            case "QRcode":
                return BarcodeFormat.QR_CODE;
            case "AZTEC":
                return BarcodeFormat.AZTEC;
            case "DataMatrix":
                return BarcodeFormat.DATA_MATRIX;
            case "PDF417":
                return BarcodeFormat.PDF_417;
        }
        return BarcodeFormat.QR_CODE;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == StaticVars.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
				String path = Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(), data.getData());
				tvImgPath.setText(String.format("当前图片：%s", path));
				if (cbCrop.isChecked()) {
					Intent in=new Intent(getActivity(), CropActivity.class);
					in.putExtra("path", path);
					startActivityForResult(in, 9961);
				} else {
					logoImage = BitmapFactory.decodeFile(path);
				}
			} else if (requestCode == 9961) {
				byte[] bis = data.getByteArrayExtra("bitmap");
				if (bis != null) {
					logoImage = BitmapFactory.decodeByteArray(bis, 0, bis.length);
				}
			} 
		} else if (resultCode == Activity.RESULT_CANCELED) {
			MainActivity.instance.showToast("取消选择图片");
		}
        super.onActivityResult(requestCode, resultCode, data);
    }

}
