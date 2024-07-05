package com.meng.customview;

import android.app.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.meng.toolset.mediatool.*;
import com.meng.toolset.picture.barcode.*;

public class MengBarcodeScanView extends FrameLayout {
    private Activity activity;
    private BarcodeReaderCamera barcodeReaderCamera;

    public MengBarcodeScanView(@NonNull Activity context) {
        super(context);
        init(context);
    }

    public MengBarcodeScanView(@NonNull Activity context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MengBarcodeScanView(@NonNull Activity context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnResultAction(BiConsumer<String, String> onResultAction) {
        barcodeReaderCamera = (BarcodeReaderCamera) activity.getFragmentManager().findFragmentByTag("mListFragment");
        barcodeReaderCamera.setOnResultAction(onResultAction);
    }

    private void init(@NonNull Activity context) {
        activity = context;
        View v = LayoutInflater.from(context).inflate(R.layout.meng_barcode_scan_view, this);
        barcodeReaderCamera = (BarcodeReaderCamera) activity.getFragmentManager().findFragmentByTag("mListFragment");
        if (barcodeReaderCamera == null) {
            barcodeReaderCamera = new BarcodeReaderCamera();
            activity.getFragmentManager().beginTransaction().add(R.id.barcode_scan_fragment_frame, barcodeReaderCamera, "mListFragment").commit();
        }
    }

    public void onDesdroy() {
        barcodeReaderCamera.onPause();
        barcodeReaderCamera.onDestroy();
    }
}
