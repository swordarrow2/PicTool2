package com.meng.pt2.sanaeConnect;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.pt2.*;
import com.meng.pt2.tools.*;
import java.io.*;

import com.meng.pt2.R;

public class CrashUploadFragment extends Fragment {
    private final int requestFileCode = 1001;
    private Button btnSend;
	private Button btnSelect;
	private TextView text;
	private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crash_upload, container, false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		text = (TextView)view.findViewById(R.id.crash_uploadTextView);
        btnSend = (Button) view.findViewById(R.id.crash_uploadButton_upload);
		btnSelect = (Button) view.findViewById(R.id.crash_uploadButton_select);
		btnSelect.setOnClickListener(onClick);
		btnSend.setOnClickListener(onClick);
	}

	OnClickListener onClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.crash_uploadButton_select:
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("*/*");
					startActivityForResult(intent, requestFileCode);
					break;
				case R.id.crash_uploadButton_upload:
					BotDataPack bdp=BotDataPack.encode(BotDataPack.opCrashLog);
					PackageInfo packageInfo=null;
					try {
						packageInfo = MainActivity2.instence.getPackageManager().getPackageInfo(MainActivity2.instence.getPackageName(), 0);
					} catch (PackageManager.NameNotFoundException e) {
						LogTool.e("发送失败");
						return;
					}
					bdp.write(packageInfo.packageName);
					bdp.write(packageInfo.versionCode);
					bdp.write(new File(path));
					if (MainActivity2.instence.sanaeConnect.isClosed()) {
						MainActivity2.instence.sanaeConnect.connect();
					}
					MainActivity2.instence.sanaeConnect.send(bdp.getData());
					//LogTool.t("发送成功");
					break;	
			}
		}
	};

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == requestFileCode) {
            Uri inputUri = data.getData();
            path = Tools.ContentHelper.absolutePathFromUri(getActivity(), inputUri);
            if (TextUtils.isEmpty(path)) {
				LogTool.t("路径未找到");
				return;
			}
			text.setText(Tools.FileTool.readString(new File(path)));
		}
	}
}
