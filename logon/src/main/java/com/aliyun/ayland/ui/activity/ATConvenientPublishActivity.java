package com.aliyun.ayland.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATConvenientLifeCommunityBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATGridImageAdapter;
import com.aliyun.ayland.widget.ATFullyGridLayoutManager;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.baidu.idl.face.platform.utils.BitmapUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATConvenientPublishActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1001;
    private static final int REQUEST_CODE_CAN_SEE = 1003;
    private ATMainPresenter mPresenter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ATGridImageAdapter mATGridImageAdapter;
    private List<String> baseList = new ArrayList<>();
    private int uploadImgPosition = 0;
    private boolean submit = false;
    private ATConvenientLifeCommunityBean fromCommunity;
    private EditText editText;
    private TextView tvComeFrom, tvWhichCanSee;
    private List<ATConvenientLifeCommunityBean> canSeeCommunityBeanList = new ArrayList<>();
    private ATMyTitleBar titlebar;
    private RelativeLayout rlComeFrom, rlWhoCanSee;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_convenient_publish;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        editText = findViewById(R.id.editText);
        tvComeFrom = findViewById(R.id.tv_come_from);
        tvWhichCanSee = findViewById(R.id.tv_which_can_see);
        recyclerView = findViewById(R.id.recyclerView);
        rlWhoCanSee = findViewById(R.id.rl_who_can_see);
        rlComeFrom = findViewById(R.id.rl_come_from);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
//        if (mCommunityBeanList.size() == 0)
//            getCommunityList();
    }

    private void insertCommunityDynamic() {
        if (baseList.size() < selectList.size()) {
            submit = true;
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", editText.getText().toString());
        jsonObject.put("createPerson", ATGlobalApplication.getOpenId());
        StringBuilder imageList = new StringBuilder();
        for (String s : baseList) {
            imageList = imageList.append(s).append(",");
        }
        if (baseList.size() > 0)
            imageList.deleteCharAt(imageList.lastIndexOf(","));
        jsonObject.put("imageList", imageList);
        StringBuilder canSeeCommunity = new StringBuilder();
        for (ATConvenientLifeCommunityBean ATConvenientLifeCommunityBean : canSeeCommunityBeanList) {
            canSeeCommunity.append(ATConvenientLifeCommunityBean.getId()).append(",");
        }
        canSeeCommunity.deleteCharAt(canSeeCommunity.lastIndexOf(","));
        jsonObject.put("canSeeCommunity", canSeeCommunity);
        jsonObject.put("fromCommunity", fromCommunity.getId());
        mPresenter.request(ATConstants.Config.SERVER_URL_INSERTCOMMUNITYDYNAMIC, jsonObject);
    }

    private void getCommunityList() {
        JSONObject jsonObject = new JSONObject();
        mPresenter.request(ATConstants.Config.SERVER_URL_GETCOMMUNITYLIST, jsonObject);
    }

    private void uploadCommunityImg() {
        LocalMedia media = selectList.get(uploadImgPosition);
        int mimeType = media.getMimeType();
        String path = "";
        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            path = media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("imageBase64", BitmapUtils.bitmapToJpegBase64(BitmapUtils.loadBitmapFromFile(this, path), 100));
        mPresenter.request(ATConstants.Config.SERVER_URL_UPLOADCOMMUNITYIMG, jsonObject);
    }

    private void init() {
        fromCommunity = getIntent().getParcelableExtra("ATConvenientLifeCommunityBean");
        if (fromCommunity == null) {
            fromCommunity = new ATConvenientLifeCommunityBean();
            fromCommunity.setId(1);
            fromCommunity.setCommunityName(getString(R.string.at_square));
        }
        canSeeCommunityBeanList.add(fromCommunity);
        tvComeFrom.setText(fromCommunity.getCommunityName());
        tvWhichCanSee.setText(fromCommunity.getCommunityName());

//        mCommunityBeanList = getIntent().getParcelableArrayListExtra("communityList");
        if (ContextCompat.checkSelfPermission(ATConvenientPublishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ATConvenientPublishActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
        titlebar.setSendText(getString(R.string.at_publish));
        titlebar.setPublishClickListener(() -> {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                showToast(getString(R.string.at_please_share_something_new));
            } else {
                showBaseProgressDlg();
                if (selectList.size() != 0) {
                    submit = true;
                    uploadImgPosition = 0;
                    uploadCommunityImg();
                } else
                    insertCommunityDynamic();
            }
        });
        rlComeFrom.setOnClickListener(view -> {
        });
        rlWhoCanSee.setOnClickListener(view -> startActivityForResult(new Intent(this, ATConvenientPublishCommunityActivity.class)
                .putExtra("ATConvenientLifeCommunityBean", fromCommunity), REQUEST_CODE_CAN_SEE));
        ATFullyGridLayoutManager manager = new ATFullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mATGridImageAdapter = new ATGridImageAdapter(this, onAddPicClickListener);
        mATGridImageAdapter.setList(selectList);
        recyclerView.setAdapter(mATGridImageAdapter);
        mATGridImageAdapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                PictureSelector.create(ATConvenientPublishActivity.this).externalPicturePreview(position, selectList);
            }
        });
    }

    private ATGridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
        PictureSelector.create(ATConvenientPublishActivity.this).openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(6)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .cropCompressQuality(10)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    };


    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_INSERTCOMMUNITYDYNAMIC:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_publish_success));
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_UPLOADCOMMUNITYIMG:
                        baseList.add(jsonResult.getString("data"));
                        uploadImgPosition++;
                        if (uploadImgPosition < selectList.size()) {
                            uploadCommunityImg();
                        } else {
                            if (submit)
                                insertCommunityDynamic();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_GETCOMMUNITYLIST:
//                        mCommunityBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<CommunityBean>>() {
//                        }.getType());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAN_SEE:
                    ArrayList<ATConvenientLifeCommunityBean> selectCommunityList = data.getParcelableArrayListExtra("selectCommunityList");
                    canSeeCommunityBeanList.clear();
                    canSeeCommunityBeanList.add(fromCommunity);
                    canSeeCommunityBeanList.addAll(selectCommunityList);
                    StringBuilder cansee = new StringBuilder();
                    for (ATConvenientLifeCommunityBean ATConvenientLifeCommunityBean : canSeeCommunityBeanList) {
                        cansee.append(ATConvenientLifeCommunityBean.getCommunityName()).append(",");
                    }
                    cansee.deleteCharAt(cansee.lastIndexOf(","));
                    tvWhichCanSee.setText(cansee);
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mATGridImageAdapter.setList(selectList);
                    mATGridImageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast("开启权限后方可进行拍照操作");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}