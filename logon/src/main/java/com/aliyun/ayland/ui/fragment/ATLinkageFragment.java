package com.aliyun.ayland.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.data.ATEventClazz;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by fr on 2017/12/19.
 */
public class ATLinkageFragment extends ATBaseFragment {
    public static final int REQUEST_CODE_EDIT_LINKAGE = 0x1002;
    private ATLinkageFamilyFragment mATLinkageFamilyFragment;
    private ATMyTitleBar titlebar;
    private ATTestFragment mATTestFragment;
    private List<Fragment> mFragments;
    private Fragment mCurFragment, toFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage;
    }

    @Override
    protected void findView(View view) {
        titlebar = view.findViewById(R.id.titlebar);
        init();
    }

    @Override
    protected void initPresenter() {

    }

    private void init() {
        titlebar.setTitle("");
        titlebar.setTitleStrings(getString(R.string.at_recommend), getString(R.string.at_mine));
        titlebar.setRightBtTextImage(R.drawable.at_ioc_tianjia);
        titlebar.setCeneterClick(who -> {
            toFragment = mFragments.get(who);
            showFragment(mCurFragment, toFragment);
            mCurFragment = toFragment;
        });

        titlebar.setTitleBarClickBackListener(() -> EventBus.getDefault().post(new ATEventClazz("HomeATFragment")));
        titlebar.setRightClickListener(() -> {
            if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
                showToast(getString(R.string.at_can_not_create_scene));
                return;
            }
            startActivityForResult(new Intent(getActivity(), ATLinkageAddActivity.class), REQUEST_CODE_EDIT_LINKAGE);
        });
//        titlebar.setTitleBarClickBackListener(() -> {
////            startActivity(new Intent(getActivity(), LinkageAddConditionActivity.class));
//            Router.getInstance().toUrl(getContext(), "link://router/devicenotices");
//        });
        mATLinkageFamilyFragment = new ATLinkageFamilyFragment();
        mATTestFragment = new ATTestFragment();

        mFragments = new ArrayList<>();
        if (mFragments.size() == 0) {
            mFragments.add(mATLinkageFamilyFragment);
            mFragments.add(mATTestFragment);
        }
        mCurFragment = mFragments.get(0);
        replaceFragment(mCurFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment).commit();
    }

    private void showFragment(Fragment from, Fragment to) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.framelayout, to).commitAllowingStateLoss();
        } else {
            transaction.hide(from).show(to).commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_LINKAGE) {
            mATLinkageFamilyFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}