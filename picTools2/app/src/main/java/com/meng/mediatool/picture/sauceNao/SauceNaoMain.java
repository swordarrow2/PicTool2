package com.meng.mediatool.picture.sauceNao;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.mediatool.*;
import com.meng.mediatool.picture.pixivPictureDownloader.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.tools.MaterialDesign.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.jsoup.*;

public class SauceNaoMain extends Fragment {
    private FloatingButton mFabSelect;
    private ListView listView;
    public HashMap<String, Bitmap> hashMap = new HashMap<>();
    public ExecutorService threadPool;
    public String uploadBmpAbsPath;
    public boolean running = false;
    private AlertDialog alertDialog;
//    public Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFabSelect = (FloatingButton) view.findViewById(R.id.fab_select);
        listView = (ListView) view.findViewById(R.id.list);
		//      spinner=(Spinner)view.findViewById(R.id.spinner_simple);
        threadPool = Executors.newFixedThreadPool(Integer.parseInt(SharedPreferenceHelper.getString("threads", "3")));
        mFabSelect.setOnClickListener(onClickListener);
        mFabSelect.hide(false);
        new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mFabSelect.show(true);
					mFabSelect.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
					mFabSelect.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
				}
			}, 450);
		/*       String[] spinnerItems =getResources().getStringArray(R.array.databases_entries);
		 String[] spinnerItems2 =getResources().getStringArray(R.array.databases_values);
		 //自定义选择填充后的字体样式
		 ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerItems);
		 spinner.setAdapter(adapter);
		 spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		 @Override
		 public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
		 MainActivity.instance.showToast(p1.getItemAtPosition(p3));
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> p1) {
		 // TODO: Implement this method
		 }
		 });*/
        listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					final PicResults.Result result = (PicResults.Result) p1.getItemAtPosition(p3);
					ListView urlSelect = new ListView(getActivity());
					urlSelect.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result.mExtUrls));
					urlSelect.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(final AdapterView<?> p1, View p2, final int p3, long p4) {
								String url = (String) p1.getItemAtPosition(p3);
								if (url.contains("illust_id")) {
									MainActivity.instance.showFragment(PixivDownloadMain.class);
									MainActivity.instance.getFragment(PixivDownloadMain.class).editTextURL.setText(url);
								} else {
									ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
									ClipData clipData = ClipData.newPlainText("text", url);
									clipboardManager.setPrimaryClip(clipData);
									MainActivity.instance.showToast("已复制到剪贴板");
								}
								alertDialog.dismiss();
							}
						});
					alertDialog = new AlertDialog.Builder(getActivity())
                        .setView(urlSelect)
                        .setTitle("这是一个标题")
                        .setNegativeButton("我好了", null).show();
				}
			});
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_select:
                    if (running) return;
                    running = true;
                    mFabSelect.setShowProgressBackground(true);
                    mFabSelect.setIndeterminate(true);
                    MainActivity.instance.selectImage(SauceNaoMain.this);
                    break;
            }
        }
    };

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, MainActivity.instance.CROP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == Activity.RESULT_OK) {
            //      if (requestCode == MainActivity.instence.SELECT_FILE_REQUEST_CODE) {
            //  String path = Tools.ContentHelper.absolutePathFromUri(getActivity().getApplicationContext(),
            //        cropPhoto(data.getData());
            //	  } else
            if (requestCode == MainActivity.instance.SELECT_FILE_REQUEST_CODE) {
				/*    Bundle bundle = data.getExtras();
				 if (bundle != null) {
				 Bitmap bmp = bundle.getParcelable("data");
				 if (bmp == null) {
				 mFabSelect.hideProgress();
				 MainActivity.instance.showToast("选择图片出错");
				 running = false;
				 }
				 */
                uploadBmpAbsPath = Tools.ContentHelper.absolutePathFromUri(getActivity(), data.getData());//= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/picTool/search_tmp.png";
				/*	File f = new File(uploadBmpAbsPath);
				 try {
				 f.createNewFile();
				 FileOutputStream fOut = new FileOutputStream(f);
				 bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				 fOut.flush();
				 fOut.close();
				 //  getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
				 } catch (IOException e) {
				 mFabSelect.hideProgress();
				 running = false;
				 MainActivity.instance.showToast("裁剪图片出错");
				 }
				 MainActivity.instance.showToast("图片添加成功");
				 */
                mFabSelect.setImageResource(R.drawable.ic_progress);
                new Thread(new Runnable() {
						@Override
						public void run() {
							FileInputStream fInputStream;
							try {
								fInputStream = new FileInputStream(uploadBmpAbsPath);
								Connection.Response response = Jsoup.connect("https://saucenao.com/search.php?db=" + 999)
                                    .timeout(60000).data("file", "image.png", fInputStream).method(Connection.Method.POST).execute();
								if (response.statusCode() != 200) {
									running = false;
									MainActivity.instance.showToast("发生错误" + response.statusCode());
									return;
								}
								PicResults mResults = new PicResults(Jsoup.parse(response.body()));
								final ResultAdapter resultAdapter = new ResultAdapter(getActivity(), mResults.getResults());
								getActivity().runOnUiThread(new Runnable() {
										@Override
										public void run() {
											mFabSelect.setImageResource(R.drawable.fab_add);
											listView.setAdapter(resultAdapter);
											mFabSelect.hideProgress();
											running = false;
										}
									});
							} catch (Exception e) {
								MainActivity.instance.showToast(e.toString());
							}
						}
					}).start();
            } else {
                mFabSelect.hideProgress();
                mFabSelect.setImageResource(R.drawable.ic_progress);
                MainActivity.instance.showToast("取消了选择图片");
                running = false;
            }
            //	  }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            mFabSelect.hideProgress();
            mFabSelect.setImageResource(R.drawable.ic_progress);
            running = false;
            MainActivity.instance.showToast("取消选择图片");
        } else {
            MainActivity.instance.selectImage(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
