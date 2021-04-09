package com.aliyun.ayland.data;

import java.util.List;

public class ATPayBean {
    /**
     * code : 200
     * payData : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2021001141683062&biz_content=%7B%22body%22%3A%22colorfulBox%22%2C%22out_trade_no%22%3A%221242294224671068160colorfulBox%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%81%A5%E8%BA%AB%E6%88%BF%22%2C%22timeout_express%22%3A%2210m%22%2C%22total_amount%22%3A%22200%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay¬ify_url=https%3A%2F%2Fsmarthome.cifi.com.cn%2Fvillagecenter%2Fcolorfulbox%2FaliPay%2FpayNotify&sign=fK47LPPr%2FdPvT2Uzj5%2FaGJ%2F2RZzPGQExWRgTxUfaged3evwwjVYk4NAyrNyzuwO3%2FlA8rop%2Be4oTXlnc9H01MYwCK3DnTcTzXgDArdsfylr42tqz6jy%2BG31lpQkOsc%2FRl%2BMSuMN%2FQ9QOvhr7sMrrHToWpUZcn09Fkku3AXOCXNIFZ3vdm7jY8YvOlMMsGTbc9%2FFCT4VpTfoCOQs2fFEI1A9XNEHQg1dNpxMCWhGkuHEBQ4aeG6DHra76E2zs%2Bb7hVunxAYvQtEISKyuZBRisBsPyE7y6aAwFzwBzRiSsc5cdZZUlRio1ditjJiLfKZ4WIyiqCN6DYtbTFQcrhog%2FyA%3D%3D&sign_type=RSA2×tamp=2020-03-24+11%3A36%3A38&version=1.0
     * message : success
     * applicationIdList : [512]
     */

    private int code;
    private String payData;
    private String message;
    private List<Integer> applicationIdList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPayData() {
        return payData;
    }

    public void setPayData(String payData) {
        this.payData = payData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getApplicationIdList() {
        return applicationIdList;
    }

    public void setApplicationIdList(List<Integer> applicationIdList) {
        this.applicationIdList = applicationIdList;
    }
}
