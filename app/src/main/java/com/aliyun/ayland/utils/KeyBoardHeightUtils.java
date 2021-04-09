package com.aliyun.ayland.utils;


import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class KeyBoardHeightUtils {
    private View mChildOfContent;//activity 的布局View
    private int usableHeightPrevious;//activity的View的可视高度
    private KeyBoardHigthListener keyBoardHigthListener;

    public static KeyBoardHeightUtils setKeyBoardHeigthListener(Activity activity, KeyBoardHigthListener keyBoardHigthListener) {
        return new KeyBoardHeightUtils(activity, keyBoardHigthListener);
    }

    public void removeKeyboardHeightListener() {
        if (mChildOfContent == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mChildOfContent.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
        } else {
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        }
    }

    private KeyBoardHeightUtils(final Activity activity, KeyBoardHigthListener keyBoardHigthListener) {
        this.keyBoardHigthListener = keyBoardHigthListener;
//获取安卓activity的最顶级控件
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);//
        mChildOfContent = content.getChildAt(0);
        //监听视图高度变化      mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
//点击父控件隐藏软键盘
        mChildOfContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.hideInputForce(activity);
            }
        });
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            possiblyResizeChildOfContent();
        }
    };


    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//为了去除无谓的干扰，限制下高度
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                keyBoardHigthListener.keyBoardHigthListener(usableHeightSansKeyboard - heightDifference, true, mChildOfContent);
            } else {
                keyBoardHigthListener.keyBoardHigthListener(usableHeightSansKeyboard, false, mChildOfContent);
            }
            usableHeightPrevious = usableHeightNow;
        }
    }
    //获取控件在屏幕的可视高度，就是界面除去了标题栏、除去了被软键盘挡住的部分，所剩下的矩形区域
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }


    public interface KeyBoardHigthListener {//这个回调类写的不好，请自行修改
        void keyBoardHigthListener(int heigth, boolean showKeyBoard, View contentView);
    }
}