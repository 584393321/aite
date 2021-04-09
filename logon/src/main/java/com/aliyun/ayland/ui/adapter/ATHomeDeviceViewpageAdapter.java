package com.aliyun.ayland.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ATHomeDeviceViewpageAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> listFragment;
    private int count = 1;

    public ATHomeDeviceViewpageAdapter(FragmentManager fm, List<Fragment> fragments, int count) {
        super(fm);
        this.listFragment = fragments;
        this.count = count;
    }

    //刷新数据的时候可以把FragmentTransaction 中不需要的fragment移除掉
    public void setList(List<Fragment> list, int count) {
        if(listFragment!=null){
            //  Log.i("lx","fragmentSize"+listFragment.size());
        }
        this.listFragment = list;
        this.count = count;
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        //  Log.i("lx","position"+position+"count"+count);
        return listFragment.get(position);
    }


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}