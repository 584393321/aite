package com.aliyun.ayland.ui.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATVisitorResultBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.utils.ATQRCodeUtil;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ATVisitorAppointResultActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1002;
    private ATMainPresenter mPresenter;
    private ATVisitorResultBean mVisitorReservateBean;
    private String baseString, qrcodeUrl;
    private static final String shareUri = "https://smarthome.cifi.com.cn/addfacewap/index.php/Main/visitorResult?id=";
    private RelativeLayout rlOffLine, rlHttp;
    private ImageView imgOffLine, img;
    private TextView tvOffLine, tvName, tvVisiteRoom, tvPhone, tvVisiteTime, tvLeaveTime, tvPlateNumber;
    private ATMyTitleBar titlebar;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_visitor_appoint_result;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        rlOffLine = findViewById(R.id.rl_off_line);
        rlHttp = findViewById(R.id.rl_http);
        imgOffLine = findViewById(R.id.img_off_line);
        img = findViewById(R.id.img);
        tvOffLine = findViewById(R.id.tv_off_line);
        tvName = findViewById(R.id.tv_name);
        tvVisiteRoom = findViewById(R.id.tv_visite_room);
        tvPhone = findViewById(R.id.tv_phone);
        tvVisiteTime = findViewById(R.id.tv_visite_time);
        tvLeaveTime = findViewById(R.id.tv_leave_time);
        tvPlateNumber = findViewById(R.id.tv_plate_number);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getVisitorReservation();
    }

    private void getVisitorReservationQRCode() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("visitorReservationId", getIntent().getStringExtra("id"));
        mPresenter.request(ATConstants.Config.SERVER_URL_GETVISITORRESERVATIONQRCODE, jsonObject);
    }

    private void getVisitorReservation() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getIntent().getStringExtra("id"));
        mPresenter.request(ATConstants.Config.SERVER_URL_GETVISITORRESERVATION, jsonObject);
    }

    private void init() {
        baseString = "mnt/sdcard/" + getApplicationInfo().packageName + "/";
        titlebar.setRightBtTextImage(R.drawable.at_icon_s_fkyy_fenxiang);
        titlebar.setRightButtonEnable(true);
        titlebar.setRightClickListener(() -> {
            if (getIntent().getStringExtra("id") == null) {
                showToast("复制失败，地址为空");
                return;
            }
            if(mVisitorReservateBean == null){
                getVisitorReservation();
                return;
            }
//            shareView(getBitmap());
            shareViewUri( mVisitorReservateBean.getFaceUrl());
        });

//        btnShare.setOnClickListener(view -> {
//            if (getIntent().getStringExtra("id") == null) {
//                showToast("复制失败，地址为空");
//                return;
//            }
//            //获取剪贴板管理器：
//            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData mClipData = ClipData.newPlainText("Label", shareUri + getIntent().getStringExtra("id"));
//            cm.setPrimaryClip(mClipData);
//            showToast("复制成功");
//        });

        rlOffLine.setOnClickListener(view -> {
            if (TextUtils.isEmpty(qrcodeUrl)) {
                getVisitorReservationQRCode();
            }
        });
    }

    private void shareView(String bitmapUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);
        if (resInfo.isEmpty()) {
            return;
        }
        List<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo resolveInfo : resInfo) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo.packageName.contains("com.tencent.mobileqq")) {
                if (resolveInfo.loadLabel(pm).toString().contains("QQ收藏")
                        || resolveInfo.loadLabel(pm).toString().contains("我的电脑")
                        || resolveInfo.loadLabel(pm).toString().contains("面对面快传")) {
                    continue;
                }
                Intent target = new Intent();
                target.setAction(Intent.ACTION_SEND);
                target.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                target.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                target.setType("image/*");
                targetIntents.add(new LabeledIntent(target, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
            }
            if (activityInfo.packageName.contains("com.tencent.mm")) {
                //过滤掉qq收藏
                if (resolveInfo.loadLabel(pm).toString().contains("微信收藏")
                        || resolveInfo.loadLabel(pm).toString().contains("朋友圈")) {
                    continue;
                }
                Intent target = new Intent();
                target.setAction(Intent.ACTION_SEND);
                target.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                target.putExtra(Intent.EXTRA_STREAM, getImageContentUri(this, new File(bitmapUri)));
                target.setType("image/*");
                targetIntents.add(new LabeledIntent(target, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
            }
        }
        if (targetIntents.size() <= 0) {
            return;
        }
        Intent chooser = Intent.createChooser(targetIntents.remove(targetIntents.size() - 1), "");
        if (chooser == null) return;
        LabeledIntent[] labeledIntents = targetIntents.toArray(new LabeledIntent[targetIntents.size()]);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, labeledIntents);
        startActivity(chooser);
    }

    private void shareViewUri(String bitmapUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, bitmapUri);

        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);
        if (resInfo.isEmpty()) {
            return;
        }
        List<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo resolveInfo : resInfo) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo.packageName.contains("com.tencent.mobileqq")) {
                if (resolveInfo.loadLabel(pm).toString().contains("QQ收藏")
                        || resolveInfo.loadLabel(pm).toString().contains("我的电脑")
                        || resolveInfo.loadLabel(pm).toString().contains("面对面快传")) {
                    continue;
                }
                Intent target = new Intent();
                target.setAction(Intent.ACTION_SEND);
                target.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                target.putExtra(Intent.EXTRA_TEXT, bitmapUri);

                target.setType("text/plain");
                targetIntents.add(new LabeledIntent(target, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
            }
            if (activityInfo.packageName.contains("com.tencent.mm")) {
                //过滤掉qq收藏
                if (resolveInfo.loadLabel(pm).toString().contains("微信收藏")
                        || resolveInfo.loadLabel(pm).toString().contains("朋友圈")) {
                    continue;
                }
                Intent target = new Intent();
                target.setAction(Intent.ACTION_SEND);
                target.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                target.putExtra(Intent.EXTRA_TEXT, bitmapUri);
                target.setType("text/plain");
                targetIntents.add(new LabeledIntent(target, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
            }
        }
        if (targetIntents.size() <= 0) {
            return;
        }
        Intent chooser = Intent.createChooser(targetIntents.remove(targetIntents.size() - 1), "");
        if (chooser == null) return;
        LabeledIntent[] labeledIntents = targetIntents.toArray(new LabeledIntent[targetIntents.size()]);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, labeledIntents);
        startActivity(chooser);
    }

    private String getBitmap() {
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        String filePath = null;
        if (bitmap != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                filePath = sdCardPath + File.separator + "screenshot.png";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
        return filePath;
    }

    private String getPathString() {
        String nowFilePath = baseString + System.currentTimeMillis() + ".jpg";
        File file = new File(baseString);
        if (!file.exists()) {
            file.mkdir();
        }
        return nowFilePath;
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETVISITORRESERVATIONQRCODE:
                        qrcodeUrl = jsonResult.has("data") ? jsonResult.getString("data") : "";
                        if (TextUtils.isEmpty(qrcodeUrl)) {
                            imgOffLine.setVisibility(View.VISIBLE);
                            tvOffLine.setVisibility(View.VISIBLE);
                            showToast("二维码走丢了，请稍后重试");
                        } else {
                            imgOffLine.setVisibility(View.GONE);
                            tvOffLine.setVisibility(View.GONE);
                            img.setImageBitmap(ATQRCodeUtil.createQRImage(mVisitorReservateBean.getQrcodeUrl(), ATAutoUtils.getPercentWidthSize(701)
                                    , ATAutoUtils.getPercentHeightSize(701), getPathString(), false, null));
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_GETVISITORRESERVATION:
                        mVisitorReservateBean = gson.fromJson(jsonResult.getString("data"), new TypeToken<ATVisitorResultBean>() {
                        }.getType());
                        qrcodeUrl = mVisitorReservateBean.getQrcodeUrl();
                        if (TextUtils.isEmpty(qrcodeUrl)) {
                            imgOffLine.setVisibility(View.VISIBLE);
                            tvOffLine.setVisibility(View.VISIBLE);
                        } else {
                            imgOffLine.setVisibility(View.GONE);
                            tvOffLine.setVisibility(View.GONE);
                            img.setImageBitmap(ATQRCodeUtil.createQRImage(mVisitorReservateBean.getQrcodeUrl(), ATAutoUtils.getPercentWidthSize(701)
                                    , ATAutoUtils.getPercentHeightSize(701), getPathString(), false, null));
                        }
                        tvName.setText(mVisitorReservateBean.getVisitorName());
                        tvVisiteRoom.setText(String.format(getString(R.string.at_visit_room_), mVisitorReservateBean.getVisitorHouse()));
                        tvVisiteTime.setText(String.format(getString(R.string.at_visite_time_), mVisitorReservateBean.getReservationStartTime()));
                        tvLeaveTime.setText(String.format(getString(R.string.at_leave_time1_), mVisitorReservateBean.getReservationEndTime()));
                        tvPlateNumber.setText(String.format(getString(R.string.at_plate_number1_),
                                TextUtils.isEmpty(mVisitorReservateBean.getCarNumber()) ? "无" :mVisitorReservateBean.getCarNumber()));
                        tvPhone.setText(String.format(getString(R.string.at_phone1_),
                                TextUtils.isEmpty(mVisitorReservateBean.getVisitorTel()) ? "无" : mVisitorReservateBean.getVisitorTel()));
                        if (mVisitorReservateBean.getHasCar() != -1) {
                            tvPlateNumber.setVisibility(View.VISIBLE);
//                                tvPlateNumber.setText(mJsonObject.getString("carNumber"));
                        }
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

    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;

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

}
