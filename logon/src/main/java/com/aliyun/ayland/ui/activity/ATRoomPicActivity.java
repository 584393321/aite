package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.ui.adapter.ATRoomPicRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

public class ATRoomPicActivity extends ATBaseActivity {
    private boolean create;
    private Dialog mDialogName;
    private String[] mTitles;
    private int current_position;
    private EditText mEdittext;
    private ATRoomPicRVAdapter mATRoomPicRVAdapter;
    private RecyclerView rvRoomPic;
    private TextView mTvCount;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_room_pic;
    }

    @Override
    protected void findView() {
        rvRoomPic = findViewById(R.id.rv_room_pic);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        create = !TextUtils.isEmpty(getIntent().getStringExtra("allDeviceData"));
        if (create) {
            initNameDialog();
        }

        mTitles = getResources().getStringArray(R.array.at_room_type);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvRoomPic.setLayoutManager(gridLayoutManager);
        mATRoomPicRVAdapter = new ATRoomPicRVAdapter(this, mTitles);
        rvRoomPic.setAdapter(mATRoomPicRVAdapter);
        mATRoomPicRVAdapter.setOnItemClickListener((view, position) -> {
            current_position = position;
            if (create) {
                mEdittext.setText(ATResourceUtils.getResIdByName(mTitles[position], ATResourceUtils.ResourceType.STRING));
                mEdittext.setSelection(mEdittext.getText().toString().length());
                mDialogName.show();
                mEdittext.requestFocus();
                mEdittext.performClick();
            } else {
                setResult(RESULT_OK, new Intent().putExtra("room_type", mTitles[position].replace("at_","")));
                finish();
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initNameDialog() {
        mDialogName = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_room_name, null, false);
        mEdittext = view.findViewById(R.id.edittext);
        mTvCount = view.findViewById(R.id.tv_count);
        mEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvCount.setText(String.format(getString(R.string.at_4), mEdittext.getText().toString().length()));
            }
        });
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.at_input_room_name1));
        view.findViewById(R.id.tv_cancel).setOnClickListener((v) -> {
            mATRoomPicRVAdapter.setSelectItem(-1);
            mDialogName.dismiss();
        });
        view.findViewById(R.id.tv_sure).setOnClickListener((v) -> {
            String room_name = mEdittext.getText().toString();
            if (TextUtils.isEmpty(room_name)) {
                showToast(getString(R.string.at_input_room_name));
            } else {
                mDialogName.dismiss();
                startActivity(getIntent().putExtra("room_type", mTitles[current_position].replace("at_",""))
                        .putExtra("room_name", room_name).setClass(this, ATEditRoomActivity.class));
                finish();
            }
        });
        mDialogName.setContentView(view);
    }
}