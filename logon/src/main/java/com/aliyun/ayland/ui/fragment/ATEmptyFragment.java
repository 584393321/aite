package com.aliyun.ayland.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.utils.ATSDKUtils;
import com.anthouse.xuhui.R;

public class ATEmptyFragment extends ATBaseFragment {
    private LinearLayout llContent ;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_empty;
    }

    @Override
    protected void findView(View view) {
        llContent = view.findViewById(R.id.ll_content);
        init();
    }

    private void init() {
        llContent.setOnClickListener(v -> {
            ATSDKUtils.getInstance(getActivity()).shortcutList((list, atOnRVItemClickListener) -> Log.e("onCallBack: ", list.toString()));
            ATSDKUtils.getInstance(getContext()).getQrcode();
        });
    }

    @Override
    protected void initPresenter() {

    }
}