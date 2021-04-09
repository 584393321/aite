/*
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.aliyun.ayland.widget.face;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.anthouse.xuhui.R;


/**
 * ATDefaultDialog
 * 描述:通用Dialog
 */
public class ATDefaultDialog extends Dialog {

    public ATDefaultDialog(Context context) {
        super(context);
    }

    public ATDefaultDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String negative;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negative = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negative = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ATDefaultDialog create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final ATDefaultDialog dialog = new ATDefaultDialog(context, R.style.nameDialog);
            View layout = inflater.inflate(R.layout.at_dialog_face, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.tv_title)).setText(title);

            if (negative != null) {
                ((TextView) layout.findViewById(R.id.tv_sure))
                        .setText(negative);
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.tv_sure)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.tv_sure).setVisibility(
                        View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.tv_content)).setText(message);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
