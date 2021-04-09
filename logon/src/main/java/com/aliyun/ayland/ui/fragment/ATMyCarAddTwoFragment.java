package com.aliyun.ayland.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATParkBean;
import com.aliyun.ayland.data.ATParkNumberBean;
import com.aliyun.ayland.data.db.ATParkNumberDao;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.popup.ATBottomPlatePopup;
import com.aliyun.ayland.widget.popup.ATPopupRvSlideFromBottom;
import com.aliyun.ayland.widget.popup.ATSlideFromBottomInputPopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ATMyCarAddTwoFragment extends ATBaseFragment implements ATMainContract.View {
    private EditText etCarOwner, etIdNumber, etPhoneNumber, etCarBrand, etCarColor, etCarType;
    private ATMainPresenter mPresenter;
    private ATParkNumberDao mATParkNumberDao;
    private List<ATParkNumberBean> mParkNumberList;
    private ATBottomPlatePopup mATBottomPlatePopup;
    private ATSlideFromBottomInputPopup mATSlideFromBottomInputPopup;
    private RelativeLayout rlPlateNumbers, rlCarPark, rlCarBuilding;
    private TextView tvPlateNumber, tvPlateNumbers, tvCarPark, tvCarBuilding;
    private int defaultParkId = -1;
    private ATPopupRvSlideFromBottom mATPopupRvSlideFromBottom;
    private List<ATParkBean> atParkBeanList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private ATHouseBean mATHouseBean;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_add_car_two;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        parkingList();
    }

    @Override
    protected void findView(View view) {
        rlPlateNumbers = view.findViewById(R.id.rl_plate_numbers);
        rlCarPark = view.findViewById(R.id.rl_car_park);
        rlCarBuilding = view.findViewById(R.id.rl_car_building);
        etCarOwner = view.findViewById(R.id.et_car_owner);
        etCarBrand = view.findViewById(R.id.et_car_brand);
        etCarColor = view.findViewById(R.id.et_car_color);
        etCarType = view.findViewById(R.id.et_car_type);
        etIdNumber = view.findViewById(R.id.et_id_number);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        tvPlateNumber = view.findViewById(R.id.tv_plate_number);
        tvCarPark = view.findViewById(R.id.tv_car_park);
        tvCarBuilding = view.findViewById(R.id.tv_car_building);
        tvPlateNumbers = view.findViewById(R.id.tv_plate_numbers);
        init();
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (mATHouseBean != null)
            tvCarBuilding.setText(mATHouseBean.getHouseAddress());

        mATParkNumberDao = new ATParkNumberDao(getContext());
        mParkNumberList = mATParkNumberDao.getAll();
        if (mParkNumberList.size() == 0)
            initLitepal();

        mATPopupRvSlideFromBottom = new ATPopupRvSlideFromBottom(getActivity(), getString(R.string.at_choose_car_park), position -> {
            defaultParkId = atParkBeanList.get(position).getId();
            tvCarPark.setText(atParkBeanList.get(position).getName());
        });
        rlCarPark.setOnClickListener(v -> {
            if (atParkBeanList == null) {
                parkingList();
            } else {
                mATPopupRvSlideFromBottom.showPopupWindow();
            }
        });

        mATBottomPlatePopup = new ATBottomPlatePopup(getActivity());
        mATBottomPlatePopup.setOnItemClickListener((view, position) -> {
            String park_number = mParkNumberList.get(position).getPark_number();
            updateLitepal(park_number);
            tvPlateNumber.setText(park_number);
            tvPlateNumbers.setText(park_number);
            tvPlateNumbers.setTextColor(getResources().getColor(R.color._333333));
            mATSlideFromBottomInputPopup.showPopupWindow();
        });

        mATSlideFromBottomInputPopup = new ATSlideFromBottomInputPopup(getActivity(), "ATMyCarAddTwoFragment");
        mATSlideFromBottomInputPopup.setOnItemClickListener((view, text) -> {
            String parkNumber = tvPlateNumbers.getText().toString();
            if (TextUtils.isEmpty(text)) {
                if (parkNumber.length() > 1) {
                    tvPlateNumbers.setText(parkNumber.substring(0, parkNumber.length() - 1));
                } else {
                    tvPlateNumbers.setText(getString(R.string.at_input_plate_number));
                    tvPlateNumbers.setTextColor(getResources().getColor(R.color._CCCCCC));
                    mATSlideFromBottomInputPopup.dismiss();
                    mATBottomPlatePopup.showPopupWindow();
                }
            } else {
                if (parkNumber.length() == 2)
                    parkNumber += "-";
                tvPlateNumbers.setText(parkNumber + text);
            }
        });

        tvPlateNumber.setText(mParkNumberList.get(0).getPark_number());
        tvPlateNumbers.setText(mParkNumberList.get(0).getPark_number());
        tvPlateNumbers.setTextColor(getResources().getColor(R.color._333333));

        tvPlateNumbers.setOnClickListener(view -> {
            if (getString(R.string.at_input_plate_number).equals(tvPlateNumbers.getText().toString()))
                mATBottomPlatePopup.showPopupWindow();
            else
                mATSlideFromBottomInputPopup.showPopupWindow();
        });

        rlPlateNumbers.setOnClickListener(view -> mATBottomPlatePopup.showPopupWindow());
    }

    public String getCarOwner() {
        return etCarOwner.getText().toString();
    }

    public String getIdNumber() {
        return etIdNumber.getText().toString();
    }

    public String getCarBrand() {
        return etCarBrand.getText().toString();
    }

    public String getCarColor() {
        return etCarColor.getText().toString();
    }

    public String getCarType() {
        return etCarType.getText().toString();
    }

    public String getCarNumber() {
        return tvPlateNumbers.getText().toString().contains("-") ? tvPlateNumbers.getText().toString().split("-")[0].substring(0, 2)
                + "-" + tvPlateNumbers.getText().toString().split("-")[1] : tvPlateNumbers.getText().toString();
    }

    public String getCarPark() {
        return tvCarPark.getText().toString();
    }

    public String getCarBuilding() {
        return tvCarBuilding.getText().toString();
    }

    public String getPhoneNumber() {
        return etPhoneNumber.getText().toString();
    }

    public int getDefaultParkId() {
        return defaultParkId;
    }

    private void updateLitepal(String park_number) {
        ATParkNumberBean ATParkNumberBean = mATParkNumberDao.getByParkNumber(park_number);
        ATParkNumberBean.setCreate_time(new Date().getTime());
        mATParkNumberDao.update(ATParkNumberBean);
    }

    private void initLitepal() {
        List<String> s = new ArrayList<>();
        s.add("新");
        s.add("宁");
        s.add("青");
        s.add("甘");
        s.add("陕");
        s.add("藏");
        s.add("云");
        s.add("贵");
        s.add("川");
        s.add("渝");
        s.add("琼");
        s.add("桂");
        s.add("晋");
        s.add("蒙");
        s.add("鄂");
        s.add("豫");
        s.add("鲁");
        s.add("冀");
        s.add("闽");
        s.add("皖");
        s.add("浙");
        s.add("苏");
        s.add("沪");
        s.add("黑");
        s.add("吉");
        s.add("辽");
        s.add("津");
        s.add("京");
        s.add("湘");
        s.add("粤");
        s.add("赣");
        // 添加用户数据
        for (int i = 0; i < s.size(); i++) {
            ATParkNumberBean ATParkNumberBean = new ATParkNumberBean();
            ATParkNumberBean.setCreate_time(new Date().getTime() + i * 10);
            ATParkNumberBean.setPark_number(s.get(i));
            mATParkNumberDao.add(ATParkNumberBean);
        }
        mParkNumberList = mATParkNumberDao.getAll();
    }

    public void parkingList() {
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_PARKINGLIST, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_PARKINGLIST.equals(url)) {
                    atParkBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATParkBean>>() {
                    }.getType());
                    if (atParkBeanList.size() > 0) {
                        stringList.clear();
                        for (ATParkBean atParkBean : atParkBeanList) {
                            stringList.add(atParkBean.getName());
                        }
                        tvCarPark.setText(atParkBeanList.get(0).getName());
                        defaultParkId = atParkBeanList.get(0).getId();
                        mATPopupRvSlideFromBottom.setList(stringList);
                    }
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}