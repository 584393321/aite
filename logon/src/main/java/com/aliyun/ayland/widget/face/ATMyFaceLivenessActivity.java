package com.aliyun.ayland.widget.face;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity1;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;
import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ILivenessStrategy;
import com.baidu.idl.face.platform.ILivenessStrategyCallback;
import com.baidu.idl.face.platform.ui.FaceSDKResSettings;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.BitmapUtils;
import com.baidu.idl.face.platform.utils.CameraPreviewUtils;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * 活体检测界面
 */
public class ATMyFaceLivenessActivity extends ATBaseActivity1 implements ATMainContract.View,
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        Camera.ErrorCallback,
        VolumeUtils.VolumeCallback,
        ILivenessStrategyCallback {
    private static final int RESULT_CODE_STARTCAMERA = 1001;
    private final int MSG_SPECIAL_QUERY = 1;
    public static final String TAG = ATMyFaceLivenessActivity.class.getSimpleName();
    private ATMainPresenter mPresenter;
    private LinearLayout llRight;
    private FrameLayout livenessSurfaceLayout;
    private ImageView imgResult, livenessSuccessImage;
    private Button btnLogging;
    private FaceDetectRoundView livenessFaceRound;
    private TextView tvText, tvBack, livenessBottomTips;
    // View
    protected SurfaceView mSurfaceView;
    protected SurfaceHolder mSurfaceHolder;
    // 人脸信息
    protected FaceConfig mFaceConfig;
    protected ILivenessStrategy mILivenessStrategy;
    // 显示Size
    private Rect mPreviewRect = new Rect();
    protected int mDisplayWidth = 0;
    protected int mDisplayHeight = 0;
    protected int mSurfaceWidth = 0;
    protected int mSurfaceHeight = 0;
    protected Drawable mTipsIcon;
    // 状态标识
    protected HashMap<String, String> mBase64ImageMap = new HashMap<>();
    protected boolean mIsCreateSurface = false;
    protected boolean mIsCompletion = false;
    // 相机
    protected Camera mCamera;
    protected Camera.Parameters mCameraParam;
    protected int mCameraId;
    protected int mPreviewWidth;
    protected int mPreviewHight;
    protected int mPreviewDegree;
    private String imageBase64 = "";
    private boolean idStatus = false;
    private String openId = null, personCode = null;

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SPECIAL_QUERY:

                    break;
                default:
                    break;
            }
        }
    };
    private Intent intent;
    private boolean mRefresh;
    private double mDouble;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_my_face_liveness;
    }

    @Override
    protected void findView() {
        llRight = findViewById(R.id.ll_right);
        tvText = findViewById(R.id.tv_text);
        tvBack = findViewById(R.id.tv_back);
        livenessBottomTips = findViewById(R.id.liveness_bottom_tips);
        livenessSurfaceLayout = findViewById(R.id.liveness_surface_layout);
        imgResult = findViewById(R.id.img_result);
        livenessSuccessImage = findViewById(R.id.liveness_success_image);
        btnLogging = findViewById(R.id.btn_logging);
        livenessFaceRound = findViewById(R.id.liveness_face_round);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void deleteFace() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userType", "OPEN");
        jsonObject.put("userId", idStatus ? openId : ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_DELETEFACE, jsonObject);
    }

    private void addFace() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userType", "OPEN");
        jsonObject.put("userId", idStatus ? openId : ATGlobalApplication.getOpenId());
        jsonObject.put("imageBase64", imageBase64);
//        jsonObject.put("userName", "");
//        jsonObject.put("expiredTime", "");//过期时间
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDFACE, jsonObject);
    }

    private void init() {
        openId = getIntent().getStringExtra("openId");
        personCode = getIntent().getStringExtra("personCode");
        if (!TextUtils.isEmpty(openId)) {
            idStatus = true;
        } else {
            idStatus = false;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            //提示用户开户权限   拍照和读写sd卡权限
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, perms, RESULT_CODE_STARTCAMERA);
        }

        DisplayMetrics dm = new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        mDisplayWidth = dm.widthPixels;
        mDisplayHeight = dm.heightPixels;

        FaceSDKResSettings.initializeResId();
        mFaceConfig = FaceSDKManager.getInstance().getFaceConfig();

        intent = getIntent();
        mRefresh = intent.getBooleanExtra("refresh", false);
        tvText.setText(getString(R.string.at_done));
        llRight.setVisibility(View.GONE);

        mSurfaceView = new SurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setSizeFromLayout();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.setVisibility(View.GONE);
        int w = mDisplayWidth;
        int h = mDisplayHeight;
        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * FaceDetectRoundView.SURFACE_RATIO), (int) (h * FaceDetectRoundView.SURFACE_RATIO),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mSurfaceView.setLayoutParams(cameraFL);
        livenessSurfaceLayout.addView(mSurfaceView);

        tvBack.setOnClickListener(view -> finish());
        llRight.setOnClickListener(view -> {
            if (getString(R.string.at_done).equals(tvText.getText().toString()))
                addFace();
            else
                deleteFace();
        });
        if (mBase64ImageMap != null) {
            mBase64ImageMap.clear();
        }
        if (mRefresh) {
            mSurfaceView.setVisibility(View.VISIBLE);
            imgResult.setVisibility(View.GONE);
            btnLogging.setClickable(false);
            btnLogging.setText(R.string.at_logging);
            btnLogging.setBackground(getResources().getDrawable(R.drawable.shape_66px_fcd6ab_to_f8e8c1));
        } else {
            String imageUrl = intent.getStringExtra("imageUrl");
            if (!TextUtils.isEmpty(imageUrl)) {
                llRight.setVisibility(View.VISIBLE);
                tvText.setText(getString(R.string.at_delete));
                Glide.with(this).load(imageUrl).into(imgResult);
                imgResult.setBackground(getResources().getDrawable(R.drawable.shape_6px_18pxe3c5ad));
                imgResult.setVisibility(View.VISIBLE);
                btnLogging.setText(getString(R.string.at_logging_again));
            }
        }
        btnLogging.setOnClickListener(v -> {
            switch (btnLogging.getText().toString()) {
                case "开始录入":
                    mSurfaceView.setVisibility(View.VISIBLE);
                    imgResult.setVisibility(View.GONE);
                    btnLogging.setClickable(false);
                    btnLogging.setText(R.string.at_logging);
                    btnLogging.setBackground(getResources().getDrawable(R.drawable.shape_66px_fcd6ab_to_f8e8c1));
                    break;
                case "重新录入":
                    finish();
                    intent.putExtra("refresh", true);
                    intent.setClass(ATMyFaceLivenessActivity.this, ATFaceLivenessExpActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (livenessBottomTips != null) {
            livenessBottomTips.setText(R.string.at_face_logging_tip);
        }
        startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }

    @Override
    public void onStop() {
        if (mILivenessStrategy != null) {
            mILivenessStrategy.reset();
        }
        super.onStop();
        stopPreview();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void volumeChanged() {
    }

    private Camera open() {
        Camera camera;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            return null;
        }

        int index = 0;
        while (index < numCameras) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                break;
            }
            index++;
        }

        if (index < numCameras) {
            camera = Camera.open(index);
            mCameraId = index;
        } else {
            camera = Camera.open(0);
            mCameraId = 0;
        }
        return camera;
    }

    protected void startPreview() {
        if (mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
        }

        if (mCamera == null) {
            try {
                mCamera = open();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (mCameraParam == null) {
            mCameraParam = mCamera.getParameters();
        }

        mCameraParam.setPictureFormat(PixelFormat.JPEG);
        int degree = displayOrientation(this);
        mCamera.setDisplayOrientation(degree);
        // 设置后无效，camera.setDisplayOrientation方法有效
        mCameraParam.set("rotation", degree);
        mPreviewDegree = degree;

        Point point = CameraPreviewUtils.getBestPreview(mCameraParam,
                new Point(mDisplayWidth, mDisplayHeight));

        mPreviewWidth = point.x;
        mPreviewHight = point.y;
        // Preview 768,432
        if (mILivenessStrategy != null) {
            mILivenessStrategy.setPreviewDegree(degree);
        }

        mPreviewRect.set(0, 0, mPreviewHight, mPreviewWidth);
        mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHight);
        mCamera.setParameters(mCameraParam);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.stopPreview();
            mCamera.setErrorCallback(this);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        }
    }

    protected void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.setErrorCallback(null);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CameraUtils.releaseCamera(mCamera);
                mCamera = null;
            }
        }
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(this);
        }
        if (mILivenessStrategy != null) {
            mILivenessStrategy = null;
        }
    }

    private int displayOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        int result = (0 - degrees + 360) % 360;
        if (APIUtils.hasGingerbread()) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }
        }
        return result;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsCreateSurface = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format,
                               int width,
                               int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (holder.getSurface() == null) {
            return;
        }
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsCreateSurface = false;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mIsCompletion) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Log.e("createBitmap: ", size.width + "--1--" + size.height);
                    mDouble = 1;
                    if (size.width - 800 > 0) {
                        mDouble = size.width / 800;
                    }
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);

                    byte[] bytes = stream.toByteArray();
                    String deviceModel = Build.MODEL;
                    float rotate = -90;
                    if ("Nexus 6".equals(deviceModel)) {
                        rotate = 90;
                    }
                    stream.close();
                    Bitmap bitmap2 = BitmapUtils.createBitmap(this, bytes, rotate);
//                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 0, (int)(bitmap2.getHeight() - bitmap2.getWidth() * 4 / 3) / 2,
//                            (int)(bitmap2.getWidth() * mDouble), (int)(bitmap2.getWidth() * 4 / 3 * mDouble));
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 0, (bitmap2.getHeight() - bitmap2.getWidth() * 4 / 3) / 2,
                            bitmap2.getWidth(), bitmap2.getWidth() * 4 / 3);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    byte[] data1 = out.toByteArray();
                    Bitmap bitmap = BitmapUtils.createBitmap(this, data1, 0);
                    out.close();

                    bitmap = mirrorConvert(bitmap, 0);
                    imageBase64 = BitmapUtils.bitmapToJpegBase64(bitmap, 80);
                    imgResult.setImageBitmap(bitmap);
                    imgResult.setBackground(getResources().getDrawable(R.drawable.shape_6px_18pxe3c5ad));
                    imgResult.setVisibility(View.VISIBLE);
                    livenessBottomTips.setText(getString(R.string.at_click_right_top));

                    btnLogging.setClickable(true);
                    btnLogging.setText(R.string.at_logging_again);
                    btnLogging.setCompoundDrawables(null, null, null, null);
                    btnLogging.setBackground(getResources().getDrawable(R.drawable.selector_66px_fcd6abf8e8c1_f7de92f9b49c));
                }
            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
            return;
        }

        if (mILivenessStrategy == null) {
            mILivenessStrategy = FaceSDKManager.getInstance().getLivenessStrategyModule();
            mILivenessStrategy.setPreviewDegree(mPreviewDegree);

            Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(
                    mDisplayWidth, mPreviewHight, mPreviewWidth);
            mILivenessStrategy.setLivenessStrategyConfig(
                    mFaceConfig.getLivenessTypeList(), mPreviewRect, detectRect, this);
        }
        mILivenessStrategy.livenessStrategy(data);
    }

    public Bitmap mirrorConvert(Bitmap srcBitmap, int flag) {
        //flag: 0 左右翻转，1 上下翻转
        Matrix matrix = new Matrix();
        if (flag == 0) //左右翻转
            matrix.setScale(-1, 1);
        if (flag == 1)
            matrix.setScale(1, -1);
        return Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
    }

    @Override
    public void onError(int error, Camera camera) {
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        if (mIsCompletion) {
            return;
        }
        onRefreshView(status, message);
        if (status == FaceStatusEnum.OK) {
            mIsCompletion = true;
            llRight.setVisibility(View.VISIBLE);
            startPreview();
            mSurfaceView.setVisibility(View.GONE);
        }
        Ast.getInstance().faceHit("liveness");
    }

    private void onRefreshView(FaceStatusEnum status, String message) {
        switch (status) {
            case OK:
            case Liveness_OK:
            case Liveness_Completion:
                onRefreshTipsView(false, message);
                livenessBottomTips.setText(getString(R.string.at_click_right_top));
                livenessFaceRound.processDrawState(false);
                onRefreshSuccessView(true);
                break;
            case Detect_DataNotReady:
            case Liveness_Eye:
            case Liveness_Mouth:
            case Liveness_HeadUp:
            case Liveness_HeadDown:
            case Liveness_HeadLeft:
            case Liveness_HeadRight:
            case Liveness_HeadLeftRight:
                onRefreshTipsView(false, message);
                livenessFaceRound.processDrawState(false);
                onRefreshSuccessView(false);
                break;
            case Detect_PitchOutOfUpMaxRange:
            case Detect_PitchOutOfDownMaxRange:
            case Detect_PitchOutOfLeftMaxRange:
            case Detect_PitchOutOfRightMaxRange:
                onRefreshTipsView(true, message);
                livenessBottomTips.setText(message);
                livenessFaceRound.processDrawState(true);
                onRefreshSuccessView(false);
                break;
            default:
                onRefreshTipsView(false, message);
                livenessFaceRound.processDrawState(true);
                onRefreshSuccessView(false);
        }
    }

    private void onRefreshTipsView(boolean isAlert, String message) {
        if (isAlert) {
            if (mTipsIcon == null) {
                mTipsIcon = getResources().getDrawable(R.mipmap.ic_warning);
                mTipsIcon.setBounds(0, 0, (int) (mTipsIcon.getMinimumWidth() * 0.7f),
                        (int) (mTipsIcon.getMinimumHeight() * 0.7f));
                livenessBottomTips.setCompoundDrawablePadding(15);
            }
            livenessBottomTips.setBackgroundResource(R.drawable.bg_tips);
            livenessBottomTips.setText(R.string.detect_standard);
            livenessBottomTips.setCompoundDrawables(mTipsIcon, null, null, null);
        } else {
            livenessBottomTips.setBackgroundResource(R.drawable.bg_tips_no);
            livenessBottomTips.setCompoundDrawables(null, null, null, null);
            if (!TextUtils.isEmpty(message)) {
                livenessBottomTips.setText(message);
            }
        }
    }

    private void onRefreshSuccessView(boolean isShow) {
        if (livenessSuccessImage.getTag() == null) {
            Rect rect = livenessFaceRound.getFaceRoundRect();
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) livenessSuccessImage.getLayoutParams();
            rlp.setMargins(
                    rect.centerX() - (livenessSuccessImage.getWidth() / 2),
                    rect.top - (livenessSuccessImage.getHeight() / 2),
                    0,
                    0);
            livenessSuccessImage.setLayoutParams(rlp);
            livenessSuccessImage.setTag("setlayout");
        }
        livenessSuccessImage.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler1 != null) {
            handler1.removeCallbacksAndMessages(null);
            handler1 = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case RESULT_CODE_STARTCAMERA: {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!cameraAccepted) {

                }
            }
            break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_DELETEFACE:
                        showToast(getString(R.string.at_image_delete_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_ADDFACE:
                        showToast(getString(R.string.at_image_uploaded_success));
                        finish();
                        break;
                }
            } else {
//                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}