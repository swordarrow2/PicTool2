package com.meng.tools;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;

import com.meng.app.FunctionSavePath;
import com.meng.tools.app.ExceptionCatcher;
import com.meng.tools.hash.MD5;

import java.io.*;
import java.nio.charset.*;

public class FileTool {
    private static Context context;

    public static void init(Context context) {
        FileTool.context = context;
    }

    public static File getAppFile(String path, String name) {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MediaTool/" + path + "/" + name);
       if(f.isFile()){
           if (!f.getParentFile().exists()) {
               boolean mkdirs = f.getParentFile().mkdirs();
               if (!mkdirs) {
                   return null;
               }
           } 
       } else {
           if (!f.exists()) {
               boolean mkdirs = f.mkdirs();
               if (!mkdirs) {
                   return null;
               }
           }  
       }
        
        return f;
    }

    public static File getAppFile(FunctionSavePath functionName, String name) {
        return getAppFile(functionName.toString(), name);
    }

    public static File getAppFile(FunctionSavePath functionName, String baseName, String extendName) {
        return getAppFile(functionName.toString(), baseName + "." + extendName);
    }

    public static File getAppFile(FunctionSavePath functionName, String baseName, FileFormat.FileType ft) {
        return getAppFile(functionName.toString(), baseName + "." + ft.getExtendName());
    }

    public static File getAppFile(FunctionSavePath functionName, FileFormat.FileType ft) {
        return getAppFile(functionName.toString(), (System.currentTimeMillis() / 1000) + "." + ft.getExtendName());
    }

    public static String getPreDownloadJsonPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/pixivLike.json";
    }

    public static void copyAssetsToData(Context context, String fileNameFromAssets) throws IOException {
        File filesDirectory = context.getFilesDir();
        try (InputStream is = context.getAssets().open(fileNameFromAssets)) {
            FileOutputStream os = new FileOutputStream(new File(filesDirectory, fileNameFromAssets));
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
            }
        }    
    }

    public static String generateHashFileName(byte[] fileBytes) {
        return MD5.getMd5().calculate(fileBytes).toUpperCase() + "." + FileFormat.getFileType(fileBytes);
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

    public static void fileCopy(String src, String des) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src))) {
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des))) {
                int i = -1;
                byte[] bt = new byte[1024];
                while ((i = bis.read(bt)) != -1) {
                    bos.write(bt, 0, i);
                }
            }
        } 
    }

    public static void fileCopy(File src, File des) throws IOException {
        fileCopy(src.getAbsolutePath(), des.getAbsolutePath());
    }

    public static String readString(String fileName) throws IOException {
        return readString(new File(fileName));
    }

    public static String readString(File f) throws IOException {
        byte[] filecontent = new byte[(int) f.length()];
        try (FileInputStream in = new FileInputStream(f)) {
            in.read(filecontent);
        } 
        return new String(filecontent, StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(File f) throws IOException {
        long filelength = f.length();
        byte[] filecontent = new byte[(int) filelength];
        try (FileInputStream in = new FileInputStream(f)) {
            in.read(filecontent);
        } 
        return filecontent;
    }

    public static byte[] readBytes(String path) throws IOException {
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

    public static String saveToFile(File file, Bitmap bmp) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            file.createNewFile();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } 
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return file.getAbsolutePath();
    }


    public static void savePreDownload(String str) throws IOException {
        try (FileWriter fw = new FileWriter(getPreDownloadJsonPath())) {
//            fw.flush();
            fw.write(str);
//            fw.close();
        } 
    }
}
