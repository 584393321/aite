package com.aliyun.ayland.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.aliyun.ayland.interfaces.FragmentBackHandler;
import com.aliyun.ayland.utils.BackHandlerHelper;
import com.aliyun.ayland.utils.SystemStatusBarUtils;
import com.aliyun.ayland.utils.ToastUtils;
import com.anthouse.xuhui.R;
import com.google.gson.Gson;


public abstract class BaseFragment extends Fragment implements FragmentBackHandler {
    private ProgressDialog mWaitProgressDlg;
    protected abstract int getLayoutId();
    protected abstract void findView(View view);
    protected abstract void initPresenter();
    protected Gson gson = new Gson();
    protected InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        SystemStatusBarUtils.init(getActivity(), true);
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        initPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeBaseProgressDlg();
    }

    public void closeBaseProgressDlg() {
        if (mWaitProgressDlg != null) {
            mWaitProgressDlg.dismiss();
        }
    }

    public void showBaseProgressDlg() {
        showBaseProgressDlg(getString(R.string.at_loading));
    }

    public void showBaseProgressDlg(String msg) {
        if (mWaitProgressDlg == null) {
            mWaitProgressDlg = new ProgressDialog(getActivity());
            mWaitProgressDlg.setCanceledOnTouchOutside(true);
        } else if (mWaitProgressDlg.isShowing()) {
            return;
        }
        mWaitProgressDlg.setMessage(msg);
        mWaitProgressDlg.show();
    }

    protected void showToast(String msg) {
        ToastUtils.shortShow(msg);
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
