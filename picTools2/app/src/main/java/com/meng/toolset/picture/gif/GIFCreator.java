package com.meng.toolset.picture.gif;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.meng.app.BaseFragment;
import com.meng.app.FunctionSavePath;
import com.meng.app.MainActivity;
import com.meng.tools.AnimatedGifEncoder;
import com.meng.tools.FileTool;
import com.meng.tools.MaterialDesign.FloatingButton;
import com.meng.tools.MaterialDesign.MDEditText;
import com.meng.tools.app.ThreadPool;
import com.meng.toolset.mediatool.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GIFCreator extends BaseFragment {

    public MDEditText mengEtFrameDelay;
    public ArrayList<GIFFrame> selectedImages = new ArrayList<>();
    public EditFrameAdapter editFrameAdapter;
    public FloatingButton fabAdd;
    public FloatingButton fabEncode;

    private int mPreviousVisibleItem;
    private boolean encoding = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gif_creator, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mengEtFrameDelay = (MDEditText) view.findViewById(R.id.gif_creator_delay);
        fabAdd = (FloatingButton) view.findViewById(R.id.fab_add);
        fabEncode = (FloatingButton) view.findViewById(R.id.fab_encode);
        ListView listView = (ListView) view.findViewById(R.id.gif_creator_list);
        fabAdd.setOnClickListener(listenerBtnClick);
        fabEncode.setOnClickListener(listenerBtnClick);
        editFrameAdapter = new EditFrameAdapter(getActivity(), selectedImages, true);
        listView.setAdapter(editFrameAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View v, int position, long id) {
                final EditText editTextName = new EditText(getActivity());
                final GIFFrame personInfo = (GIFFrame) parent.getItemAtPosition(position);
                editTextName.setText(String.valueOf(personInfo.delay));
                new AlertDialog.Builder(getActivity())
                        .setView(editTextName)
                        .setTitle("设置帧延时(ms)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                personInfo.delay = Integer.parseInt(editTextName.getText().toString());
                                editFrameAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long id) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("确定删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                selectedImages.remove(position);
                                editFrameAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            }
        });
        /*   listView.setOnScrollListener(new AbsListView.OnScrollListener() {
		 @Override
		 public void onScrollStateChanged(AbsListView view, int scrollState) {
		 }

		 @Override
		 public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		 if (firstVisibleItem > mPreviousVisibleItem) {
		 fabAdd.hide(true);
		 fabEncode.hide(true);
		 } else if (firstVisibleItem < mPreviousVisibleItem) {
		 fabAdd.show(true);
		 fabEncode.show(true);
		 }
		 mPreviousVisibleItem = firstVisibleItem;
		 }
		 });*/
        fabAdd.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabAdd.show(true);
            }
        }, 300);

        fabEncode.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEncode.show(true);
            }
        }, 600);
    }

    View.OnClickListener listenerBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_add:
                    Intent intent = new Intent(getActivity(), SelectFileActivity.class);
                    startActivityForResult(intent, 9961);
                    break;
                case R.id.fab_encode:
                    if (encoding) return;
                    encoding = true;
                    MainActivity.instance.showToast("开始生成gif");
                    ThreadPool.execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                fabEncode.setMax(selectedImages.size());
                                File outputFile = FileTool.getAppFile(FunctionSavePath.awesomeQR, FileTool.FileType.gif_89a);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fabEncode.setShowProgressBackground(true);
                                        fabEncode.setIndeterminate(false);
                                    }
                                });
                                AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
                                localAnimatedGifEncoder.start(baos);//start
                                localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
                                int currentFile = 1;
                                for (GIFFrame gifFrame : selectedImages) {
                                    localAnimatedGifEncoder.setDelay(gifFrame.delay);
                                    localAnimatedGifEncoder.addFrame(BitmapFactory.decodeFile(gifFrame.filePath));
                                    setProgress(currentFile);
                                    ++currentFile;
                                }
                                localAnimatedGifEncoder.finish();
                                FileOutputStream fos = new FileOutputStream(outputFile);
                                baos.writeTo(fos);
                                baos.flush();
                                fos.flush();
                                baos.close();
                                fos.close();
                                getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
                                MainActivity.instance.showToast("完成 : " + outputFile.getAbsolutePath());
                            } catch (Exception e) {
                                MainActivity.instance.showToast(e.toString());
                            }
                            encoding = false;
                        }
                    });
                    break;
            }
        }
    };

    private void setProgress(final int progress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fabEncode.getMax() == progress) {
                    fabEncode.hideProgress();
                } else {
                    fabEncode.setProgress(progress, true);
                }
                //    if (progress == 100) {
                //    fabEncode.hideProgress();
                //	fabEncode.setIndeterminate(false);
                //      } else {
                //	  fabEncode.setIndeterminate(false);
                //           }
            }
        });
    }

    @Override
    public void onResume() {
        editFrameAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9961 && resultCode == Activity.RESULT_OK) {
            MainActivity.instance.showToast("add frame ok");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
