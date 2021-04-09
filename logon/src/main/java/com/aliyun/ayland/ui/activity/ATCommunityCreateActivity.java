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
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATCommunityTypeBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.utils.ATRealPathFromUriUtils;
import com.aliyun.ayland.widget.ATMyCircleImageView;
import com.aliyun.ayland.widget.popup.ATTakePicturePopup;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.baidu.idl.face.platform.utils.BitmapUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ATCommunityCreateActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int REQUEST_CODE_CHOOSE_COMMUNITY_TYPE = 1001;
    public static final int REQUEST_CODE_CHOOSE_PICTRUE = 1002;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1003;
    private ATMainPresenter mPresenter;
    private ATTakePicturePopup mATTakePicturePopup;
    private ATLoginBean mATLoginBean;
    private String communityIcon, communityName, communitySynopsis;
    private ArrayList<ATCommunityTypeBean> mATCommunityTypeBeanList;
    private ATCommunityTypeBean communityType;
    private int communityTypePosition = 0;
    private ATHouseBean mATHouseBean;
    private ATMyCircleImageView imgUserprofile;
    private LinearLayout llCommunityStyle;
    private EditText etCommunityName, etCommunityInfo;
    private Button button;
    private TextView tvCreatePerson, tvPhone, tvAddress, tvCommunityStyle;
    private ATMyTitleBar titlebar;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_create_community;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        tvCreatePerson = findViewById(R.id.tv_create_person);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvCommunityStyle = findViewById(R.id.tv_community_style);
        button = findViewById(R.id.button);
        imgUserprofile = findViewById(R.id.img_userprofile);
        llCommunityStyle = findViewById(R.id.ll_community_style);
        etCommunityInfo = findViewById(R.id.et_community_info);
        etCommunityName = findViewById(R.id.et_community_name);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getCommunityTypeList();
    }

    private void insertCommunity() {
        showBaseProgressDlg();

        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(communityIcon))
            jsonObject.put("communityIcon", communityIcon);
        jsonObject.put("communityName", communityName);
        jsonObject.put("communitySynopsis", communitySynopsis);
        jsonObject.put("communityType", communityType.getId());
//        jsonObject.put("createPerson", mATLoginBean.getOpenid());
        jsonObject.put("createPersonBuildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("createPersonName", mATLoginBean.getNickName());
        jsonObject.put("createPersonPhone", ATGlobalApplication.getAccount());
        jsonObject.put("createPersonVillageId", mATHouseBean.getVillageId());
//        jsonObject.put("updatePerson", mATLoginBean.getOpenid());
        mPresenter.request(ATConstants.Config.SERVER_URL_INSERTCOMMUNITY, jsonObject);
    }

    private void getCommunityTypeList() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        mPresenter.request(ATConstants.Config.SERVER_URL_GETCOMMUNITYTYPELIST, jsonObject);
    }

    private void insertCommunityImg(String imageBase64) {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imageBase64", imageBase64);
        mPresenter.request(ATConstants.Config.SERVER_URL_INSERTCOMMUNITYIMG, jsonObject);
    }

    private void init() {
        mATLoginBean = ATGlobalApplication.getATLoginBean();
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        tvCreatePerson.setText(mATLoginBean.getNickName());
        tvPhone.setText(ATGlobalApplication.getAccount());
        ATHouseBean ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (mATHouseBean != null)
            tvAddress.setText(ATHouseBean.getName());

        mATTakePicturePopup = new ATTakePicturePopup(this);
        imgUserprofile.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else {
                mATTakePicturePopup.showPopupWindow();
            }
        });
        llCommunityStyle.setOnClickListener(view -> startActivityForResult(new Intent(this, ATCommunityTypeActivity.class)
                .putParcelableArrayListExtra("CommunityTypeBeanList", mATCommunityTypeBeanList)
                .putExtra("position", communityTypePosition), REQUEST_CODE_CHOOSE_COMMUNITY_TYPE));
        button.setOnClickListener(view -> {
            communityName = etCommunityName.getText().toString();
            if (TextUtils.isEmpty(communityName)) {
                showToast(getString(R.string.at_input_community_name));
                return;
            }
            communitySynopsis = etCommunityInfo.getText().toString();
            if (TextUtils.isEmpty(communitySynopsis)) {
                showToast(getString(R.string.at_input_community_info));
                return;
            }
            insertCommunity();
        });

        if (this.getIntent().getStringExtra("communityOpenId") != null) {
            editCommunity();
        }

    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETCOMMUNITYTYPELIST:
                        mATCommunityTypeBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATCommunityTypeBean>>() {
                        }.getType());
                        if (mATCommunityTypeBeanList.size() > 0) {
                            communityType = mATCommunityTypeBeanList.get(communityTypePosition);
                            tvCommunityStyle.setText(communityType.getTypeName());
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_INSERTCOMMUNITYIMG:
                        communityIcon = jsonResult.getString("data");
//                        Glide.with(this).load(communityIcon).into(imgUserprofile);
//                        updateUserInfo();
                        break;
                    case ATConstants.Config.SERVER_URL_INSERTCOMMUNITY:
                        showToast(getString(R.string.at_create_community_success));
                        finish();
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_COMMUNITY_TYPE:
                    communityTypePosition = data.getIntExtra("position", 0);
                    communityType = mATCommunityTypeBeanList.get(communityTypePosition);
                    tvCommunityStyle.setText(communityType.getTypeName());
                    break;
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
                            insertCommunityImg(BitmapUtils.bitmapToJpegBase64(bitmap, 80));
                        }
                    } else {
                        bitmap = BitmapUtils.loadBitmapFromFile(ATRealPathFromUriUtils.getRealPathFromUri(this, uri));
                        imgUserprofile.setImageBitmap(bitmap);
                        showBaseProgressDlg();
                        insertCommunityImg(BitmapUtils.bitmapToJpegBase64(bitmap, 80));
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast("开启权限后方可进行拍照操作");
            } else {
                mATTakePicturePopup.showPopupWindow();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void editCommunity() {
        titlebar.setTitle("编辑社群");
        etCommunityName.setText(this.getIntent().getStringExtra("communityName"));
        etCommunityInfo.setText(this.getIntent().getStringExtra("communityInfo"));
        tvCommunityStyle.setText(this.getIntent().getStringExtra("communityType"));
        button.setOnClickListener(view -> {
            communityName = etCommunityName.getText().toString();
            if (TextUtils.isEmpty(communityName)) {
                showToast(getString(R.string.at_input_community_name));
                return;
            }
            communitySynopsis = etCommunityInfo.getText().toString();
            if (TextUtils.isEmpty(communitySynopsis)) {
                showToast(getString(R.string.at_input_community_info));
            }
        });
    }
}