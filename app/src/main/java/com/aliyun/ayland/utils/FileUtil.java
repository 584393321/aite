package com.aliyun.ayland.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**

 */
public class FileUtil {

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH = Environment.getExternalStorageDirectory().toString();
    public static final String VIDEO_PATH = "/atsmartlife/monitor_video/";
    public static final String IMAGE_PATH = "/atsmartlife/image/";
    public static final String FIRMWARE_PATH = "/atsmartlife/firmware/";

    public static final String HCF_IMGAE_PATH = "/atsmartlife/hcf_image/";
    public static final String HCF_VIDEO_PATH = "atsmartlife/hcf_monitor_video/";
    //	public static final String SCENE = "scene";
    //这个文件用来保存用户自定义的场景模式图片
    public static final String SCENECUSTOMPIC = "scenecustompic";

    public static boolean isSdcardMounted() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public static File getSaveFile(Context context) {
        return new File(context.getFilesDir(), "pic.jpg");
    }

    /**
     * 写数据到本地的data目录下
     *
     * @param str
     * @param fileName 只需要添加文件名字 03-01 08:52:51.241: E/xhc(1415): amusement: open
     *                 failed: EROFS (Read-only file system)
     */
    public static void writeSomeToSdcard(String str, String fileName, Context context) {
        if (isSdcardMounted()) {
            // String path = /*"/sdcard/"*/BASE_PATH + context.getFilesDir() +
            // File.separator + fileName;

            byte[] buf = null;
            FileOutputStream stream = null;
            try {
                stream = context.openFileOutput(fileName, context.MODE_PRIVATE);
                buf = str.getBytes();
                stream.write(buf);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件从sdcard中
     *
     * @param fileName
     * @param context
     * @return
     */
    // 读数据
    public static String readFileFromData(String fileName, Context context) {
        String res = "";
        try {
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * Delete corresponding path, file or directory.
     *
     * @param file path to delete.
     */
    public static void delete(File file) {
        delete(file, false);
    }

    /**
     * Delete corresponding path, file or directory.
     *
     * @param file      path to delete.
     * @param ignoreDir whether ignore directory. If true, all files will be deleted
     *                  while directories is reserved.
     */
    public static void delete(File file, boolean ignoreDir) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] fileList = file.listFiles();
        if (fileList == null) {
            return;
        }

        for (File f : fileList) {
            delete(f, ignoreDir);
        }
        // delete the folder if need.
        if (!ignoreDir)
            file.delete();
    }

    public static String[] getListFileFromAssets(Context ctx, String path) {
        String str[] = null;
        try {
            str = ctx.getAssets().list(path);
            if (str != null && str.length > 0) {
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPicDir(Context context) {

        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //根据当前时间生成图片的名称
//        String fileName = "/" + System.currentTimeMillis() + "_" + ".jpg";
//        File file = new File(appDir, fileName);
////        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
////        File file = new File(rootPath, IMAGE_PATH);
//        if (!file.exists())
//            file.mkdirs();
        return appDir.getPath();
    }

    public static String getVideoPath(Context context) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(rootPath, VIDEO_PATH);
        if (!file.exists())
            file.mkdirs();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = df.format(new Date()) + ".avi";
        File file1 = new File(file.getPath(), fileName);
        return file1.getPath();
    }


    public static String getHCFPicDir(Context context) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(rootPath, HCF_IMGAE_PATH);
        if (!file.exists())
            file.mkdirs();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = df.format(new Date()) + ".jpg";
        File file1 = new File(file.getPath(), fileName);
        return file1.getPath();
    }

    public static String getHCFVideoPath(Context context) {
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(rootPath, HCF_VIDEO_PATH);
        if (!file.exists())
            file.mkdirs();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = df.format(new Date()) + ".avi";
        File file1 = new File(file.getPath(), fileName);
        return file1.getPath();
    }

    public static String getFirmwarePath(Context context) {
        //AppUtils.getAppName(context);
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(rootPath, FIRMWARE_PATH);
        if (!file.exists())
            file.mkdirs();
        return file.getPath();
    }

    public static boolean existsFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return existsFile(new File(path));
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean existsFile(File file) {
        return file != null && file.exists() && file.isFile();
    }
}
