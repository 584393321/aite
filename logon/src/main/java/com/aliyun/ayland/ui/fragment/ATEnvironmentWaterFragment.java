package com.aliyun.ayland.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATEnvironmentWaterBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATEnvironmentWaterFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private int switchStatus = 0;
    private RelativeLayout rlWaterSwitch;
    private TextView tvWaterState, tvWaterOut, tvWaterFirst, tvWaterOne, tvWaterTwo, tvWaterThree;
//    private ATCircleWaveView imgWaterAmount;
    private ImageView imgWaterSwitch, imgWaterAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_environment_water;
    }

    @Override
    protected void findView(View view) {
        rlWaterSwitch = view.findViewById(R.id.rl_water_switch);
        tvWaterState = view.findViewById(R.id.tv_water_state);
        tvWaterOut = view.findViewById(R.id.tv_water_out);
        tvWaterFirst = view.findViewById(R.id.tv_water_first);
        tvWaterOne = view.findViewById(R.id.tv_water_one);
        tvWaterTwo = view.findViewById(R.id.tv_water_two);
        tvWaterThree = view.findViewById(R.id.tv_water_three);
        imgWaterAmount = view.findViewById(R.id.img_water_amount);
        imgWaterSwitch = view.findViewById(R.id.img_water_switch);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
    }

    private void init() {
//        imgWaterAmount.setProgress(40);
        rlWaterSwitch.setOnClickListener(view -> {
            if (switchStatus == 0) {
//                imgWaterAmount.setAlpha(0.4f);
                tvWaterState.setText(R.string.at_water_standby);
                //存放变暗的ui
                imgWaterSwitch.setImageResource(R.drawable.athome_hjsj_ico_daiji);
                imgWaterAmount.setImageResource(R.drawable.athome_hjsj_bg_b);
                switchStatus = 1;
            } else {
//                imgWaterAmount.setAlpha(1.0f);
                tvWaterState.setText(R.string.at_water_boot);
                //存放变亮的ui
                imgWaterSwitch.setImageResource(R.drawable.athome_hjsj_ico_kaiji);
                imgWaterAmount.setImageResource(R.drawable.athome_hjsj_bg_c);
                switchStatus = 0;
            }
        });
        result();
    }

    private void result() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("villageId", mATHouseBean.getVillageId());
//        mPresenter.request(ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTDATA, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTHISTORYDATA:
                        break;
                    case ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTDATA:
                        ATEnvironmentWaterBean ATEnvironmentWaterBean = gson.fromJson(jsonResult.getJSONObject("data").getString("environmentData"), ATEnvironmentWaterBean.class);
                        tvWaterOut.setText(ATEnvironmentWaterBean.getOutWater());
                        tvWaterFirst.setText(ATEnvironmentWaterBean.getFirstWater());
                        tvWaterOne.setText(ATEnvironmentWaterBean.getOneLift());
                        tvWaterTwo.setText(ATEnvironmentWaterBean.getTwoLift());
                        tvWaterThree.setText(ATEnvironmentWaterBean.getThreeLift());
                        tvWaterState.setText(ATEnvironmentWaterBean.getStatus());

//                        mATCircleWaveView.setProgress(ATEnvironmentWaterBean.getLastWater());
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}