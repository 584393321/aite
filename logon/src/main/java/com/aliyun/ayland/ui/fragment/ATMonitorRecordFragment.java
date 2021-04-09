package com.aliyun.ayland.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.data.ATMediaBean;
import com.aliyun.ayland.ui.adapter.ATSaveRecordRVAdapter;
import com.anthouse.xuhui.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ATMonitorRecordFragment extends ATBaseFragment implements OnRefreshListener {
    private ATSaveRecordRVAdapter mATSaveRecordRVAdapter;
    private List<ATMediaBean> list = new ArrayList<>();
    private Dialog dialog;
    private int deletePosition;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout llContent;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_monitor_record;
    }

    @Override
    protected void findView(View view) {
        llContent = view.findViewById(R.id.ll_content);
        recyclerView = view.findViewById(R.id.recyclerView);
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        init();
    }

    /**
     * 获取文件
     * @return
     */
    private List<ATMediaBean> getVideoFile() {
        List<ATMediaBean> list = new ArrayList<>();
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "AliScreenRecord" + "/");
        appDir.listFiles(file -> {
            String name = file.getName();
            int i = name.indexOf('.');
            if (i != -1) {
                name = name.substring(i);
                if (name.equalsIgnoreCase(".jpg")) {
                    ATMediaBean media = new ATMediaBean();
                    file.getUsableSpace();
                    media.setMediaName(file.getName());
                    media.setPath(file.getAbsolutePath());
                    media.setType("image");
                    list.add(media);
                    return true;
                } else if (name.equalsIgnoreCase(".mp4")) {
                    ATMediaBean media = new ATMediaBean();
                    file.getUsableSpace();
                    media.setMediaName(file.getName());
                    media.setPath(file.getAbsolutePath());
                    media.setType("video");
                    list.add(media);
                    return true;
                }
                // 判断是不是目录
            } else if (file.isDirectory()) {
                getVideoFile();
            }
            return false;
        });
        return list;
    }

    private void init() {
        initDialog();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mATSaveRecordRVAdapter = new ATSaveRecordRVAdapter(getActivity());
        recyclerView.setAdapter(mATSaveRecordRVAdapter);
        mATSaveRecordRVAdapter.addItemLongClickListener(position -> {
            dialog.show();
            deletePosition = position;
        });

        smartRefreshLayout.setNoMoreData(true);
        smartRefreshLayout.setOnRefreshListener(this);
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(getActivity(), R.style.nameDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.at_dialog_normal, null, false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(getString(R.string.at_sure_delete_file));
        TextView tvRight = view.findViewById(R.id.tv_sure);
        tvRight.setText(getString(R.string.at_delete));
        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> dialog.dismiss());
        tvRight.setOnClickListener(view12 -> {
            // 通知图库更新
            deleteFile(list.get(deletePosition).getPath());

            dialog.dismiss();
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
        });
        dialog.setContentView(view);
    }

    public void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                showToast(getString(R.string.at_file_has_been_removed));
                onRefresh(smartRefreshLayout);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh(smartRefreshLayout);
    }

    public void onRefresh() {
        onRefresh(smartRefreshLayout);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        list.clear();
        for (int i = getVideoFile().size() - 1; i >= 0; i--) {
            list.add(getVideoFile().get(i));
        }
        mATSaveRecordRVAdapter.setList(list);
        if (list.size() > 0) {
            llContent.setVisibility(View.GONE);
        } else {
            llContent.setVisibility(View.VISIBLE);
        }
        refreshLayout.finishRefresh(500);
    }


    @Override
    protected void initPresenter() {

    }
}