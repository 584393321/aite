package com.aliyun.ayland.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.BaseActivity;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.interfaces.OnPopupItemClickListener;
import com.aliyun.ayland.presenter.MainPresenter;
import com.aliyun.ayland.utils.FileUtil;
import com.aliyun.ayland.widget.popup.AccessRecordPopup;
import com.aliyun.ayland.widget.popup.Options1Popup;
import com.aliyun.ayland.widget.titlebar.MyTitleBar;
import com.anthouse.lgcs.R;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VisitorRegist1Activity extends BaseActivity implements MainContract.View {
    private static final int REQUEST_CODE_REGIST = 101;
    private static final int REQUEST_CODE_CAMERA = 102;
    private MainPresenter mPresenter;
    private MyTitleBar titleBar;
    private EditText etIdNumber, etName;
    private ImageView imgShot;
    private TextView tvIdType;
    private String id_type = "101";
    private AccessRecordPopup mAccessRecordPopup;
    private List<String> mIdTypeList = new ArrayList<>();
    private Options1Popup options1Popup;
    private Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visitor_regist1;
    }

    @Override
    protected void findView() {
        titleBar = findViewById(R.id.titlebar);
        etIdNumber = findViewById(R.id.et_id_number);
        imgShot = findViewById(R.id.img_shot);
        etName = findViewById(R.id.et_name);
        tvIdType = findViewById(R.id.tv_id_type);
        button = findViewById(R.id.button);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this);
        mPresenter.install(this);
    }

    private void init() {
        mIdTypeList.add("身份证");
        mIdTypeList.add("境外护照");
        mIdTypeList.add("回乡证");
        mIdTypeList.add("台胞证");
        mIdTypeList.add("军官/士兵证");
        tvIdType.setText(mIdTypeList.get(0));
        tvIdType.setOnClickListener(v -> options1Popup.showPopupWindow());
        button.setOnClickListener(view -> startActivityForResult(new Intent(this, VisitorRegist2Activity.class)
                .putExtra("id_type", id_type).putExtra("id_name", etName.getText().toString())
                .putExtra("id_number", etIdNumber.getText().toString()), REQUEST_CODE_REGIST));
        options1Popup = new Options1Popup(this, "选择证件类型", mIdTypeList);
        options1Popup.setOnItemClickListener(new OnPopupItemClickListener() {
            @Override
            public void onItemClick(int i1, int i2) {
                id_type = String.valueOf(101 + i1);
                tvIdType.setText(mIdTypeList.get(i1));
            }

            @Override
            public void onItemClick(String s1, String s2, String s3) {
            }
        });

        imgShot.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        });

        // 初始化
        initAccessTokenWithAkSk();
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(
                new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {
                        initLicense();
                    }

                    @Override
                    public void onError(OCRError error) {
                        error.printStackTrace();
                    }
                }, getApplicationContext(),
                "p8ZC5t3TaZTEGYavSeRAPyOx",
                "ali34IveUxOvNuSQds1G6blyvs8xOccp");
    }

    private void initLicense() {
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                (errorCode, e) -> {
                    final String msg;
                    switch (errorCode) {
                        case CameraView.NATIVE_SOLOAD_FAIL:
                            msg = "加载so失败，请确保apk中存在ui部分的so";
                            break;
                        case CameraView.NATIVE_AUTH_FAIL:
                            msg = "授权本地质量控制token获取失败";
                            break;
                        case CameraView.NATIVE_INIT_FAIL:
                            msg = "本地质量控制";
                            break;
                        default:
                            msg = String.valueOf(errorCode);
                    }
                });
    }


    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    String name = "";
                    String sex = "";
                    String nation = "";
                    String num = "";
                    String address = "";
                    if (result.getName() != null) {
                        name = result.getName().toString();
                    }
                    if (result.getGender() != null) {
                        sex = result.getGender().toString();
                    }
                    if (result.getEthnic() != null) {
                        nation = result.getEthnic().toString();
                    }
                    if (result.getIdNumber() != null) {
                        num = result.getIdNumber().toString();
                    }
                    if (result.getAddress() != null) {
                        address = result.getAddress().toString();
                    }
                    etIdNumber.setText(num);
                    etIdNumber.setSelection(num.length());
                    etName.setText(name);
                }
            }

            @Override
            public void onError(OCRError error) {
                showToast("识别出错,请查看log错误代码");
            }
        });
    }

    @Override
    protected void onDestroy() {
        CameraNativeHelper.release();
        // 释放内存资源
        OCR.getInstance(this).release();
        super.onDestroy();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case Constants.Config.SERVER_URL_FINDSAFETYAPPLICATION:
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
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }else if (requestCode == REQUEST_CODE_REGIST && resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}