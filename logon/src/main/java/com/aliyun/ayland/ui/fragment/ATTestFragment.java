package com.aliyun.ayland.ui.fragment;

import android.view.View;

import com.aliyun.ayland.base.ATBaseFragment;
import com.anthouse.xuhui.R;


public class ATTestFragment extends ATBaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_test;
    }

    @Override
    protected void findView(View view) {
        init();
    }

    private void init() {
//        btnLogout.setOnClickListener(v -> {
//            LoginBusiness.logout(new ILogoutCallback() {
//                @Override
//                public void onLogoutSuccess() {
//                    getActivity().finish();
//                    LoginBusiness.login(new ILoginCallback() {
//                        @Override
//                        public void onLoginSuccess() {
//                            Log.i(TAG, "登录成功");
//                        }
//
//                        @Override
//                        public void onLoginFailed(int code, String error) {
//                            Log.i(TAG, "登录失败");
//                        }
//                    });
//                }
//                @Override
//                public void onLogoutFailed(int code, String error) {
//                }
//            });
//        });
    }

    @Override
    protected void initPresenter() {

    }
}