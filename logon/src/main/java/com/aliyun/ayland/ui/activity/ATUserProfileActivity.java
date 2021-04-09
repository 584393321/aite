package com.aliyun.ayland.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.utils.ATRealPathFromUriUtils;
import com.aliyun.ayland.widget.popup.ATTakePicturePopup;
import com.anthouse.xuhui.R;
import com.baidu.idl.face.platform.utils.BitmapUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

public class ATUserProfileActivity extends ATBaseActivity implements ATMainContract.View {
    public static final int REQUEST_CODE_CHOOSE_PICTRUE = 1002;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1003;
    private ATMainPresenter mPresenter;
    private ATTakePicturePopup mTakePicturePopup;
    private ATLoginBean mLoginBean;
    private String avatarUrl;
    private ImageView imgUserprofile;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_user_profile;
    }

    @Override
    protected void findView() {
        imgUserprofile = findViewById(R.id.img_userprofile);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void updateUserInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("avatarUrl", avatarUrl);
//        jsonObject.put("openId", mLoginBean.getOpenid());
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEUSERINFO, jsonObject);
    }

    private void updateUserHeadImge(String imageBase64) {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imageBase64", imageBase64);
//        jsonObject.put("openId", mLoginBean.getOpenid());
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEUSERHEADIMGE, jsonObject);
    }

    private void init() {
        mLoginBean = ATGlobalApplication.getATLoginBean();
        mTakePicturePopup = new ATTakePicturePopup(this);
        imgUserprofile.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else {
                mTakePicturePopup.showPopupWindow();
            }
        });
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.atpho_s_mine_touxiang)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
//        Glide.with(this).load(ATGlobalApplication.getATLoginBean().getAvatarUrl()).apply(options).into(imgUserprofile);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_UPDATEUSERHEADIMGE:
                        avatarUrl = jsonResult.getString("data");
                        Glide.with(this).load(avatarUrl).into(imgUserprofile);
                        updateUserInfo();
                        break;
                    case ATConstants.Config.SERVER_URL_UPDATEUSERINFO:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_change_profile_success));
                        avatarUrl = ATConstants.Config.BASE_ALISAAS_URL + avatarUrl;
//                        mLoginBean.setAvatarUrl(avatarUrl);
                        ATGlobalApplication.setLoginBeanStr(JSONObject.toJSONString(mLoginBean));
                        finish();
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_PICTRUE:
                    //选择图片
                    Uri uri = data.getData();
                    Bitmap bitmap;
                    if (uri == null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            bitmap = (Bitmap) bundle.get("data");
                            imgUserprofile.setImageBitmap(bitmap);
                            showBaseProgressDlg();
                            updateUserHeadImge(BitmapUtils.bitmapToJpegBase64(bitmap, 80));
                        }
                    } else {
                        bitmap = BitmapUtils.loadBitmapFromFile(ATRealPathFromUriUtils.getRealPathFromUri(this, uri));
                        imgUserprofile.setImageBitmap(bitmap);
                        showBaseProgressDlg();
                        updateUserHeadImge(BitmapUtils.bitmapToJpegBase64(bitmap, 80));
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("开启权限后方可进行拍照操作");
                } else {
                    mTakePicturePopup.showPopupWindow();
                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}