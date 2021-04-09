package com.aliyun.ayland.widget.face;

import android.content.DialogInterface;
import android.os.Bundle;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;

import java.util.HashMap;

public class ATFaceDetectExpActivity extends FaceDetectActivity {

    private ATDefaultDialog mATDefaultDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            showMessageDialog("人脸图像采集", "采集成功");
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            showMessageDialog("人脸图像采集", "采集超时");
        }
    }

    private void showMessageDialog(String title, String message) {
        if (mATDefaultDialog == null) {
            ATDefaultDialog.Builder builder = new ATDefaultDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mATDefaultDialog.dismiss();
                                    finish();
                                }
                            });
            mATDefaultDialog = builder.create();
            mATDefaultDialog.setCancelable(true);
        }
        mATDefaultDialog.dismiss();
        mATDefaultDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
    }

}
