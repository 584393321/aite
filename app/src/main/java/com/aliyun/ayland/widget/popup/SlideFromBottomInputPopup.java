package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.aliyun.ayland.ui.adapter.TextRVAdapter;
import com.aliyun.ayland.widget.popup.base.BasePopupWindow;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class SlideFromBottomInputPopup extends BasePopupWindow {
    private View popupView;

    public SlideFromBottomInputPopup(Activity context, String clazz) {
        super(context);
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);
        List<String> parkNumList = new ArrayList<>();
        List<String> parkNumList1 = new ArrayList<>();
        List<String> parkNumList2 = new ArrayList<>();
        parkNumList.add("Q");
        parkNumList.add("W");
        parkNumList.add("E");
        parkNumList.add("R");
        parkNumList.add("T");
        parkNumList.add("Y");
        parkNumList.add("U");
        parkNumList.add("I");
        parkNumList.add("O");
        parkNumList.add("P");
        parkNumList.add("A");
        parkNumList.add("S");
        parkNumList.add("D");
        parkNumList.add("F");
        parkNumList.add("G");
        parkNumList.add("H");
        parkNumList.add("J");
        parkNumList.add("K");
        parkNumList.add("L");
        parkNumList.add("Z");
        parkNumList.add("X");
        parkNumList.add("C");
        parkNumList.add("V");
        parkNumList.add("B");
        parkNumList.add("N");
        parkNumList.add("M");
        parkNumList.add("港");
        parkNumList.add("澳");
        parkNumList.add("警");
        parkNumList.add("领");
        parkNumList.add("使");
        parkNumList.add("学");

        parkNumList1.add("0");
        parkNumList1.add("1");
        parkNumList1.add("2");
        parkNumList1.add("3");
        parkNumList1.add("4");
        parkNumList1.add("5");
        parkNumList1.add("6");

        parkNumList2.add("7");
        parkNumList2.add("8");
        parkNumList2.add("9");
        parkNumList2.add("·");
        parkNumList2.add("5");
        RecyclerView rvTop = (RecyclerView) findViewById(R.id.rv_top);
        RecyclerView rvCenter = (RecyclerView) findViewById(R.id.rv_center);
        RecyclerView rvBottom = (RecyclerView) findViewById(R.id.rv_bottom);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 8);
        rvTop.setLayoutManager(gridLayoutManager);
        TextRVAdapter mTextRVAdapter = new TextRVAdapter(context,parkNumList,false,clazz);
        mTextRVAdapter.setOnItemClickListener((view, text) -> mOnItemClickListener.onItemClick(view, text));
        rvTop.setAdapter(mTextRVAdapter);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, 7);
        rvCenter.setLayoutManager(gridLayoutManager1);
        TextRVAdapter mTextRVAdapter1 = new TextRVAdapter(context,parkNumList1,false,clazz);
        rvCenter.setAdapter(mTextRVAdapter1);
        mTextRVAdapter1.setOnItemClickListener((view, text) -> mOnItemClickListener.onItemClick(view, text));
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(context, 5);
        rvBottom.setLayoutManager(gridLayoutManager2);
        TextRVAdapter mTextRVAdapter2 = new TextRVAdapter(context,parkNumList2,true,clazz);
        rvBottom.setAdapter(mTextRVAdapter2);
        mTextRVAdapter2.setOnItemClickListener((view, text) -> mOnItemClickListener.onItemClick(view, text));
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.at_popup_slide_from_input_bottom, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private OnPopupItemClickListener mOnItemClickListener = null;

    public static interface OnPopupItemClickListener {
        void onItemClick(View view, String text);
    }

    public void setOnItemClickListener(OnPopupItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
