package com.aliyun.ayland.ui.fragment;

import android.view.View;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

public class ATMyCarAddThreeFragment extends ATBaseFragment {
    private ATMainPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_add_car_three;
    }

    @Override
    protected void findView(View view) {
        init();
    }

    private void init() {
    }

    @Override
    protected void initPresenter() {
        //    mPresenter = new ATMainPresenter((MainContract.View) this);
        //    mPresenter.install(getActivity());
    }
}