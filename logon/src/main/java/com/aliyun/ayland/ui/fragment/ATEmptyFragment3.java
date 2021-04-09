package com.aliyun.ayland.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.aliyun.ayland.base.ATBaseFragment;
import com.anthouse.xuhui.R;

public class ATEmptyFragment3 extends ATBaseFragment {
    private ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_empty2;
    }

    @Override
    protected void findView(View view) {
        imageView = view.findViewById(R.id.imageView);
        init();
    }

    private void init() {
        imageView.setImageResource(R.drawable.at_neighbor_page);
    }

    @Override
    protected void initPresenter() {

    }
}