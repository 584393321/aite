package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.anthouse.xuhui.R;

import java.util.List;

public class ATAboutActivity extends ATBaseActivity {
    private TextView tvVersonName;
    private TextView tvEvaluate;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_about;
    }

    @Override
    protected void findView() {
        tvVersonName = findViewById(R.id.tv_verson_name);
        tvEvaluate = findViewById(R.id.tv_evaluate);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        String mVersionName = "";
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mVersionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersonName.setText(String.format(getString(R.string.at_version_), mVersionName));
        tvEvaluate.setOnClickListener(view -> launchAppDetail("com.anthouse.lgcs", "com.tencent.android.qqdownloader"));
    }

    public boolean isMobile_spExist() {
        PackageManager manager = this.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase("com.tencent.android.qqdownloader"))
                return true;
        }
        return false;
    }

    public void launchAppDetail(String appPkg, String marketPkg) {
        if (!isMobile_spExist()) {
            showToast(getString(R.string.at_please_install_first));
        }
        try {
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}