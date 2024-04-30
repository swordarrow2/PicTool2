package com.meng.mediatool.tools;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import com.meng.mediatool.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTool {
    private static Context context;

    public static void init(Context context) {
        FileTool.context = context;
    }

    public static File getDefaultFolder(FileType type) {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pictures/mdt/" + type.toString());
        if (!f.exists()) f.mkdirs();
        return f;
    }

    public static String getSaveFileAbsPath(String name, FileType type, String format) {
        return getDefaultFolder(type) + "/" + name + "." + format;
    }

    public static String getSaveFileAbsPath(String name, FileType type, FileFormat.FileType ft) {
        return getDefaultFolder(type) + "/" + name + "." + ft.getExtendName();
    }

    public static String getSaveFileAbsPath(FileType type, FileFormat.FileType ft) {
        return getDefaultFolder(type) + "/" + (System.currentTimeMillis() / 1000) + "." + ft.getExtendName();
    }

    public static String getPreDownloadJsonPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/pixivLike.json";
    }

    public static boolean copyAssetsToData(Context context, String fileNameFromAssets) {
        File filesDirectory = context.getFilesDir();
        InputStream is;
        try {
            is = context.getAssets().open(fileNameFromAssets);
            FileOutputStream os = new FileOutputStream(new File(filesDirectory, fileNameFromAssets));
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
            }
            return true;
        } catch (IOException e) {
            MainActivity.instance.showToast(e.toString());
        }
        return false;
    }

    public static String generateHashFileName(byte[] fileBytes) {
        return Hash.getMd5().calculate(fileBytes).toUpperCase() + "." + FileFormat.getFileType(fileBytes);
    }

    public static void deleteFiles(File folder) {
        File[] fs = folder.listFiles();
        if (fs != null && fs.length > 0) {
            for (File f : fs) {
                if (f.isDirectory()) {
                    deleteFiles(f);
                } 
                f.delete();               
            }
        }
    }

    public static void fileCopy(String src, String des) {
        try {
            BufferedInputStream bis = null;
            bis = new BufferedInputStream(new FileInputStream(src));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
            int i = -1;
            byte[] bt = new byte[1024];
            while ((i = bis.read(bt)) != -1) {
                bos.write(bt, 0, i);
            }
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String fileName) {
        return readString(new File(fileName));
    }

    public static String readString(File f) {
        try {      
            long filelength = f.length();
            byte[] filecontent = new byte[(int) filelength];
            FileInputStream in = new FileInputStream(f);
            in.read(filecontent);
            in.close();
            return new String(filecontent, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readBytes(File f) {
        byte[] filecontent = null;
        try {
            long filelength = f.length();
            filecontent = new byte[(int) filelength];
            FileInputStream in = new FileInputStream(f);
            in.read(filecontent);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filecontent;
    }

    public static byte[] readBytes(String path) {
        return readBytes(new File(path));
    }

    public static File createFile(String path) throws IOException {
        return createFile(new File(path));
    }

    public static File createFile(File file) throws IOException {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public static String saveToFile(File file, String content) {
        return saveToFile(file, content.getBytes(StandardCharsets.UTF_8)); 
    }

    public static String saveToFile(File file, byte[] content) {
        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content);
            fos.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
            return null;
        }
    }

    public static String saveToFile(Bitmap bmp, FileType t) {
        String fileAbsPath = getDefaultFolder(t).getAbsolutePath() + "/" + (System.currentTimeMillis() / 1000) + ".png";
        File f = new File(fileAbsPath);
        try {
            f.createNewFile();
            FileOutputStream fOut = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
        } catch (IOException e) {
            ExceptionCatcher.getInstance().uncaughtException(Thread.currentThread(), e);
            return null;
        }
        return f.getAbsolutePath();
    }

    public static void savePreDownload(String str) {
        try {
            FileWriter fw = new FileWriter(getPreDownloadJsonPath());//SD卡中的路径
            fw.flush();
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
