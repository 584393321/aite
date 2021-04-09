package com.aliyun.ayland.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import com.anthouse.xuhui.R;

/**
 * @author guikong on 18/4/8.
 */
public class ATLocalDeviceSearcherViewHolder extends ATSettableViewHolder {
    public ATLocalDeviceSearcherViewHolder(View view) {
        super(view);
        final TextView textView = view.findViewById(R.id.tv_search);
        final String text = textView.getText().toString();
        textView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            int index = 0;
            boolean visible = false;
            Runnable animationRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!visible) {
                        return;
                    }
                    index++;
                    if (index > 3) {
                        index = 0;
                    }
                    String currentText = text;
                    for (int i = 0; i < index; i++) {
                        currentText += ".";
                    }
                    textView.setText(currentText);
                    textView.postDelayed(
                            animationRunnable,
                            index == 3 ? 1000 : 500);
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                visible = true;
                index = 0;
                textView.postDelayed(animationRunnable, 500);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                visible = false;
            }

        });
    }

    @Override
    public void setData(Object object, int position, int count) {

    }
}
