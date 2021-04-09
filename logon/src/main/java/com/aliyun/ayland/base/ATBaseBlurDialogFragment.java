package com.aliyun.ayland.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.blur.ATSupportBlurDialogFragment;


/**
 * Created by fr on 2018/2/7.
 */

public abstract class ATBaseBlurDialogFragment extends ATSupportBlurDialogFragment {
    private ProgressDialog mWaitProgressDlg;
    protected abstract  int getLayoutId();

    protected abstract void findView(View view);

    protected abstract void initPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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

    public void closeBaseProgressDlg()
    {
        if(mWaitProgressDlg != null){
            mWaitProgressDlg.dismiss();
        }
    }

    public void showBaseProgressDlg(String msg)
    {
        if(mWaitProgressDlg == null){
            mWaitProgressDlg = new ProgressDialog(getActivity());
            mWaitProgressDlg.setCanceledOnTouchOutside(false);
        }else if(mWaitProgressDlg.isShowing()){
            return;
        }
        mWaitProgressDlg.setMessage(msg);
        mWaitProgressDlg.show();
    }
}
