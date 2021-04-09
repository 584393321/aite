package com.aliyun.ayland.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.path;

/**
 * Created by 123 on 2016/12/19.
 */

public class ATLJSavePicture {
    //保存图片并且在系统图库中更新
    public static void saveImageToGallery (Context context, Bitmap bmp){
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "yunyi");
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try{
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        // 最后通知图库更新

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
    }
}
