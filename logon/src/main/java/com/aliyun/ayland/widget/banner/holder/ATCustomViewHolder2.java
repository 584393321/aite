package com.aliyun.ayland.widget.banner.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATWeatherBean;
import com.anthouse.xuhui.R;

public class ATCustomViewHolder2 implements ATBannerViewHolder<ATWeatherBean> {
    private ImageView mImgBg, mImgBg1, mImgBegin, mImgTo, mImgEnd;
    private TextView mTvWeather, mTvOutsideTemp, mTvWind, mTvAddBox;
    private LinearLayout mLlInside;
    private Context mContext;

    @Override
    public View createView(Context context) {
        mContext =context;
        View view = LayoutInflater.from(context).inflate(R.layout.at_banner_item, null);
        ATAutoUtils.auto(view);
        mImgBg = view.findViewById(R.id.img_bg);
        mImgBg1 = view.findViewById(R.id.img_bg1);
        mImgBegin = view.findViewById(R.id.img_begin);
        mImgTo = view.findViewById(R.id.img_to);
        mImgEnd = view.findViewById(R.id.img_end);
        mTvWeather = view.findViewById(R.id.tv_weather);
        mTvOutsideTemp = view.findViewById(R.id.tv_outside_temp);
        mTvWind = view.findViewById(R.id.tv_wind);
        mTvAddBox = view.findViewById(R.id.tv_add_box);
        mLlInside = view.findViewById(R.id.ll_inside);
        return view;
    }

    @Override
    public void onBind(Context context, int position, ATWeatherBean data) {
        // 数据绑定
        mLlInside.setVisibility(View.GONE);
        mImgBg1.setVisibility(View.VISIBLE);
        if (position != 0) {
            mImgBg1.setImageResource(R.drawable.at_bjsh_banner_b);
//            Glide.with(mContext).load(data.getImgUrl()).into(mImgBg1);
        } else {
            mImgBg1.setImageResource(R.drawable.at_bjsh_banner_a);
        }
//            mImgBg1.setVisibility(View.GONE);
//            if (data != null) {
//                mTvOutsideTemp.setText(String.format(context.getString(R.string._outside_temp)
//                        , data.getMinimumTemperature(), data.getMaximumTemperature()));
//                mTvWind.setText(data.getWindLevel().getString("cnName"));
//                mTvWeather.setText(data.getWeather().getString("cnName"));
//                if (data.getWeather().getString("cnName").contains("云")) {
//                    mImgBg.setImageResource(R.drawable.cloudy_bg);
//                } else if (data.getWeather().getString("cnName").contains("雾") || data.getWeather().getString("cnName").contains("霾")) {
//                    mImgBg.setImageResource(R.drawable.foggy_bg);
//                } else if (data.getWeather().getString("cnName").contains("沙") || data.getWeather().getString("cnName").contains("尘")) {
//                    mImgBg.setImageResource(R.drawable.sand_bg);
//                } else if (data.getWeather().getString("cnName").contains("雷")) {
//                    mImgBg.setImageResource(R.drawable.thunder_bg);
//                } else if (data.getWeather().getString("cnName").contains("雪")) {
//                    mImgBg.setImageResource(R.drawable.snow_bg);
//                } else if (data.getWeather().getString("cnName").contains("雨")) {
//                    mImgBg.setImageResource(R.drawable.rain_bg);
//                } else if (data.getWeather().getString("cnName").contains("阴")) {
//                    mImgBg.setImageResource(R.drawable.overcast_bg);
//                }else{
//                    mImgBg.setImageResource(R.drawable.sunny_bg);
//                }
//                if (data.getWeather().getString("enName").contains("to")) {
//                    mImgBegin.setVisibility(View.VISIBLE);
//                    mImgTo.setVisibility(View.VISIBLE);
//                    switch (data.getWeather().getString("cnName")) {
//                        case "小到中雨":
//                            mImgBegin.setImageResource(R.drawable.lightrain);
//                            mImgEnd.setImageResource(R.drawable.moderaterain);
//                            break;
//                        case "中到大雨":
//                            mImgBegin.setImageResource(R.drawable.moderaterain);
//                            mImgEnd.setImageResource(R.drawable.heavyrain);
//                            break;
//                        case "大到暴雨":
//                            mImgBegin.setImageResource(R.drawable.heavyrain);
//                            mImgEnd.setImageResource(R.drawable.storm);
//                            break;
//                        case "暴雨到大暴雨":
//                            mImgBegin.setImageResource(R.drawable.storm);
//                            mImgEnd.setImageResource(R.drawable.heavystorm);
//                            break;
//                        case "大暴雨到特大暴雨":
//                            mImgBegin.setImageResource(R.drawable.heavystorm);
//                            mImgEnd.setImageResource(R.drawable.severestorm);
//                            break;
//                        case "小到中雪":
//                            mImgBegin.setImageResource(R.drawable.lightsnow);
//                            mImgEnd.setImageResource(R.drawable.moderatesnow);
//                            break;
//                        case "中到大雪":
//                            mImgBegin.setImageResource(R.drawable.moderatesnow);
//                            mImgEnd.setImageResource(R.drawable.heavysnow);
//                            break;
//                        case "大到暴雪":
//                            mImgBegin.setImageResource(R.drawable.heavysnow);
//                            mImgEnd.setImageResource(R.drawable.weather_17);
//                            break;
//                    }
//                } else {
//                    mImgBegin.setVisibility(View.GONE);
//                    mImgTo.setVisibility(View.GONE);
//                    mImgEnd.setImageResource(ATResourceUtils.getResIdByName(data.getWeather().getString("enName").replace(" ", "").toLowerCase(), ATResourceUtils.ResourceType.DRAWABLE));
//                }
//            }
//        }
    }
}
