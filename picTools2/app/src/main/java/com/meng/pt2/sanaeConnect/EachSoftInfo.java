package com.meng.pt2.sanaeConnect;

import java.util.*;

public class EachSoftInfo {
	public String lastestVersionName;
	public int lastestVersionCode;
	public int lastestSize;
	public ArrayList<SoftInfo> infoList=new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (SoftInfo si:infoList) {
			sb.append("v").append(si.versionName).append(":").append(si.versionDescribe).append("\n");
		}
		sb.append(String.format("最新版本大小:%.2f", lastestSize / 1024f / 1024f)).append("M");
		return sb.toString();
	}
}
