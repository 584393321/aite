package com.aliyun.ayland.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATSceneDoTitle;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author guikong on 18/4/8.
 */

public class ATShortcutSeleteViewHolder extends ATSettableViewHolder {
    private RadioGroup radioGroup;
    private Activity context;
    private RadioButton rbFamily, rbPublic, rbScene;

    public ATShortcutSeleteViewHolder(View view, Activity context) {
        super(view);
        ATAutoUtils.autoSize(view);
        this.context = context;
        radioGroup = view.findViewById(R.id.radioGroup);
        rbFamily = view.findViewById(R.id.rb_family);
        rbPublic = view.findViewById(R.id.rb_public);
        rbScene = view.findViewById(R.id.rb_scene);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATSceneDoTitle))
            return;
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(rbFamily.getId() == checkedId){
                EventBus.getDefault().post(new ATEventInteger("ATHomeShortcutActivity", 0));
            }else if(rbPublic.getId() == checkedId){
                EventBus.getDefault().post(new ATEventInteger("ATHomeShortcutActivity", 1));
            }else if(rbScene.getId() == checkedId){
                EventBus.getDefault().post(new ATEventInteger("ATHomeShortcutActivity", 2));
            }
        });
    }
}
