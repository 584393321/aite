package com.aliyun.ayland.ui.activity;

import android.opengl.GLSurfaceView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.iotx.linkvisual.media.video.player.LivePlayer;
import com.anthouse.xuhui.R;
import com.google.android.exoplayer2.Player;

import org.json.JSONException;

public class ATLivePlayerActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private LivePlayer mPlayer;
    private GLSurfaceView gLSurfaceView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_live_player;
    }

    @Override
    protected void findView() {
        gLSurfaceView = findViewById(R.id.gLSurfaceView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void init() {
        String iotId = getIntent().getStringExtra("iotId");

        ATLoginBean ATLoginBean = ATGlobalApplication.getATLoginBean();
//        mPlayer = new LivePlayer(ATGlobalApplication.getContext(), ATConstants.Config.APPKEY, ATLoginBean.getAuthCode());

        mPlayer.setSurfaceView(gLSurfaceView);
        mPlayer.setOnPlayerStateChangedListener(playerState -> {
            switch (playerState) {
                case Player.STATE_BUFFERING:
                    //播放器正在缓冲, 当前的位置还不可以播放
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
        mPlayer.setOnErrorListener(exception -> showToast("errorcode: " + exception.getCode() + "\n" + exception.getMessage()));
        // 设置IPC直播数据源.
//        mPlayer.setIPCLiveDataSource(iotId, 0, false, 0, false);
        //设置rtmp地址.
        mPlayer.setDataSource("rtmp://live.hkstv.hk.lxdns.com/live/hks2");
        // 设置数据源就绪监听器
        mPlayer.setOnPreparedListener(() -> {
            // 数据源就绪后开始播放
            mPlayer.start();
        });
        mPlayer.prepare();
        //        mPlayer.snapShot();截图


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
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
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:

                        break;
                }
            } else {
                closeBaseProgressDlg();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
