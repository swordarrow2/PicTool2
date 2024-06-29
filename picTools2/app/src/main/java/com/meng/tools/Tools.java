package com.meng.tools;

import android.annotation.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import java.io.*;
import java.security.*;

public class Tools {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static class FileTool {
		public static String readString(String fileName) {
			return readString(new File(fileName));
		}
		public static String readString(File f) {
			String s = "{}";
			try {      
				if (!f.exists()) {
					f.createNewFile();
				}
				long filelength = f.length();
				byte[] filecontent = new byte[(int) filelength];
				FileInputStream in = new FileInputStream(f);
				in.read(filecontent);
				in.close();
				s = new String(filecontent, DEFAULT_ENCODING);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return s;
		}
	}
	public static class Base64 {
		public static final byte[] encode(String str) {
			try {
				return encode(str.getBytes(DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		public static final byte[] encode(byte[] byteData) {
			if (byteData == null) { 
				throw new IllegalArgumentException("byteData cannot be null");
			}
			int iSrcIdx; 
			int iDestIdx; 
			byte[] byteDest = new byte[((byteData.length + 2) / 3) * 4];
			for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < byteData.length - 2; iSrcIdx += 3) {
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 2] >>> 6) & 003 | (byteData[iSrcIdx + 1] << 2) & 077);
				byteDest[iDestIdx++] = (byte) (byteData[iSrcIdx + 2] & 077);
			}
			if (iSrcIdx < byteData.length) {
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
				if (iSrcIdx < byteData.length - 1) {
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] << 2) & 077);
				} else {
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] << 4) & 077);
				}
			}
			for (iSrcIdx = 0; iSrcIdx < iDestIdx; iSrcIdx++) {
				if (byteDest[iSrcIdx] < 26) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'A');
				} else if (byteDest[iSrcIdx] < 52) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'a' - 26);
				} else if (byteDest[iSrcIdx] < 62) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + '0' - 52);
				} else if (byteDest[iSrcIdx] < 63) {
					byteDest[iSrcIdx] = '+';
				} else {
					byteDest[iSrcIdx] = '/';
				}
			}
			for (; iSrcIdx < byteDest.length; iSrcIdx++) {
				byteDest[iSrcIdx] = '=';
			}
			return byteDest;
		}

		public final static byte[] decode(String str) throws IllegalArgumentException {
			byte[] byteData = null;
			try {
				byteData = str.getBytes(DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (byteData == null) { 
				throw new IllegalArgumentException("byteData cannot be null");
			}
			int iSrcIdx; 
			int reviSrcIdx; 
			int iDestIdx; 
			byte[] byteTemp = new byte[byteData.length];
			for (reviSrcIdx = byteData.length; reviSrcIdx - 1 > 0 && byteData[reviSrcIdx - 1] == '='; reviSrcIdx--) {
				; // do nothing. I'm just interested in value of reviSrcIdx
			}
			if (reviSrcIdx - 1 == 0)	{ 
				return null; 
			}
			byte byteDest[] = new byte[((reviSrcIdx * 3) / 4)];
			for (iSrcIdx = 0; iSrcIdx < reviSrcIdx; iSrcIdx++) {
				if (byteData[iSrcIdx] == '+') {
					byteTemp[iSrcIdx] = 62;
				} else if (byteData[iSrcIdx] == '/') {
					byteTemp[iSrcIdx] = 63;
				} else if (byteData[iSrcIdx] < '0' + 10) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 52 - '0');
				} else if (byteData[iSrcIdx] < ('A' + 26)) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] - 'A');
				}  else if (byteData[iSrcIdx] < 'a' + 26) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 26 - 'a');
				}
			}
			for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < reviSrcIdx && iDestIdx < ((byteDest.length / 3) * 3); iSrcIdx += 4) {
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 2] << 6) & 0xC0 | byteTemp[iSrcIdx + 3] & 0x3F);
			}
			if (iSrcIdx < reviSrcIdx) {
				if (iSrcIdx < reviSrcIdx - 2) {
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
				} else if (iSrcIdx < reviSrcIdx - 1) {
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
				}  else {
					throw new IllegalArgumentException("Warning: 1 input bytes left to process. This was not Base64 input");
				}
			}
			return byteDest;
		}
	}

	public static class Hash {
		public static String MD5(String str) {
			try {
				return MD5(str.getBytes());
			} catch (Exception e) {
				return null;
			}
		}

		public static String MD5(byte[] bs) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				mdTemp.update(bs);
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}

		public static String MD5(File file) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				return MD5(inputStream);
			} catch (Exception e) {
				return null;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public static String MD5(InputStream inputStream) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				byte[] buffer = new byte[1024];
				int numRead = 0;
				while ((numRead = inputStream.read(buffer)) > 0) {
					mdTemp.update(buffer, 0, numRead);
				}
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}
		private static String toHexString(byte[] md) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
			int j = md.length;
			char str[] = new char[j * 2];
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
				str[i * 2 + 1] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}
	}

	public static class ContentHelper {
		@TargetApi(Build.VERSION_CODES.KITKAT)
		public static String absolutePathFromUri(final Context context, final Uri uri) {
			final boolean isKitKat=Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
			if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
				if (isExternalStorageDocument(uri)) {
					final String docId=DocumentsContract.getDocumentId(uri);
					final String[] split=docId.split(":");
					final String type=split[0];
					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}
				} else if (isDownloadsDocument(uri)) {
					final String id=DocumentsContract.getDocumentId(uri);
					final Uri contentUri=ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
					return getDataColumn(context, contentUri, null, null);
				} else if (isMediaDocument(uri)) {
					final String docId=DocumentsContract.getDocumentId(uri);
					final String[] split=docId.split(":");
					final String type=split[0];
					Uri contentUri=null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}
					final String selection="_id=?";
					final String[] selectionArgs=new String[]{
                        split[1]
					};
					return getDataColumn(context, contentUri, selection, selectionArgs);
				}
			} else if ("content".equalsIgnoreCase(uri.getScheme())) {
				return getDataColumn(context, uri, null, null);
			} else if ("file".equalsIgnoreCase(uri.getScheme())) {
				return uri.getPath();
			}
			return null;
		}

		private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
			Cursor cursor=null;
			final String column="_data";
			final String[] projection={
                column
			};
			try {
				cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
				if (cursor != null && cursor.moveToFirst()) {
					return cursor.getString(cursor.getColumnIndexOrThrow(column));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			return null;
		}

		private static boolean isExternalStorageDocument(Uri uri) {
			return "com.android.externalstorage.documents".equals(uri.getAuthority());
		}

		private static boolean isDownloadsDocument(Uri uri) {
			return "com.android.providers.downloads.documents".equals(uri.getAuthority());
		}

		private static boolean isMediaDocument(Uri uri) {
			return "com.android.providers.media.documents".equals(uri.getAuthority());
		}
	}

	public static class BitConverter {
		public static byte[] getBytes(short s) {
			byte[] bs=new byte[2];
			bs[0] = (byte) ((s >> 0) & 0xff);
			bs[1] = (byte) ((s >> 8) & 0xff) ;
			return bs;	
		}

		public static byte[] getBytes(int i) {
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 0) & 0xff);
			bs[1] = (byte) ((i >> 8) & 0xff);
			bs[2] = (byte) ((i >> 16) & 0xff);
			bs[3] = (byte) ((i >> 24) & 0xff);
			return bs;	
		}

		public static byte[] getBytes(long l) {
			byte[] bs=new byte[8];
			bs[0] = (byte) ((l >> 0) & 0xff);
			bs[1] = (byte) ((l >> 8) & 0xff);
			bs[2] = (byte) ((l >> 16) & 0xff);
			bs[3] = (byte) ((l >> 24) & 0xff);
			bs[4] = (byte) ((l >> 32) & 0xff);
			bs[5] = (byte) ((l >> 40) & 0xff);
			bs[6] = (byte) ((l >> 48) & 0xff);
			bs[7] = (byte) ((l >> 56) & 0xff);
			return bs;
		}

		public static byte[] getBytes(float f) {
			int i = Float.floatToIntBits(f);
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 24) & 0xff);
			bs[1] = (byte) ((i >> 16) & 0xff);
			bs[2] = (byte) ((i >> 8) & 0xff);
			bs[3] = (byte) ((i >> 0) & 0xff);
			return bs;	
		}

		public static byte[] getBytes(double d) {
			long l = Double.doubleToLongBits(d);
			byte[] bs = new byte[8];
			bs[0] = (byte) ((l >> 56) & 0xff);
			bs[1] = (byte) ((l >> 48) & 0xff);
			bs[2] = (byte) ((l >> 40) & 0xff);
			bs[3] = (byte) ((l >> 32) & 0xff);
			bs[4] = (byte) ((l >> 24) & 0xff);
			bs[5] = (byte) ((l >> 16) & 0xff);
			bs[6] = (byte) ((l >> 8) & 0xff);
			bs[7] = (byte) ((l >> 0) & 0xff);
			return bs;
		}

		public static byte[] getBytes(String s) {
			try {
				return s.getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static short toShort(byte[] data, int pos) {
			return (short) ((data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8);
		}

		public static short toShort(byte[] data) {
			return toShort(data , 0);
		}

		public static int toInt(byte[] data, int pos) {
			return (data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
		}

		public static int toInt(byte[] data) {
			return toInt(data, 0);
		}

		public static long toLong(byte[] data, int pos) {
			return ((data[pos] & 0xffL) << 0) | (data[pos + 1] & 0xffL) << 8 | (data[pos + 2] & 0xffL) << 16 | (data[pos + 3] & 0xffL) << 24 | (data[pos + 4] & 0xffL) << 32 | (data[pos + 5] & 0xffL) << 40 | (data[pos + 6] & 0xffL) << 48 | (data[pos + 7] & 0xffL) << 56;
		}

		public static long toLong(byte[] data) {
			return toLong(data , 0);
		}

		public static float toFloat(byte[] data, int pos) {
			int i= (data[pos] & 0xff) << 24 | (data[pos + 1] & 0xff) << 16 | (data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff) << 0;
			return Float.intBitsToFloat(i);
		}

		public static float toFloat(byte[] data) {
			return toFloat(data , 0);
		}

		public static double toDouble(byte[] data, int pos) {
			long l = ((data[pos] & 0xffL) << 56) | (data[pos + 1] & 0xffL) << 48 | (data[pos + 2] & 0xffL) << 40 | (data[pos + 3] & 0xffL) << 32 | (data[pos + 4] & 0xffL) << 24 | (data[pos + 5] & 0xffL) << 16 | (data[pos + 6] & 0xffL) << 8 | (data[pos + 7] & 0xffL) << 0;
			return Double.longBitsToDouble(l);
		}

		public static double toDouble(byte[] data) {
			return toDouble(data, 0);
		}

		public static String toString(byte[] data, int pos, int byteCount) {
			try {
				return new String(data, pos, byteCount, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static String toString(byte[] data) {
			return toString(data, 0, data.length);
		}
	}
}
