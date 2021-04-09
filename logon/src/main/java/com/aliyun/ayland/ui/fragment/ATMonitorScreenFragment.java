package com.aliyun.ayland.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.service.ATRecordService;
import com.aliyun.ayland.widget.ATWaveHelper;
import com.aliyun.ayland.widget.ATWaveView;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercom;
import com.aliyun.iotx.linkvisual.media.audio.audiotrack.SimpleStreamAudioTrack;
import com.aliyun.iotx.linkvisual.media.audio.record.AudioRecordListener;
import com.aliyun.iotx.linkvisual.media.audio.record.SimpleAudioRecord;
import com.aliyun.iotx.linkvisual.media.video.player.LivePlayer;
import com.anthouse.xuhui.R;
import com.google.android.exoplayer2.Player;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import androidx.annotation.RequiresApi;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;

/**
 * Created by fr on 2017/12/19.
 */
public class ATMonitorScreenFragment extends ATBaseFragment implements ATMainContract.View {
    private String TAG = "ATMonitorScreenFragment";
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 0x1001;
    private static final int PERMISSION_RECORD_AUDIO = 0x1002;
    public static final int REQUEST_CODE_RECORD = 0x1003;
    private static final int MSG_RECORD_VIDEO = 0x1004;
    private static final int MSG_HEARTBEAT = 0x1005;
    private static final int MSG_ADD_MONITOR_RECORD = 0x1006;
    private static final int MSG_STOP_ANIMATION = 0x1007;
    private Activity mContext;
    private RelativeLayout viewgroupLoading, rlTop, rlBottom, rlBottomH;
    private TextView tvVideoRecord, tvError;
    private ATWaveView waveView;
    private GLSurfaceView gLSurfaceView;
    private ImageView ivShot, ivShotH, imgShot, ivRecord, ivRecordH, ivMute, ivMuteH, imgFullScreen, imgBack;
    private LivePlayer mPlayer;
    private ATRecordService mATRecordService;
    private MediaProjectionManager projectionManager;
    private boolean startFlag, isPlaySound, isFullScreen;
    private int recordTime;
    private Handler handler;
    private SimpleAudioRecord mAudioRecord;
    private BlockingQueue<byte[]> audioTrackQueue = new LinkedBlockingQueue();
    private SimpleStreamAudioTrack simpleStreamAudioTrack;
    private ATMainContract.Presenter mPresenter;
    private String mIotId, monitorId, type = "0";
    private ATWaveHelper mATWaveHelper;
    public int status = 0;
    private String path = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
        mContext.startService(new Intent(mContext, ATRecordService.class));
        handler = new Handler(mContext.getMainLooper()) {
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_STOP_ANIMATION:
                        viewgroupLoading.setVisibility(View.GONE);
                        break;
                    case MSG_ADD_MONITOR_RECORD:
                        if (TextUtils.isEmpty(monitorId)) {
                            addMonitorRecord();
                        } else {
                            handler.sendEmptyMessageDelayed(MSG_HEARTBEAT, 5000);
                        }
                        break;
                    case MSG_HEARTBEAT:
                        monitorHeartBeat();
                        handler.sendEmptyMessageDelayed(MSG_HEARTBEAT, 10000);
                        break;
                    case MSG_RECORD_VIDEO:
                        recordTime = msg.arg1;
                        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
                        String format = sdf.format(new Date(recordTime * 1000));
                        tvVideoRecord.setText(format);
                        handler.removeMessages(MSG_RECORD_VIDEO);
                        if (startFlag) {
                            Message message = handler.obtainMessage();
                            message.arg1 = ++recordTime;
                            message.what = MSG_RECORD_VIDEO;
                            handler.sendMessageDelayed(message, 1000);
                        } else {
                            recordTime = 0;
                            showToast(getString(R.string.at_the_video_has_been_saved));
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_monitor_screen;
    }

    @Override
    protected void findView(View view) {
        waveView = view.findViewById(R.id.waveView);
        viewgroupLoading = view.findViewById(R.id.viewgroup_loading);
        rlTop = view.findViewById(R.id.rl_top);
        rlBottom = view.findViewById(R.id.rl_bottom);
        rlBottomH = view.findViewById(R.id.rl_bottom_h);
        tvVideoRecord = view.findViewById(R.id.tv_video_record);
        tvError = view.findViewById(R.id.tv_error);
        gLSurfaceView = view.findViewById(R.id.gLSurfaceView);
        ivShot = view.findViewById(R.id.iv_shot);
        imgShot = view.findViewById(R.id.img_shot);
        ivShotH = view.findViewById(R.id.iv_shot_h);
        ivRecord = view.findViewById(R.id.iv_record);
        ivRecordH = view.findViewById(R.id.iv_record_h);
        ivMute = view.findViewById(R.id.iv_mute);
        ivMuteH = view.findViewById(R.id.iv_mute_h);
        imgFullScreen = view.findViewById(R.id.img_full_screen);
        imgBack = view.findViewById(R.id.img_back);
    }

    private void monitorHeartBeat() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("monitorId", monitorId);
        jsonObject.put("type", type);
        mPresenter.request(ATConstants.Config.SERVER_URL_MONITORHEARTBEAT, jsonObject);
    }

    private void addMonitorRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotId", mIotId);
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDMONITORRECORD, jsonObject);
    }

//    private void queryLiveStreaming() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("iotId",mIotId);
//        Log.e("aaaa","iotid" + mIotId);
//        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING, jsonObject);
//    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            projectionManager = (MediaProjectionManager) mContext.getSystemService(MEDIA_PROJECTION_SERVICE);
        }

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
        }

        mContext.bindService(new Intent(mContext, ATRecordService.class), connection, BIND_AUTO_CREATE);
        mIotId = mContext.getIntent().getStringExtra("iotId");
        path = mContext.getIntent().getStringExtra("path");

        waveView.setWaveColor(Color.parseColor("#D2B48C"), Color.parseColor("#D2B48C"));
        waveView.setBorder(8, Color.WHITE);
        waveView.setShapeType(ATWaveView.ShapeType.CIRCLE);
        mATWaveHelper = new ATWaveHelper(waveView);

        Log.e("init: ", ATConstants.Config.APPKEY+"_"+ATGlobalApplication.getAuthCode());
        mPlayer = new LivePlayer(ATGlobalApplication.getContext(), ATConstants.Config.APPKEY, ATGlobalApplication.getAuthCode());
        mPlayer = new LivePlayer();
        mPlayer.setSurfaceView(gLSurfaceView);

        mPlayer.setOnPlayerStateChangedListener(playerState -> {
            switch (playerState) {
                case Player.STATE_BUFFERING:
                    //播放器正在缓冲, 当前的位置还不可以播放
                    mATWaveHelper.playNext();
                    handler.sendEmptyMessageDelayed(MSG_STOP_ANIMATION, 5000);
                    break;
                case Player.STATE_IDLE:
                    //播放器没有任何内容播放时的状态
                    break;
                case Player.STATE_READY:
                    break;
                case Player.STATE_ENDED:
                    break;
                default:
                    break;
            }
        });
        // 设置错误监听
        mPlayer.setOnErrorListener(exception -> {
            Log.e("onError: ", exception.getCode() + exception.getMessage());
            tvError.setText(exception.getMessage());
            handler.removeMessages(MSG_HEARTBEAT);
            mATWaveHelper.cancel();
            viewgroupLoading.setVisibility(View.GONE);
        });
        if (!TextUtils.isEmpty(path)) {
            mPlayer.setDataSource(path);
        }
        if (TextUtils.isEmpty(path)) {
            // 设置iotId.设置IPC直播数据源.
            mPlayer.setIPCLiveDataSource(mIotId, 0, true, 0, true);
        }
        // 设置iotId.设置IPC直播数据源.
//        mPlayer.setIPCLiveDataSource(mIotId, 0, true, 0, true);
        // 设置rtmp地址.
        //  mPlayer.setDataSource("rtmp://live.hkstv.hk.lxdns.com/live/hks2");
        // 设置数据源就绪监听器
        mPlayer.setOnPreparedListener(() -> {
            // 数据源就绪后开始播放
            mPlayer.start();
            viewgroupLoading.setVisibility(View.VISIBLE);
            monitorId = "";
            addMonitorRecord();
            handler.sendEmptyMessageDelayed(MSG_ADD_MONITOR_RECORD, 5000);
        });
        mPlayer.prepare();
        mATWaveHelper.start();
//        tvWatermark.setText(ATGlobalApplication.getAccount());
        ivShot.setOnClickListener((v) -> {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else {
                imgShot.setImageBitmap(mPlayer.snapShot());
                imgShot.setVisibility(View.VISIBLE);
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    saveBitmap(captureScreen());
                    imgShot.setVisibility(View.GONE);
                    showToast(getString(R.string.at_picture_saved));
                }, 300);
            }
        });
        ivShotH.setOnClickListener((v) -> {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else {
                imgShot.setImageBitmap(mPlayer.snapShot());
                imgShot.setVisibility(View.VISIBLE);
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    saveBitmap(captureScreen());
                    imgShot.setVisibility(View.GONE);
                    showToast(getString(R.string.at_picture_saved));
                }, 300);
            }
        });

        LiveIntercom liveIntercom = new LiveIntercom();
        liveIntercom.setOnErrorListener(error -> {
            error.printStackTrace();
            mAudioRecord.stop();
            liveIntercom.stop();
            if (simpleStreamAudioTrack != null) {
                simpleStreamAudioTrack.stop();
            }
        });
        liveIntercom.setOnTalkReadyListener(() -> {
        });

        liveIntercom.setOnAudioParamsChangeListener(audioParams -> {
            // 收到对端发送的音频参数，用于初始化AuudioTrack
            if (simpleStreamAudioTrack != null) {
                simpleStreamAudioTrack.release();
                audioTrackQueue.clear();
            }
            simpleStreamAudioTrack = new SimpleStreamAudioTrack(audioParams, AudioManager.STREAM_MUSIC,
                    audioTrackQueue);
            simpleStreamAudioTrack.start();
        });
        liveIntercom.setOnAudioBufferReceiveListener((bytes, i) -> {
            //收到对端发过来的音频数据, 送入播放器播放
            audioTrackQueue.add(bytes);
        });
        mAudioRecord = new SimpleAudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, AudioParams.AUDIOPARAM_MONO_8K_PCM);
        mAudioRecord.setAudioRecordListener(new AudioRecordListener() {
            @Override
            public void onRecordStart() {
                Log.d(TAG, "onRecordStart");
                // 录音开始后，再启动云对讲，指定SDK将音频数据转成G711A格式后再发送
                liveIntercom.start(mIotId, AudioParams.AUDIOPARAM_MONO_8K_G711A);
            }

            @Override
            public void onRecordEnd() {
                Log.d(TAG, "onRecordEnd");
                // 录音结束时, 关闭云对讲
                liveIntercom.stop();
            }

            @Override
            public void onBufferReceived(byte[] buffer, int offset, int size) {
                Log.d(TAG, "onBufferReceived:" + size);
                // 收到录音机PCM数据，调用云对讲接口发送给对端
                liveIntercom.sendAudioBuffer(buffer, offset, size);
            }

            @Override
            public void onError(int error, String message) {
                Log.e(TAG, "onError:" + error + message);
                // 处理录音机错误，如停止云对讲、重置UI元素等
            }
        });
        new Thread(mAudioRecord::start).start();

        ivRecord.setOnClickListener((v) -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                showToast(getString(R.string.at_version_too_low));
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
            } else {
                if (mATRecordService.isRunning()) {
                    mATRecordService.stopRecord();
                    startFlag = false;
                    tvVideoRecord.setVisibility(View.INVISIBLE);
                    ivRecord.setImageResource(R.drawable.at_village_videotape_light);
                    ivRecordH.setImageResource(R.drawable.at_village_videotape_light);
                } else {
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    mContext.startActivityForResult(captureIntent, REQUEST_CODE_RECORD);
                    tvVideoRecord.setText("00:00");
                }
            }
        });

        ivRecordH.setOnClickListener((v) -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                showToast(getString(R.string.at_version_too_low));
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else if (ContextCompat.checkSelfPermission(Objects.requireNonNull(mContext), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
            } else {
                if (mATRecordService.isRunning()) {
                    mATRecordService.stopRecord();
                    startFlag = false;
                    tvVideoRecord.setVisibility(View.INVISIBLE);
                    ivRecord.setImageResource(R.drawable.at_village_videotape_light);
                    ivRecordH.setImageResource(R.drawable.at_village_videotape_light);
                } else {
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    mContext.startActivityForResult(captureIntent, REQUEST_CODE_RECORD);
                    tvVideoRecord.setText("00:00");
                }
            }
        });
        ivMute.setOnClickListener((v) -> {
            ivMute.setImageResource(isPlaySound ? R.drawable.at_village_mute_off : R.drawable.at_village_mute_on);
            mPlayer.setVolume(isPlaySound ? 1 : 0);
            if (simpleStreamAudioTrack != null)
                simpleStreamAudioTrack.setVolume(isPlaySound ? 1 : 0);
            isPlaySound = !isPlaySound;
        });
        ivMuteH.setOnClickListener((v) -> {
            ivMuteH.setImageResource(isPlaySound ? R.drawable.at_village_mute_off : R.drawable.at_village_mute_on);
            mPlayer.setVolume(isPlaySound ? 1 : 0);
            if (simpleStreamAudioTrack != null)
                simpleStreamAudioTrack.setVolume(isPlaySound ? 1 : 0);
            isPlaySound = !isPlaySound;
        });
        imgFullScreen.setOnClickListener((v) -> {
            isFullScreen = !isFullScreen;
            setFullscreen();
        });
        imgBack.setOnClickListener((v) -> {
            isFullScreen = !isFullScreen;
            setFullscreen();
        });
    }

    public void setFullscreen() {
        status++;
        int screenOrientation = isFullScreen ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        View container = mContext.findViewById(R.id.rl_content);
        if (isFullScreen) {
//            status = "1";
//            EventBus.getDefault().post(status);
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            container.setLayoutParams(params);

            ViewGroup.LayoutParams glparams = gLSurfaceView.getLayoutParams();
            glparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            glparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            gLSurfaceView.setLayoutParams(glparams);


            rlTop.setVisibility(View.VISIBLE);
            rlBottom.setVisibility(View.GONE);
            rlBottomH.setVisibility(View.VISIBLE);
            imgFullScreen.setVisibility(View.GONE);

            mContext.findViewById(R.id.titlebar).setVisibility(View.GONE);
            mContext.findViewById(R.id.tabs).setVisibility(View.GONE);
            mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mContext.setRequestedOrientation(screenOrientation);
        } else {
//            status = "2";
//            EventBus.getDefault().post(status);
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            container.setLayoutParams(params);

            ViewGroup.LayoutParams glparams = gLSurfaceView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            gLSurfaceView.setLayoutParams(glparams);

            rlBottom.setVisibility(View.VISIBLE);
            rlBottomH.setVisibility(View.GONE);
            imgFullScreen.setVisibility(View.VISIBLE);
            mContext.findViewById(R.id.titlebar).setVisibility(View.VISIBLE);
            mContext.findViewById(R.id.tabs).setVisibility(View.VISIBLE);
            rlTop.setVisibility(View.GONE);

            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mContext.setRequestedOrientation(screenOrientation);
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            ATRecordService.RecordBinder binder = (ATRecordService.RecordBinder) service;
            mATRecordService = binder.getRecordService();
            mATRecordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @SuppressLint("NewApi")
    private Bitmap captureScreen() {
        View cv = Objects.requireNonNull(mContext).getWindow().getDecorView();
        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        bmp.setHasAlpha(false);
        bmp.prepareToDraw();
        return bmp;
    }


    public void saveBitmap(Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "AliScreenRecord" + "/");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //根据当前时间生成图片的名称
        String fileName = "/" + "小区摄像头_" + System.currentTimeMillis() + "_" + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
        mAudioRecord.stop();
        mContext.unbindService(connection);
        handler.removeMessages(MSG_ADD_MONITOR_RECORD);
        handler.removeMessages(MSG_HEARTBEAT);
        handler.removeMessages(MSG_RECORD_VIDEO);
        handler.removeCallbacksAndMessages(null);
        type = "1";
        //   monitorHeartBeat();
    }

    @Override
    public void onResume() {
        super.onResume();
        gLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gLSurfaceView.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE || requestCode == PERMISSION_RECORD_AUDIO) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast("开启权限后方可进行截屏和录屏操作");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_RECORD) {
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);

            ivRecord.setImageResource(R.drawable.at_village_videotape_dark);
            ivRecordH.setImageResource(R.drawable.at_village_videotape_dark);

            mATRecordService.setMediaProject(mediaProjection);
            mATRecordService.startRecord("小区摄像头");
            tvVideoRecord.setVisibility(View.VISIBLE);
            recordTime = 0;
            startFlag = true;
            Message message = handler.obtainMessage();
            message.arg1 = recordTime;
            message.what = MSG_RECORD_VIDEO;
            handler.sendMessage(message);
        }
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_MONITORHEARTBEAT:

                        break;
                    case ATConstants.Config.SERVER_URL_ADDMONITORRECORD:
                        monitorId = jsonResult.getString("monitorId");
                        break;
//                    case ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING:
//                        path = jsonResult.getString("data");
//                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}