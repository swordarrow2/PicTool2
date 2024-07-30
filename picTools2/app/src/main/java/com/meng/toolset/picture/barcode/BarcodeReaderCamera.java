package com.meng.toolset.picture.barcode;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.*;
import android.widget.*;

import com.google.zxing.*;
import com.meng.app.*;
import com.meng.tools.*;
import com.meng.tools.app.*;
import com.meng.tools.zxing.camera.*;
import com.meng.tools.zxing.decoding.*;
import com.meng.tools.zxing.view.*;
import com.meng.toolset.mediatool.R;

import java.util.*;
import java.util.concurrent.*;

public class BarcodeReaderCamera extends BaseFragment implements Callback {

    private final int REQUEST_PERMISSION_CAMERA = 1000;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private boolean flashLightOpen = false;
    private ImageButton flashIbtn;
    private BiConsumer<String, String> onResultAction = new BiConsumer<String, String>() {
        @Override
        public void action(String v1, String v2) {
            MainActivity.instance.showToast(String.format("二维码类型%s,内容:%s,已复制到剪贴板", v1, v2));
            AndroidContent.copyToClipboard(v2);
            ThreadPool.executeAfterTime(new Runnable() {
                @Override
                public void run() {
                    restartPreview();
                }
            }, 3, TimeUnit.SECONDS);
        }
    };

    public void setOnResultAction(BiConsumer<String, String> onResultAction) {
        this.onResultAction = onResultAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
        CameraManager.init(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }
        viewfinderView = (ViewfinderView) view.findViewById(R.id.viewfinder_view);
        flashIbtn = (ImageButton) view.findViewById(R.id.flash_ibtn);
        flashIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashIbtn.setImageResource(flashLightOpen ? R.drawable.ic_flash_off_white_24dp : R.drawable.ic_flash_on_white_24dp);
                toggleFlashLight();
            }
        });
    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        SystemTools.doVibrate(getActivity(), 200);
        handleResult(result.getText(), result.getBarcodeFormat().toString());
    }

    public void handleResult(String result, String format) {
        if (TextUtils.isEmpty(result)) {
            restartPreview();
        } else {
            onResultAction.action(result, format);
        }
    }

    protected void setViewfinderView(ViewfinderView view) {
        viewfinderView = view;
    }

    public void toggleFlashLight() {
        setFlashLightOpen(!flashLightOpen);
    }

    public void setFlashLightOpen(boolean open) {
        if (flashLightOpen == open) return;
        flashLightOpen = !flashLightOpen;
        CameraManager.get().setFlashLight(open);
    }

    public boolean isFlashLightOpen() {
        return flashLightOpen;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    protected void restartPreview() {
        // 当界面跳转时 handler 可能为null
        if (handler != null) {
            Message restartMessage = Message.obtain();
            restartMessage.what = R.id.restart_preview;
            handler.handleMessage(restartMessage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(getClass().toString(), "camera open");
        //LogTool.t("camera open");
        View sv = this.getView();
        if (sv == null) {
            return;
        }
        SurfaceView surfaceView = (SurfaceView) sv.findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(getClass().toString(), "camera close");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (flashIbtn != null) {
            flashIbtn.setImageResource(R.drawable.ic_flash_off_white_24dp);
        }
        CameraManager cm = CameraManager.get();
        if (cm != null) {
            cm.closeDriver();
        }
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 未获得Camera权限
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("请在系统设置中为App开启摄像头权限后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                            }
                        }).show();
            }
        }
    }
}
