package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATParkNumberBean;
import com.aliyun.ayland.data.db.ATParkNumberDao;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.fragment.ATEmptyFragment;
import com.aliyun.ayland.ui.fragment.ATMyCarAddOneFragment;
import com.aliyun.ayland.ui.fragment.ATMyCarAddThreeFragment;
import com.aliyun.ayland.ui.fragment.ATMyCarAddTwoFragment;
import com.aliyun.ayland.utils.ATIdNumberUtil;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.baidu.idl.face.platform.utils.BitmapUtils;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATMyCarAddActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATMyCarAddOneFragment mATMyCarAddOneFragment;
    private ATMyCarAddTwoFragment mATMyCarAddTwoFragment;
    private ATMyCarAddThreeFragment mATMyCarAddThreeFragment;
    private List<Fragment> mFragments;
    private int nextStatus = 0;
    private ATMyTitleBar titlebar;
    private TextView tvTwo1, tvTwo2, tvThree1, tvThree2;
    private String carOwner, idNumber, phoneNumber, carNumber, carPark, carBuilding, carBrand, carColor, carType, carImage;
    private ATHouseBean mATHouseBean;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_add_car;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        tvTwo1 = findViewById(R.id.tv_two1);
        tvTwo2 = findViewById(R.id.tv_two2);
        tvThree1 = findViewById(R.id.tv_three1);
        tvThree2 = findViewById(R.id.tv_three2);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    public void addCar() {
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("mobile", phoneNumber);
        jsonObject.put("defaultParkId", mATMyCarAddTwoFragment.getDefaultParkId());
        jsonObject.put("plateNumber", carNumber);
        jsonObject.put("userName", carOwner);
        jsonObject.put("identityId", idNumber);
        jsonObject.put("carImage", carImage);
        jsonObject.put("color", carColor);
        jsonObject.put("carType", carType);
        jsonObject.put("brand", carBrand);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("buildingName", mATHouseBean.getHouseAddress());
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDCAR, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        mFragments = new ArrayList<>();
        if (mFragments.size() == 0) {
            mATMyCarAddOneFragment = new ATMyCarAddOneFragment();
            mATMyCarAddTwoFragment = new ATMyCarAddTwoFragment();
            mATMyCarAddThreeFragment = new ATMyCarAddThreeFragment();
            mFragments.add(mATMyCarAddOneFragment);
            mFragments.add(mATMyCarAddTwoFragment);
            mFragments.add(mATMyCarAddThreeFragment);
            mFragments.add(new ATEmptyFragment());
        }

        replaceFragment(mATMyCarAddOneFragment);
        titlebar.setSendText(getString(R.string.at_next));
        titlebar.setPublishClickListener(() -> {
            Log.e("init: ", "nextStatus---");
            if (nextStatus == 0) {
                showToast("请先上传照片");
            } else if (nextStatus == 1) {
                showFragment(mATMyCarAddOneFragment, mATMyCarAddTwoFragment);
                tvTwo1.setBackground(getResources().getDrawable(R.drawable.shape_circle_60px_fda448));
                tvTwo2.setTextColor(getResources().getColor(R.color._333333));
                nextStatus = 2;
            } else if (nextStatus == 2) {
                carOwner = mATMyCarAddTwoFragment.getCarOwner();
                if (TextUtils.isEmpty(carOwner)) {
                    showToast(getString(R.string.at_hint_car_owner));
                    return;
                }
                idNumber = mATMyCarAddTwoFragment.getIdNumber();
                if (new ATIdNumberUtil(idNumber).isCorrect() != 0) {
                    showToast("请输入正确身份证");
                    return;
                }
                phoneNumber = mATMyCarAddTwoFragment.getPhoneNumber();
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast(getString(R.string.at_text_import_phonenumber));
                    return;
                }
                carNumber = mATMyCarAddTwoFragment.getCarNumber();
                if (TextUtils.isEmpty(carNumber) || carNumber.equals("请输入访客车牌号")) {
                    showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                    return;
                }
                carNumber = mATMyCarAddTwoFragment.getCarNumber();
                if (getString(R.string.at_input_plate_number).equals(carNumber)) {
                    showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                    return;
                }
                if (!TextUtils.isEmpty(carNumber)) {
                    String firstString = carNumber.substring(0, 1);
                    if ("港".equals(firstString)) {
                        showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                        return;
                    }
                    //字母开头
                    Pattern patternA = Pattern.compile("^[A-Z]{1}");
                    //文字开头
                    Pattern pattern1 = Pattern.compile("^[\u4e00-\u9fff]{1}[A-Z][-]{1}[A-Z0-9]{4}[A-Z0-9\u4e00-\u9fff]{1}$");
                    //七位车牌号码，新能源车
                    Pattern pattern2 = Pattern.compile("^[\u4e00-\u9fff]{1}[A-Z][-]{1}[A-Z0-9]{5}[A-Z0-9\u4e00-\u9fff]{1}$");
                    Matcher mA = patternA.matcher(carNumber);
                    Matcher m1 = pattern1.matcher(carNumber);
                    Matcher m2 = pattern2.matcher(carNumber);
                    if (m1.matches() || m2.matches()) {
                        ATParkNumberDao mATParkNumberDao = new ATParkNumberDao(this);
                        ATParkNumberBean ATParkNumberBean = mATParkNumberDao.getByParkNumber(firstString);
                        ATParkNumberBean.setCreate_time(new Date().getTime());
                        mATParkNumberDao.update(ATParkNumberBean);
                    } else if (mA.matches()) {

                    } else {
                        showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                        return;
                    }
                } else {
                    showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                    return;
                }
                carPark = mATMyCarAddTwoFragment.getCarPark();
                if (getString(R.string.at_choose_car_park).equals(carPark)) {
                    showToast(getString(R.string.at_choose_car_park));
                    return;
                }
                carBuilding = mATMyCarAddTwoFragment.getCarBuilding();
                if (getString(R.string.at_choose_car_building).equals(carBuilding)) {
                    showToast(getString(R.string.at_choose_car_building));
                    return;
                }
                carBrand = mATMyCarAddTwoFragment.getCarBrand();
                if (TextUtils.isEmpty(carBrand)) {
                    showToast(getString(R.string.at_hint_car_brand));
                    return;
                }
                carColor = mATMyCarAddTwoFragment.getCarColor();
                if (TextUtils.isEmpty(carColor)) {
                    showToast(getString(R.string.at_hint_car_color));
                    return;
                }
                carType = mATMyCarAddTwoFragment.getCarType();
                if (TextUtils.isEmpty(carType)) {
                    showToast(getString(R.string.at_hint_car_type));
                    return;
                }
                addCar();
            } else {
                finish();
            }
        });
    }

    public void addCarSuccess() {
        showFragment(mATMyCarAddTwoFragment, mATMyCarAddThreeFragment);
        tvThree1.setBackground(getResources().getDrawable(R.drawable.shape_circle_60px_fda448));
        tvThree2.setTextColor(getResources().getColor(R.color._333333));
        titlebar.setSendText("完成");
        nextStatus = 3;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_add_car, fragment).commit();
    }

    private void showFragment(Fragment from, Fragment to) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.framelayout_add_car, to).commitAllowingStateLoss();
        } else {
            transaction.hide(from).show(to).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {
            try {
                Bitmap bitmap1 = com.aliyun.ayland.utils.BitmapUtils.saveBefore(data.getStringExtra("IntentKeyFilePath"));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 80, out);
                byte[] data1 = out.toByteArray();
                Bitmap bitmap = BitmapUtils.createBitmap(this, data1, 0);
                out.close();
                carImage = BitmapUtils.bitmapToJpegBase64(bitmap, 80);
                mATMyCarAddOneFragment.onActivityResult(requestCode, resultCode, data);
                nextStatus = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_ADDCAR:
                        showToast(ATMyCarAddActivity.this.getString(R.string.at_morning_model_success));
                        setResult(RESULT_OK);
                        tvThree1.setBackground(getResources().getDrawable(R.drawable.shape_circle_60px_fda448));
                        tvThree2.setTextColor(getResources().getColor(R.color._333333));
                        addCarSuccess();
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}