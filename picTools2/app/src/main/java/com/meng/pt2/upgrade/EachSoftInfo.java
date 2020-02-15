package com.meng.pt2.upgrade;

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
			sb.append(si.versionDescribe).append("\n");
		}
		sb.append("大小:").append(lastestSize / 1024f / 1024f).append("M");
		return sb.toString();
	}
}
