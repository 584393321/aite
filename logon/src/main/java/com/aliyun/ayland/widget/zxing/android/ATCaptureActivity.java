package com.aliyun.ayland.widget.zxing.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATPayResult;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.activity.ATDiscoveryDeviceActivity;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.ayland.widget.zxing.bean.ATZxingConfig;
import com.aliyun.ayland.widget.zxing.camera.ATCameraManager;
import com.aliyun.ayland.widget.zxing.common.ATConstant;
import com.aliyun.ayland.widget.zxing.decode.ATDecodeThread;
import com.aliyun.ayland.widget.zxing.utils.ATBeepManager;
import com.aliyun.ayland.widget.zxing.utils.ATCaptureActivityHandler;
import com.aliyun.ayland.widget.zxing.utils.ATImageUtil;
import com.aliyun.ayland.widget.zxing.utils.ATInactivityTimer;
import com.anthouse.xuhui.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

public class ATCaptureActivity extends ATBaseActivity implements SurfaceHolder.Callback, ATMainContract.View {
    private static final int REQUEST_CODE_SCAN_GALLERY = 1001;
    private static final int MSG_RESTART_PREVIEW = 1002;
    private static final int MSG_PAY_RESULT = 1003;
    private ATMainPresenter mPresenter;
    public ATZxingConfig config;
    private ATCameraManager mATCameraManager;
    private ATCaptureActivityHandler handler;
    private ATInactivityTimer mATInactivityTimer;
    private ATBeepManager mATBeepManager;
    private TranslateAnimation mAnimation;
    private boolean isHasSurface = false, isFromGYM;
    private Rect mCropRect = null;
    private Drawable mDrawable;
    private String mCurrentScanResult;
    private ProgressDialog mProgress;
    private Bitmap scanBitmap;
    private SurfaceView capturePreview;
    private ImageView captureScanLine;
    private RelativeLayout captureCropView, captureContainer;
    private TextView tvBack, tvRight, tvLamp;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 避免在子线程中改变了adapter中的数据
            switch (msg.what) {
                case MSG_RESTART_PREVIEW:
                    if (handler != null)
                        handler.restartPreviewAndDecode();
                    break;
                case 2:
//                    if (!sendSuccess) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(mCurrentScanResult);
//                            infoMap.put(jsonObject.getString("type"), jsonObject.getString("code"));
//                            errorMap.put(jsonObject.getString("type"), mCurrentScanResult);
//                            sanResultMap.put(jsonObject.getString("type"), mCurrentScanResult);
//                            List<String> info = new ArrayList<>(infoMap.values());
//                            mContext.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mScanResultInfoAdapter.setList(info);
//                                }
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (mIsTitle) {
//                            mHandler.removeMessages(MSG_RESTART_PREVIEW);
//                            mHandler.sendEmptyMessageDelayed(MSG_RESTART_PREVIEW, 2000);
//                        } else {
//                            setResult(RESULT_OK, new Intent().putExtra("info", "")
//                                    .putExtra("scan_result", mCurrentScanResult));
//                            finish();
//                        }
//                    }
                    break;
                case MSG_PAY_RESULT:
                    ATPayResult ATPayResult = new ATPayResult((Map<String, String>) msg.obj);
                    String resultInfo = ATPayResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = ATPayResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showToast(getString(R.string.at_appoint_success));
                        finish();
                    } else {
                        showToast(getString(R.string.at_appoint_failed));
                    }
                    closeBaseProgressDlg();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_scan;
    }

    @Override
    protected void findView() {
        tvBack = findViewById(R.id.tv_back);
        tvRight = findViewById(R.id.tv_right);
        tvLamp = findViewById(R.id.tv_lamp);
        captureCropView = findViewById(R.id.capture_crop_view);
        captureContainer = findViewById(R.id.capture_container);
        capturePreview = findViewById(R.id.capture_preview);
        captureScanLine = findViewById(R.id.capture_scan_line);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isFromGYM = getIntent().getBooleanExtra("isFromGYM", false);
        /*先获取配置信息*/
        try {
            config = (ATZxingConfig) getIntent().getExtras().get("zxingConfig");
        } catch (Exception e) {
            Log.i("config", e.toString());
        }

        if (config == null) {
            config = new ATZxingConfig();
        }
        init();
    }

    public Handler getHandler() {
        return handler;
    }

    public ATCameraManager getATCameraManager() {
        return mATCameraManager;
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        mATInactivityTimer.onActivity();
        mATBeepManager.playBeepSoundAndVibrate();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        mCurrentScanResult = rawResult.getText();
        bundle.putString("result", mCurrentScanResult);
        Log.e("handleDecode: ", mCurrentScanResult);
        if (TextUtils.isEmpty(mCurrentScanResult)) {
            showToast(getString(R.string.at_please_scan_again));
            finish();
        } else if (mCurrentScanResult.contains("type") && mCurrentScanResult.contains("village") && mCurrentScanResult.contains("buildingCode")) {
            runGymDevice();
        } else if (mCurrentScanResult.contains("Aite-TMtype")) {
            JSONObject jsonObject = JSONObject.parseObject(mCurrentScanResult);
            qrCodeLoginConfirm(jsonObject.getString("Aite-TMtype"));
        } else if (mCurrentScanResult.contains("https:") && mCurrentScanResult.contains("?") && mCurrentScanResult.contains("pk=")) {
            String productKey = "", deviceName = "";
            for (String s : mCurrentScanResult.split("\\?")[1].split("&")) {
                if ("pk".equals(s.split("=")[0])) {
                    productKey = s.split("=")[1];
                }
                if ("dn".equals(s.split("=")[0])) {
                    deviceName = s.split("=")[1];
                }
            }
            if (!TextUtils.isEmpty(productKey))
                startActivity(new Intent(this, ATDiscoveryDeviceActivity.class)
                        .putExtra("productKey", productKey).putExtra("deviceName", deviceName));
            finish();
        } else if (mCurrentScanResult.contains("payData")) {
            JSONObject jsonObject = JSONObject.parseObject(mCurrentScanResult);
            getPayData(jsonObject.getString("payData"));
        } else {
            showToast(getString(R.string.at_please_scan_again));
            finish();
        }
    }

    private void getPayData(String orderNo) {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo", orderNo);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETPAYDATA, jsonObject);
    }

    private void qrCodeLoginConfirm(String tmType) {
        showBaseProgressDlg();
        JSONObject jsonObject = JSONObject.parseObject(mCurrentScanResult);
        jsonObject.put("qrCodeAccessToken", tmType);
        jsonObject.put("username", ATGlobalApplication.getAccount());
        jsonObject.put("password", ATGlobalApplication.getPassword());
        mPresenter.request(ATConstants.Config.SERVER_URL_QRCODELOGINCONFIRM, jsonObject);
    }

    private void runGymDevice() {
        showBaseProgressDlg();
        JSONObject jsonObject = JSONObject.parseObject(mCurrentScanResult);
        jsonObject.remove("img_url");
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        Log.e("runGymDevice: ", jsonObject.toJSONString());
        mPresenter.request(ATConstants.Config.SERVER_URL_RUNGYMDEVICE, jsonObject);
    }

    private void init() {
        ATSystemStatusBarUtils.init(this, false);
        tvBack.setOnClickListener(view -> finish());
        tvRight.setOnClickListener(view -> {
            //打开手机中的相册
            Intent innerIntent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                innerIntent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            }
            innerIntent.setType("image/*");
            Intent wrapperIntent = Intent.createChooser(innerIntent, getString(R.string.at_select_qr_code_image));
            startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
//                startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
        });

        mATInactivityTimer = new ATInactivityTimer(this);
        mATBeepManager = new ATBeepManager(this);

        mAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        mAnimation.setDuration(2000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);

        tvLamp.setOnClickListener(view -> mATCameraManager.switchFlashLight(handler));
        tvLamp.setVisibility(config.isShowFlashLight() ? View.VISIBLE : View.GONE);
        /*有闪光灯就显示手电筒按钮  否则不显示*/
        tvLamp.setVisibility(isSupportCameraLedFlash(getPackageManager()) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    /**
     * @param pm
     * @return 是否有闪光灯
     */
    public boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param flashState 切换闪光灯图片
     */
    public void switchFlashImg(int flashState) {
        if (flashState == ATConstant.FLASH_OPEN) {
            mDrawable = getResources().getDrawable(R.drawable.at_ic_s_sys_guanbi);
            mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
            tvLamp.setCompoundDrawables(null, mDrawable, null, null);
            tvLamp.setCompoundDrawablePadding(10);
            tvLamp.setText(getResources().getString(R.string.at_touch_dark));
        } else {
            mDrawable = getResources().getDrawable(R.drawable.at_ic_s_sys_zhaoliang);
            mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
            tvLamp.setCompoundDrawables(null, mDrawable, null, null);
            tvLamp.setCompoundDrawablePadding(10);
            tvLamp.setText(getResources().getString(R.string.at_touch_light));
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (surfaceHolder != null) {
            if (!isHasSurface) {
                isHasSurface = true;
                initCamera(surfaceHolder);
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mATCameraManager.isOpen()) {
            return;
        }
        try {
            mATCameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new ATCaptureActivityHandler(this, mATCameraManager, ATDecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException | RuntimeException ioe) {
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.at_app_name));
        builder.setMessage(getString(R.string.at_camera_opening_error));
        builder.setPositiveButton(getString(R.string.at_sure), (dialog, which) -> finish());
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mATCameraManager.getCameraResolution().y;
        int cameraHeight = mATCameraManager.getCameraResolution().x;
        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        captureCropView.getLocationInWindow(location);
        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();
        int cropWidth = captureCropView.getWidth();
        int cropHeight = captureCropView.getHeight();
        /** 获取布局容器的宽高 */
        int containerWidth = captureContainer.getWidth();
        int containerHeight = captureContainer.getHeight();
        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;
        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;
        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 处理选择的图片
     *
     * @param data
     */
    private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        Log.e("handleAlbumPic: ", data.getData() + "---");
        Log.e("handleAlbumPic: ", data.toString() + "---");
//        String photo_path = ATUriUtil.getRealPathFromUri(ATCaptureActivity.this, data.getData());
        String photo_path = ATImageUtil.getImageAbsolutePath(this, data.getData());
        mProgress = new ProgressDialog(ATCaptureActivity.this);
        mProgress.setMessage(getString(R.string.at_scanning));
        mProgress.setCancelable(false);
        mProgress.show();
        runOnUiThread(() -> {
            mProgress.dismiss();
            Result result = scanningImage(photo_path);
            if (result != null) {
                Bundle bundle = new Bundle();
                bundle.putString(ATConstant.INTENT_EXTRA_KEY_QR_SCAN, result.getText());
                handleDecode(result, bundle);
            } else {
                showToast(getString(R.string.at_identificate_failure));
            }
        });
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        int[] data = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
        scanBitmap.getPixels(data, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            capturePreview.setVisibility(View.VISIBLE);
            captureContainer.setBackgroundColor(Color.TRANSPARENT);
            captureScanLine.startAnimation(mAnimation);
        }, 100);
        mATCameraManager = new ATCameraManager(getApplication());
        handler = null;
        if (isHasSurface) {
            initCamera(capturePreview.getHolder());
        } else {
            capturePreview.getHolder().addCallback(this);
        }
        mATInactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (captureScanLine != null)
            captureScanLine.clearAnimation();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        mATInactivityTimer.onPause();
        mATBeepManager.close();
        mATCameraManager.closeDriver();
        if (!isHasSurface) {
            capturePreview.getHolder().removeCallback(this);
        }

        switchFlashImg(ATConstant.FLASH_CLOSE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(MSG_RESTART_PREVIEW);
        mATInactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETPAYDATA:
                        String data = jsonResult.getString("data");
                        Thread payThread = new Thread(() -> {
                            PayTask alipay = new PayTask(this);
                            Map<String, String> result1 = alipay.payV2(data, true);
                            Message msg = new Message();
                            msg.what = MSG_PAY_RESULT;
                            msg.obj = result1;
                            mHandler.sendMessage(msg);
                        });
                        payThread.start();
                        break;
                    case ATConstants.Config.SERVER_URL_QRCODELOGINCONFIRM:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_tmall_wizard_login_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_RUNGYMDEVICE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_open_success));
//                        if (isFromGYM) {
//                            startActivity(new Intent(this, ATConvenientGYMActivity.class));
//                        } else {
                        finish();
//                        }
                        break;
                }
            } else {
                closeBaseProgressDlg();
                String message = getString(R.string.at_please_scan_again);
                if (jsonResult.has("message")) {
                    message = jsonResult.getString("message");
                }
                showToast(message);
                mHandler.removeMessages(MSG_RESTART_PREVIEW);
                mHandler.sendEmptyMessageDelayed(MSG_RESTART_PREVIEW, 2000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    handleAlbumPic(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
