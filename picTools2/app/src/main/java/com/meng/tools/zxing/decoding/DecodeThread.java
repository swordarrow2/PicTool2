/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meng.tools.zxing.decoding;

import android.os.*;
import com.google.zxing.*;
import com.meng.toolset.picture.barcode.*;
import java.util.*;
import java.util.concurrent.*;


final class DecodeThread extends Thread {

    public static final String BARCODE_BITMAP="barcode_bitmap";
    private final BarcodeReaderCamera activity;
    private final ConcurrentHashMap<DecodeHintType,Object> hints;
    private final CountDownLatch handlerInitLatch;
    private Handler handler;

    DecodeThread(BarcodeReaderCamera activity, Vector<BarcodeFormat> decodeFormats, String characterSet, ResultPointCallback resultPointCallback) {
        this.activity = activity;
        handlerInitLatch = new CountDownLatch(1);
        hints = new ConcurrentHashMap<DecodeHintType,Object>();
		/*
		 if(decodeFormats==null||decodeFormats.isEmpty()){
		 decodeFormats=new Vector<BarcodeFormat>();
		 decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
		 decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
		 decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		 decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
		 }
		 hints.put(DecodeHintType.POSSIBLE_FORMATS,decodeFormats);
		 */
        if (characterSet != null) {
            hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }
        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
    }

    Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new DecodeHandler(activity, hints);
        handlerInitLatch.countDown();
        Looper.loop();
    }
}
