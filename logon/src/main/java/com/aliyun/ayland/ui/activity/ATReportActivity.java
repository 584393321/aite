package com.aliyun.ayland.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATGridImageAdapter;
import com.aliyun.ayland.utils.ATFormdataUpload;
import com.aliyun.ayland.widget.ATFullyGridLayoutManager;
import com.anthouse.xuhui.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.aliyun.ayland.global.ATConstants.Config.SERVER_URL_ADDIMGFILE;

public class ATReportActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1001;
    private ATMainPresenter mPresenter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ATGridImageAdapter mATGridImageAdapter;
    private List<String> baseList = new ArrayList<>();
    private int uploadImgPosition = 0, beReportedId;
    private String reportMessage;
    private boolean submit = false, report_dynamic;
    private ExecutorService sendThreads = Executors.newSingleThreadExecutor();
    private Button button;
    private EditText editText;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_report;
    }

    @Override
    protected void findView() {
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void report() {
        if (report_dynamic) {
            reportDynamic();
        } else {
            reportCommunity();
        }
    }

    private void reportCommunity() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("beReportedCommunityId", beReportedId);
        StringBuilder reportImage = new StringBuilder();
        for (String s : baseList) {
            reportImage = reportImage.append(s).append(",");
        }
        if (baseList.size() > 0)
            reportImage.deleteCharAt(reportImage.lastIndexOf(","));
        jsonObject.put("reportImage", reportImage);
        jsonObject.put("reportMessage", reportMessage);
        jsonObject.put("reportPersonId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_REPORTCOMMUNITY, jsonObject);
    }

    private void reportDynamic() {
        if (baseList.size() < selectList.size()) {
            submit = true;
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("beReportedDynamicId", beReportedId);
        StringBuilder reportImage = new StringBuilder();
        for (String s : baseList) {
            reportImage = reportImage.append(s).append(",");
        }
        if (baseList.size() > 0)
            reportImage.deleteCharAt(reportImage.lastIndexOf(","));
        jsonObject.put("reportImage", reportImage);
        jsonObject.put("reportMessage", reportMessage);
        jsonObject.put("reportPersonId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_REPORTDYNAMIC, jsonObject);
    }

    private void uploadCommunityImg() {
        if (baseList.size() < selectList.size()) {
            submit = true;
            return;
        }
        LocalMedia media = selectList.get(uploadImgPosition);
        Map<String, String> textMap = new HashMap<>();
        Map<String, String> fileMap = new HashMap<>();
        String path;
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
        String finalPath = path;
        sendThreads.execute(() -> {
            String urlStr = String.format(ATConstants.Config.SERVER_BASE_URL, SERVER_URL_ADDIMGFILE) + "?type=" + (report_dynamic ? "reportCommunityDynamicImg" : "reportCommunityImg");
            Log.e("uploadCommunityImg1: ", urlStr);
            textMap.put("name", "testname");
            fileMap.put("file", finalPath);
            String jsonResult = ATFormdataUpload.formUpload(urlStr, textMap, fileMap);
            try {
                Log.e("uploadCommunityImg: ", jsonResult + "---");
                baseList.add(new org.json.JSONObject(jsonResult).getString("data"));
                uploadImgPosition++;
                if (uploadImgPosition < selectList.size()) {
                    SystemClock.sleep(500);
                    uploadCommunityImg();
                } else {
                    if (submit)
                        report();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void init() {
        report_dynamic = getIntent().getBooleanExtra("report_dynamic", false);
        beReportedId = getIntent().getIntExtra("beReportedId", 0);

        if (ContextCompat.checkSelfPermission(ATReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ATReportActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }

        button.setOnClickListener(view -> {
            reportMessage = editText.getText().toString();
            if (TextUtils.isEmpty(reportMessage)) {
                showToast(getString(R.string.at_report_reason));
            } else {
                showBaseProgressDlg();
                if (selectList.size() != 0) {
                    submit = true;
                    uploadImgPosition = 0;
                    uploadCommunityImg();
                } else
                    report();
            }
        });

        ATFullyGridLayoutManager manager = new ATFullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mATGridImageAdapter = new ATGridImageAdapter(this, onAddPicClickListener);
        mATGridImageAdapter.setList(selectList);
        recyclerView.setAdapter(mATGridImageAdapter);
        mATGridImageAdapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                PictureSelector.create(ATReportActivity.this).externalPicturePreview(position, selectList);
            }
        });
    }

    private ATGridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
        PictureSelector.create(ATReportActivity.this).openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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
                    case ATConstants.Config.SERVER_URL_REPORTDYNAMIC:
                    case ATConstants.Config.SERVER_URL_REPORTCOMMUNITY:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_report_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_UPLOADCOMMUNITYIMG:
                        baseList.add(jsonResult.getString("data"));
                        uploadImgPosition++;
                        if (uploadImgPosition < selectList.size()) {
                            uploadCommunityImg();
                        } else {
                            if (submit)
                                report();
                        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("开启权限后方可进行拍照操作");
                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mATGridImageAdapter.setList(selectList);
                    mATGridImageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}