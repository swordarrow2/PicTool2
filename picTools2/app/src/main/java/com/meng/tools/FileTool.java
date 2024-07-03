package com.meng.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.meng.app.FunctionSavePath;
import com.meng.tools.app.ExceptionCatcher;
import com.meng.tools.hash.MD5;

import org.jsoup.helper.StringUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class FileTool {
    private static Context context;

    private static HashSet<Content> hashset = new HashSet<>();

    public static void init(Context context) {
        FileTool.context = context;
        add("FF D8 FF E0 00 10 4A 46 49 46", "jpg", FileType.jpg_JFIF);
        add("FF D8 FF E1", "jpg", FileType.jpg_Exif);

        add("89 50 4E 47 0D 0A 1A 0A", "png", FileType.png);
        add("47 49 46 38 37 61", "gif", FileType.gif_87a);
        add("47 49 46 38 39 61", "gif", FileType.gif_89a);
        add("49 49 2A 00 22 71 05 00 80 37", "tif", FileType.tif);
        add("42 4D 38 60", "bmp", FileType.bmp_16colors);
        add("42 4D 22 8C 01", "bmp", FileType.bmp_16colors);
        add("42 4D 82 40 09", "bmp", FileType.bmp_24colors);
        add("42 4D 8E 1B 03", "bmp", FileType.bmp_256colors);


        add("FF FE", "txt", FileType.Unicode_LE);
        add("FE FF", "txt", FileType.Unicode_BE);
        add("EB BB BF", "txt", FileType.UTF_8);


        add("41 43 31 30", "dwg", FileType.cad);
        add("25 50 44 46 2D 31 2E", "pdf", FileType.pdf);
        add("50 4B 03 04", "zip", FileType.zip);
        add("52 61 72 21", "rar", FileType.rar);
        add("57 41 56 45", "wav", FileType.wav);
        add("52 49 46 46", "wav", FileType.wav);
        add("41 56 49 20", "avi", FileType.avi);
        add("2E 52 4D 46", "rmvb", FileType.rmvb);
        add("6D 6F 6F 76", "mov", FileType.mov);
        add("1F 8B 08", "gz", FileType.gzip);
        add("7F 45 4C 46", "elf", FileType.elf);
        add("4D 54 68 64", "mid", FileType.mid);
        add("0E E0 00 00", "rpy", FileType.rpy_thsss_replay);
        add("78 6F 66 20", "x", FileType.x_directx_data);

        add("4D 5A 90 00", "exe", FileType.exe_maybe_dll);

        add("64 65 78 0A 30 33 35", "Android dex file", FileType.dex_android_executeable);
        add("CA FE BA BE", "Java class file", FileType.class_jvm_executeable);
        add("53 43 50 54", "ecl", FileType.ecl_zun_danmaku);
        add("08 00 00 00", "anm", FileType.anm_zun_texture);

        add("54 36 52 50", "rpy", FileType.rpy_th06_replay);
        add("54 37 52 50", "rpy", FileType.rpy_th07_replay);
        add("54 38 52 50", "rpy", FileType.rpy_th08_replay);
        add("54 39 52 50", "rpy", FileType.rpy_th09_replay);
        add("74 39 35 72", "rpy", FileType.rpy_th09_5_replay);
        add("74 31 30 72", "rpy", FileType.rpy_th10_replay);
        add("74 31 31 72", "rpy", FileType.rpy_th11_replay);
        add("74 31 32 72", "rpy", FileType.rpy_th12_replay);
        add("31 32 38 72", "rpy", FileType.rpy_th12_8_replay);
        add("74 31 33 72", "rpy", FileType.rpy_th13_th14_replay);
        add("74 31 34 33", "rpy", FileType.rpy_th14_3_replay);
        add("74 31 35 72", "rpy", FileType.rpy_th15_replay);
        add("74 31 36 72", "rpy", FileType.rpy_th16_replay);
        add("74 31 35 36", "rpy", FileType.rpy_th16_5_replay);
        add("74 31 37 72", "rpy", FileType.rpy_th17_replay);
        add("74 31 38 72", "rpy", FileType.rpy_th18_replay);

    }

    public static File getAppFile(String path, String name) {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MediaTool/" + path + "/" + name);
        if (f.isFile()) {
            if (!f.getParentFile().exists()) {
                boolean mkdirs = f.getParentFile().mkdirs();
                if (!mkdirs) {
                    return null;
                }
            }
        } else {
            if (!f.exists()) {
                boolean mkdirs = false;
                try {
                    mkdirs = f.createNewFile();
                } catch (IOException e) {

                }
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

    public static File getAppFile(FunctionSavePath functionName, String baseName, FileType ft) {
        return getAppFile(functionName.toString(), baseName + "." + ft.getExtendName());
    }

    public static File getAppFile(FunctionSavePath functionName, FileType ft) {
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
        return MD5.getMd5().calculate(fileBytes).toUpperCase() + "." + getFileType(fileBytes);
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
        if (StringUtil.isBlank(path)) {
            return null;
        }
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


    public static Content getFileType(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bs = new byte[16];
            fis.read(bs, 0, bs.length);
            return getFileType(bs);
        }
    }

    public static boolean isFormat(File file, String extendName) {
        try {
            return extendName.equalsIgnoreCase(getFileType(file).extendName);
        } catch (IOException e) {
            return false;
        }
    }

    public static Content getFileType(byte[] file) {
        for (Content content : hashset) {
            if (file.length < content.magic.length) {
                continue;
            }
            if (arrayContains(file, content.magic)) {
                return content;
            }
        }
        return null;
    }

    public static File renameByFormat(File file) throws IOException {
        File ret;
        String type = getFileType(file).extendName;
        String fullName = file.getName();
        String parent = file.getParent() + File.separator;
        int dividerIndex = fullName.lastIndexOf(".");
        if (dividerIndex == -1) {
            ret = new File(parent + fullName + "." + type);
            if (ret.exists()) {
                ret.delete();
            }
            file.renameTo(ret);
        } else {
            String fName = fullName.substring(0, dividerIndex - 1);
            ret = new File(parent + fName + "." + type);
            if (ret.exists()) {
                ret.delete();
            }
            file.renameTo(ret);
        }
        return ret;
    }

    private static boolean arrayContains(byte[] big, byte[] small) {
        for (int i = 0; i < small.length; ++i) {
            if (big[i] == 0) {
                continue;
            }
            if (small[i] != big[i]) {
                return false;
            }
        }
        return true;
    }

    private static void add(String bytes, String extendName, FileType describe) {
        hashset.add(new Content(bytes, extendName, describe));
    }

    private static byte[] getByteArray(String hex) {
        String[] hexByte = hex.split(" ");
        byte[] bs = new byte[hexByte.length];
        for (int i = 0; i < hexByte.length; ++i) {
            bs[i] = (byte) Integer.parseInt(hexByte[i], 16);
        }
        return bs;
    }

    public static class Content {
        public byte[] magic;
        public String extendName;
        public FileType describe;

        public Content(String magic, String extendName, FileType describe) {
            this.magic = getByteArray(magic);
            this.extendName = extendName;
            this.describe = describe;
        }

        @Override
        public String toString() {
            return extendName;
        }
    }

    public enum FileType {
        jpg_JFIF,
        jpg_Exif,
        png,
        gif_87a,
        gif_89a,
        tif,
        bmp_16colors,
        bmp_24colors,
        bmp_256colors,
        Unicode_LE,
        Unicode_BE,
        UTF_8,
        cad,
        pdf,
        zip,
        rar,
        wav,
        avi,
        rmvb,
        mov,
        gzip,
        elf,
        mid,
        rpy_thsss_replay,
        x_directx_data,
        exe_maybe_dll,
        dex_android_executeable,
        class_jvm_executeable,
        ecl_zun_danmaku,
        anm_zun_texture,
        rpy_th06_replay,
        rpy_th07_replay,
        rpy_th08_replay,
        rpy_th09_replay,
        rpy_th09_5_replay,
        rpy_th10_replay,
        rpy_th11_replay,
        rpy_th12_replay,
        rpy_th12_5_replay,
        rpy_th12_8_replay,
        rpy_th13_th14_replay,
        rpy_th14_3_replay,
        rpy_th15_replay,
        rpy_th16_replay,
        rpy_th16_5_replay,
        rpy_th17_replay,
        rpy_th18_replay;

        public String getExtendName() {
            switch (this) {
                case gif_87a:
                case gif_89a:
                    return "gif";
                case jpg_JFIF:
                case jpg_Exif:
                    return "jpg";
                case png:
                    return "png";
                case bmp_16colors:
                case bmp_24colors:
                case bmp_256colors:
                    return "bmp";
                case Unicode_LE:
                case Unicode_BE:
                case UTF_8:
                    return "txt";
                case cad:
                case pdf:
                case zip:
                case rar:
                case wav:
                case avi:
                case rmvb:
                case mov:
                case gzip:
                case mid:
                    return name();
//                case elf:
//                case x_directx_data:
                case exe_maybe_dll:
                    return "exe";
                case dex_android_executeable:
                    return "dex";
                case class_jvm_executeable:
                    return "jar";
                case ecl_zun_danmaku:
                    return "ecl";
                case anm_zun_texture:
                    return "anm";
                case rpy_thsss_replay:
                case rpy_th06_replay:
                case rpy_th07_replay:
                case rpy_th08_replay:
                case rpy_th09_replay:
                case rpy_th09_5_replay:
                case rpy_th10_replay:
                case rpy_th11_replay:
                case rpy_th12_replay:
                case rpy_th12_5_replay:
                case rpy_th12_8_replay:
                case rpy_th13_th14_replay:
                case rpy_th14_3_replay:
                case rpy_th15_replay:
                case rpy_th16_replay:
                case rpy_th16_5_replay:
                case rpy_th17_replay:
                case rpy_th18_replay:
                    return "rpy";
                default:
                    throw new IllegalArgumentException("not expect value :" + name());
            }
        }
    }
}
