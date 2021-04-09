package com.aliyun.ayland.ui.activity;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
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
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.face.ATFaceLivenessExpActivity;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
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

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ATUserFaceLoggingActivity extends ATBaseActivity implements ATMainContract.View,
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        Camera.ErrorCallback,
        VolumeUtils.VolumeCallback,
        ILivenessStrategyCallback {
    private static final int RESULT_CODE_STARTCAMERA = 1001;
    private ATMainPresenter mPresenter;
    // 人脸信息
    private FaceConfig mFaceConfig;
    private ILivenessStrategy mILivenessStrategy;
    // 显示Size
    private Rect mPreviewRect = new Rect();
    private int mDisplayWidth = 0;
    private int mDisplayHeight = 0;
    private int mSurfaceWidth = 0;
    private int mSurfaceHeight = 0;
    private Drawable mTipsIcon;
    // 状态标识
    private volatile boolean mIsEnableSound = true;
    private HashMap<String, String> mBase64ImageMap = new HashMap<String, String>();
    private boolean mIsCreateSurface = false;
    protected boolean mIsCompletion = false;
    // 相机
    private Camera mCamera;
    private Camera.Parameters mCameraParam;
    private int mCameraId;
    private int mPreviewWidth;
    private int mPreviewHight;
    private int mPreviewDegree;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Intent mIntent;
    private boolean mRefresh;
    private String mResult;
    private ATMyTitleBar titlebar;
    private FrameLayout livenessSurfaceLayout;
    private LinearLayout livenessSurfaceOverlayLayout;
    private Button btnLogging;
    private ImageView imgResult, livenessSuccessImage;
    private TextView livenessBottomTips, livenessTopTips;
    private FaceDetectRoundView livenessFaceRound;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_user_face_logging;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        livenessSurfaceLayout = findViewById(R.id.liveness_surface_layout);
        livenessSurfaceOverlayLayout = findViewById(R.id.liveness_surface_overlay_layout);
        btnLogging = findViewById(R.id.btn_logging);
        imgResult = findViewById(R.id.img_result);
        livenessSuccessImage = findViewById(R.id.liveness_success_image);
        livenessFaceRound = findViewById(R.id.liveness_face_round);
        livenessTopTips = findViewById(R.id.liveness_top_tips);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getFace();
    }

    private void getFace() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userType", "OPEN");
        jsonObject.put("userId", ATGlobalApplication.getOpenId());
        jsonObject.put("imageFormat", "URL");
        mPresenter.request(ATConstants.Config.SERVER_URL_GETFACE, jsonObject);
    }

    private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            //提示用户开户权限   拍照和读写sd卡权限
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, perms, RESULT_CODE_STARTCAMERA);
        }
        mIntent = getIntent();
        mRefresh = mIntent.getBooleanExtra("refresh", false);

        FaceSDKResSettings.initializeResId();
        mFaceConfig = FaceSDKManager.getInstance().getFaceConfig();

        titlebar.setRightButtonText(getString(R.string.at_done));
//        titlebar.setRightBtTextImage(() -> {
//            if (!TextUtils.isEmpty(mResult)) {
//                showLoadingDialog(getString(R.string.deal_something), 12000);
//                if (mIsVip) {
//                    sqFacerecognizeSpecialRecord(mResult);
//                } else {
//                    sqFacerecognizeRecord(mResult);
//                }
//            }
//        });


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
        btnLogging.setOnClickListener(v -> {
            mSurfaceView.setVisibility(View.VISIBLE);
            imgResult.setVisibility(View.GONE);
            livenessSurfaceOverlayLayout.setVisibility(View.VISIBLE);
        });
        imgResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                mLivenesssurfaceOverlayLayout.setVisibility(View.VISIBLE);
                //                mSurfaceView.setVisibility(View.VISIBLE);
                //                mTvClickPic.setVisibility(View.VISIBLE);
                //                imgResult.setVisibility(View.GONE);
                //                mTvTip.setVisibility(View.GONE);
                //                mTvVipTip.setVisibility(View.GONE);
                //                mTvVipEquipment.setVisibility(View.GONE);
                //                mTitlebar.showRightButton(true);
                //                mTitlebar.setRightButtonText(getString(R.string.done));
                //
                //                startPreview();
                //                mIsCompletion = false;
                //                mFaceStateRVAdapter.setLists(new ArrayList<>());
                //                livenessBottomTips.setText(getString(R.string.getting_the_eigenvalue_));
                //                mTvClickPic.setText(getString(R.string.follow_the_instructions_));
                finish();
                mIntent.putExtra("refresh", true);
                mIntent.setClass(ATUserFaceLoggingActivity.this, ATFaceLivenessExpActivity.class);
                startActivity(mIntent);
            }
        });

//        mRvFaceState.setLayoutManager(new LinearLayoutManager(this));
//        mFaceStateRVAdapter = new FaceStateRVAdapter(this);
//        mRvFaceState.setAdapter(mFaceStateRVAdapter);
//        mRvFaceState.setNestedScrollingEnabled(false);

        if (mBase64ImageMap != null) {
            mBase64ImageMap.clear();
        }
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
        }

        if (mCamera == null) {
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

        //        List<Camera.Size> supportedPictureSizes = mCameraParam.getSupportedPictureSizes();
        //        Camera.Size size = supportedPictureSizes.get(0);
        //        parameters.setPictureSize(size.width, size.height);
        //        camera.setParameters(parameters);

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
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                    //                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    //                    stream.close();
                    //                    Bitmap bitmap = ATBitmapUtils.rotateBitmap(90, bmp);
                    //                    imgResult.setImageBitmap(bitmap);
                    byte[] bytes = stream.toByteArray();
                    String deviceModel = Build.MODEL;
                    float rotate = -90;
                    if ("Nexus 6".equals(deviceModel)) {
                        rotate = 90;
                    }
                    stream.close();
                    Bitmap bitmap2 = BitmapUtils.createBitmap(this, bytes, rotate);
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 0, (bitmap2.getHeight() - bitmap2.getWidth() * 4 / 3) / 2,
                            bitmap2.getWidth(), bitmap2.getWidth() * 4 / 3);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    byte[] data1 = out.toByteArray();
                    Bitmap bitmap = BitmapUtils.createBitmap(this, data1, 0);
                    out.close();

                    Log.e("createBitmap: ", bitmap.getWidth() + "--3--" + bitmap.getHeight());

                    bitmap = mirrorConvert(bitmap, 0);

                    int quality = 80;
                    //                    Log.e("onPreviewFrame: ", +"");  3168000 46101 50
                    if (bitmap.getByteCount() >= 3168000) {
                        quality = 40;
                    } else if (bitmap.getByteCount() >= 2668000) {
                        quality = 50;
                    } else if (bitmap.getByteCount() >= 2168000) {
                        quality = 60;
                    }
                    mResult = BitmapUtils.bitmapToJpegBase64(bitmap, 80);
                    imgResult.setImageBitmap(bitmap);
                    livenessBottomTips.setText(getString(R.string.at_click_right_top));
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
            livenessSurfaceOverlayLayout.setVisibility(View.GONE);
            titlebar.showRightButton(true);
            startPreview();
            mSurfaceView.setVisibility(View.GONE);
            imgResult.setVisibility(View.VISIBLE);
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
                livenessBottomTips.setText("");
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
                livenessBottomTips.setText(getString(R.string.at_getting_the_eigenvalue_));
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
                livenessTopTips.setCompoundDrawablePadding(15);
            }
            livenessTopTips.setBackgroundResource(R.drawable.bg_tips);
            livenessTopTips.setText(R.string.detect_standard);
            livenessTopTips.setCompoundDrawables(mTipsIcon, null, null, null);
        } else {
            livenessTopTips.setBackgroundResource(R.drawable.bg_tips_no);
            livenessTopTips.setCompoundDrawables(null, null, null, null);
            if (!TextUtils.isEmpty(message)) {
                livenessTopTips.setText(message);
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
//        if (handler1 != null) {
//            handler1.removeCallbacksAndMessages(null);
//            handler1 = null;
//        }
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETFACE:

                        break;
                }
            } else if ("2008".equals(jsonResult.getString("code"))) {
                livenessSurfaceOverlayLayout.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.GONE);
                livenessBottomTips.setText(getString(R.string.at_best_experience));
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (livenessTopTips != null) {
            livenessTopTips.setText(R.string.detect_face_in);
        }
        startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }
}
