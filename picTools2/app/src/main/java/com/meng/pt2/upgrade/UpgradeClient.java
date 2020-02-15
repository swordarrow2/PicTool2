package com.meng.pt2.upgrade;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.support.v7.app.*;
import com.google.gson.*;
import com.meng.pt2.*;
import com.meng.pt2.tools.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

public class UpgradeClient extends WebSocketClient {

	public UpgradeClient() throws Exception {
		super(new URI("ws://123.207.65.93:9234"));
	}

	@Override
	public void onOpen(ServerHandshake p1) {
		try {
			PackageInfo packageInfo = MainActivity2.instence.getPackageManager().getPackageInfo(MainActivity2.instence.getPackageName(), 0);
			CheckNewBean cnb=new CheckNewBean();
			cnb.packageName = packageInfo.packageName;
			cnb.nowVersionCode = packageInfo.versionCode;
			send(new Gson().toJson(cnb));
		} catch (PackageManager.NameNotFoundException e) {
			LogTool.e(e);
		}
	}

	@Override
	public void onMessage(String p1) {
		if (!p1.equals("")) {
			final EachSoftInfo esi=new Gson().fromJson(p1, EachSoftInfo.class);
			if (!SharedPreferenceHelper.getValue("newVersion", "0.0.0").equals(esi.infoList.get(esi.infoList.size() - 1).versionName)) {	
				MainActivity2.instence.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							new AlertDialog.Builder(MainActivity2.instence)
								.setTitle("发现新版本")
								.setMessage(esi.toString())
								.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface p1, int p2) {
										send(BotDataPack.encode(BotDataPack.opGetApp).write(MainActivity2.instence.getPackageName()).getData());
										LogTool.t("开始下载");
									}
								}).setNeutralButton("下次提醒我", null)
								.setNegativeButton("忽略本次更新", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										SharedPreferenceHelper.putValue("newVersion", esi.infoList.get(esi.infoList.size() - 1).versionName);
									}
								}).show();
						}
					});
			}
		}
	}

	@Override
	public void onMessage(ByteBuffer bytes) {
		BotDataPack bdp=BotDataPack.decode(bytes.array());
		if (bdp.getOpCode() == BotDataPack.opGetApp) {
			File f=new File(Environment.getExternalStorageDirectory() + "/download/" + MainActivity2.instence.getPackageName() + ".apk");
			bdp.readFile(f);
			LogTool.t("文件已保存至" + f.getAbsolutePath());
		}
		super.onMessage(bytes);
	}

	@Override
	public void onClose(int p1, String p2, boolean p3) {
		// TODO: Implement this method
	}

	@Override
	public void onError(Exception p1) {
		// TODO: Implement this method
	}
}
