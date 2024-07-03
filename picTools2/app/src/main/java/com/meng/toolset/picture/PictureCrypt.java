package com.meng.toolset.picture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.meng.app.BaseFragment;
import com.meng.app.Constant;
import com.meng.app.FunctionSavePath;
import com.meng.app.MainActivity;
import com.meng.tools.AndroidContent;
import com.meng.tools.FileTool;
import com.meng.tools.QrUtils;
import com.meng.tools.app.ThreadPool;
import com.meng.toolset.mediatool.R;

import java.io.File;
import java.io.IOException;

public class PictureCrypt extends BaseFragment implements View.OnClickListener {
    private final int REQUEST_PERMISSION_PHOTO = 1001;
    private ImageView imageView;
    private Bitmap cryptBitmap;
    private Button btnSave;
    private RadioButton rbEn;
    private RadioButton rbDe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.picture_crypt, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.picture_cryptImageView);
        btnSave = (Button) view.findViewById(R.id.picture_cryptButton_save);
        rbEn = (RadioButton) view.findViewById(R.id.picture_cryptRadioGroup_encry);
        rbDe = (RadioButton) view.findViewById(R.id.picture_cryptRadioGroup_decry);
        view.findViewById(R.id.picture_cryptButton_read_gallery).setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View p1) {
        if (p1.getId() == R.id.picture_cryptButton_read_gallery) {
            openGallery();
        } else if (p1.getId() == R.id.picture_cryptButton_save) {
            try {
                String s = FileTool.saveToFile(FileTool.getAppFile(FunctionSavePath.bus, FileTool.FileType.png), cryptBitmap);
                getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));
            } catch (IOException e) {}           
        }
    }

    private void createBitmap(final String path) {
        ThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    if (rbEn.isChecked()) {
                        MainActivity.instance.showToast("开始加密");
                        cryptBitmap = QrUtils.encryBitmap(BitmapFactory.decodeFile(path));
                    } else if (rbDe.isChecked()) {
                        MainActivity.instance.showToast("开始解密");
                        cryptBitmap = QrUtils.decryBitmap(BitmapFactory.decodeFile(path));
                    }
                    MainActivity.instance.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                imageView.setImageBitmap(cryptBitmap);
                                btnSave.setVisibility(View.VISIBLE);
                            }
                        });
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == Constant.SELECT_FILE_REQUEST_CODE) {
            Uri inputUri = data.getData();
            String path = AndroidContent.absolutePathFromUri(getActivity(), inputUri);
            if (!TextUtils.isEmpty(path)) {
                createBitmap(path);
            } else {
                MainActivity.instance.showToast("图片路径未找到");
            }
        }
    }

    public void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_PHOTO);
        } else {
            selectImage();
        }
    }
}
