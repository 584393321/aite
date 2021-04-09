package com.aliyun.ayland.ui.activity;

import android.util.Log;
import android.view.View;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.utils.ATJZMediaIjkplayer;
import com.anthouse.xuhui.R;


import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class ATLocalVideoActivity extends ATBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_local_video;
    }

    @Override
    protected void findView() {
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        String url = getIntent().getStringExtra("url");
        Log.e("onCreate: ", url);
        JzvdStd myJzvdStd = findViewById(R.id.videoplayer);
        myJzvdStd.setMediaInterface(new ATJZMediaIjkplayer());
        myJzvdStd.setUp(url, "", JzvdStd.SCREEN_WINDOW_FULLSCREEN );
//        Glide.with(this).load(url).into(myJzvdStd.thumbImageView);
        myJzvdStd.startVideo();
        myJzvdStd.batteryLevel.setVisibility(View.GONE);
        myJzvdStd.videoCurrentTime.setVisibility(View.GONE);
        myJzvdStd.backButton.setOnClickListener(v -> finish());

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
