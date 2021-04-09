package com.aliyun.ayland.widget.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.utils.ATImageUtil;
import com.aliyun.ayland.utils.ATToastUtils;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

import static com.aliyun.ayland.ui.activity.ATCommunityCreateActivity.REQUEST_CODE_CHOOSE_PICTRUE;

public class ATTakePicturePopup extends ATBasePopupWindow implements View.OnClickListener {
    private Activity mContext;
    private View popupView;

    public ATTakePicturePopup(Context context) {
        super(context);
        mContext = (Activity) context;
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);

        findViewById(R.id.tv_take_picture).setOnClickListener(this);
        findViewById(R.id.tv_my_photo).setOnClickListener(this);
        findViewById(R.id.tv_save_picture).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateAnimation(0, 250 * 2, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.at_popup_take_picture, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_take_picture) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            mContext.startActivityForResult(getImageByCamera, REQUEST_CODE_CHOOSE_PICTRUE);
            dismiss();
        } else if (i == R.id.tv_my_photo) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mContext.startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_CODE_CHOOSE_PICTRUE);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                mContext.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_CHOOSE_PICTRUE);
            }
            dismiss();
        } else if (i == R.id.tv_save_picture) {
//            if ("http://alisaas.atsmartlife.comnull".equals(ATGlobalApplication.getATLoginBean().getAvatarUrl())) {
//                ATImageUtil.saveImageToGallery(mContext, ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.at_pho_s_mine_touxiang_d)).getBitmap());
//                ATToastUtils.shortShow("图片已成功保存至本地相册");
//            } else {
//                ATImageUtil.donwloadImg(mContext, ATGlobalApplication.getATLoginBean().getAvatarUrl());
//            }
            dismiss();
        } else if (i == R.id.tv_cancel) {
            dismiss();
        }
    }
}