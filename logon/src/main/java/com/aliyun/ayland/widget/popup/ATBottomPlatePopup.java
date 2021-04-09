package com.aliyun.ayland.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATParkNumberBean;
import com.aliyun.ayland.data.db.ATParkNumberDao;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class ATBottomPlatePopup extends ATBasePopupWindow {
    private Context mContext;
    private View popupView;
    private final ATParkNumberDao mATParkNumberDao;
    private int currentPosition;

    public ATBottomPlatePopup(Context context) {
        super(context);
        mContext = context;
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);

        ImageView mImgArrow = (ImageView) findViewById(R.id.img_arrow);
        mImgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TagFlowLayout mFlowlayout = (TagFlowLayout) findViewById(R.id.flowlayout);
        mATParkNumberDao = new ATParkNumberDao(mContext);
        List<ATParkNumberBean> mParkNumberList = mATParkNumberDao.getAll();
        mFlowlayout.setAdapter(new TagAdapter(mParkNumberList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                View view = mInflater.inflate(R.layout.at_item_parkname_flowlayout, mFlowlayout, false);
                TextView tv = view.findViewById(R.id.tv_text);
                tv.setText(((ATParkNumberBean) o).getPark_number());
                if (position == currentPosition) {
                    tv.setTextColor(Color.WHITE);
                    tv.setBackground(mContext.getResources().getDrawable(R.drawable.shape_park_nu_pre));
                }
                ATAutoUtils.autoSize(view);
                return view;
            }
        });

        mFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                currentPosition = position;
                dismiss();
                mFlowlayout.getAdapter().notifyDataChanged();
                mOnItemClickListener.onItemClick(view, position);
                return true;
            }
        });
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

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.at_bottom_plate_popup, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private OnPopupItemClickListener mOnItemClickListener = null;

    public static interface OnPopupItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnPopupItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}