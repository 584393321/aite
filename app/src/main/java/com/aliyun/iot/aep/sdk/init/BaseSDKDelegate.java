package com.aliyun.iot.aep.sdk.init;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.SecurityInit;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectConfig;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectState;
import com.aliyun.alink.linksdk.channel.gateway.api.IGatewayConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import com.aliyun.alink.page.rn.InitializationHelper;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.adapter.APIGatewayHttpAdapterImpl;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Env;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.log.ALog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Base SDK
 */
@SuppressWarnings("unused")
public final class BaseSDKDelegate extends SimpleSDKDelegateImp {

    static final private String TAG = "APIGatewaySDKDelegate";

    private final static String KEY_ENV = "env";
    private final static String KEY_AUTH_CODE = "securityIndex";
    private final static String KEY_REGION = "region";
    private final static String KEY_LANGUAGE = "language";

    private final static String DEFAULT_ENV = "RELEASE";
    private final static String DEFAULT_AUTH_CODE = "114d";
    private final static String DEFAULT_REGION = "china";
    private final static String DEFAULT_HOST = "api.link.aliyun.com";
    private final static String DEFAULT_LANGUAGE = "zh-CN";

    private final static String KEY_PLUGIN_ENV = "pluginEnv";

    private static final String DEFAULT_SERVER_ENV = "production";
    private static final String DEFAULT_PLUGIN_ENV = "release";

    private final static String KEY_CHANNEL_HOST = "channelHost";
    private final static String KEY_AUTO_HOST = "autoSelectChannelHost";
    private final static String KEY_CHECK_ROOT_CRT = "isCheckChannelRootCrt";

    /* API: ISDKDelegate */

    @Override
    public int init(Application app, SDKConfigure sdkConfigure, Map<String, String> args) {
        int ret = 0;
        int apiRet = initApiGateway(app, sdkConfigure, args);
        if (-1 == apiRet) {
            ret = -1;
        }

        int rnRet = initRn(app, sdkConfigure, args);
        if (-1 == rnRet) {
            ret = -1;
        }

        int downstreamRet = initDownstream(app, sdkConfigure, args);
        if (-1 == downstreamRet) {
            ret = -1;
        }

        initBreeze(app);

        return ret;
    }

    int initApiGateway(Application app, SDKConfigure sdkConfigure, Map<String, String> args) {
        int ret = 0;

        try {
            ret = SecurityInit.Initialize(app);
        } catch (JAQException ex) {
            ALog.e(TAG, "security-sdk-initialize-failed", ex);
            ret = ex.getErrorCode();
        } catch (Exception ex) {
            ALog.e(TAG, "security-sdk-initialize-failed", ex);
            ret = -1;
        }

        Env apiEnv = Env.RELEASE;
        String env = "RELEASE";
        String region = DEFAULT_REGION;
        String authCode = DEFAULT_AUTH_CODE;
        String language = DEFAULT_LANGUAGE;
        String host = DEFAULT_HOST;
        boolean languageSepcified = false;

        {
            String e = args.get(KEY_ENV);
            if ("PRE".equalsIgnoreCase(e)) {
                apiEnv = Env.PRE;
                env = "PRE";
            } else if ("TEST".equalsIgnoreCase(e)) {
                apiEnv = Env.RELEASE;
                env = "TEST";
            }

            String re = args.get(KEY_REGION);
            if ("singapore".equalsIgnoreCase(re)) {
                region = "singapore";
            }

            String ac = args.get(KEY_AUTH_CODE);
            if (!TextUtils.isEmpty(ac)) {
                authCode = ac;
            }

            String lan = args.get(KEY_LANGUAGE);
            if (!TextUtils.isEmpty(lan)) {
                languageSepcified = true;
                language = lan;
            }
        }

        do {

            JSONObject opts = sdkConfigure.opts;
            if (null == opts) {
                break;
            }

            JSONObject re = opts.optJSONObject(region);
            if (null == re) {
                break;
            }

            JSONObject hosts = re.optJSONObject("hosts");
            if (null == hosts) {
                break;
            }

            String h = hosts.optString(env.toLowerCase());
            if (!TextUtils.isEmpty(h)) {
                host = h;
            }

            String lan = re.optString("language");
            if (!languageSepcified && !TextUtils.isEmpty(lan)) {
                language = lan;
            }

        } while (false);

        IoTAPIClientImpl.InitializeConfig config = new IoTAPIClientImpl.InitializeConfig();
        config.host = host;
        config.apiEnv = apiEnv;
        config.authCode = authCode;

        IoTAPIClientImpl impl = IoTAPIClientImpl.getInstance();
        impl.init(app, config);
        impl.setLanguage(language);

        return ret;
    }

    int initRn(Application app, SDKConfigure sdkConfigure, Map<String, String> args) {
        String serverEnv = DEFAULT_SERVER_ENV;
        String pluginEnv = DEFAULT_PLUGIN_ENV;
        String region = DEFAULT_REGION;
        String language = DEFAULT_LANGUAGE;

        {
            String env = args.get(KEY_ENV);
            if ("PRE".equalsIgnoreCase(env)) {
                serverEnv = "test";
            } else if ("TEST".equalsIgnoreCase(env)) {
                serverEnv = "development";
            }

            String pEnv = args.get(KEY_PLUGIN_ENV);
            if ("test".equalsIgnoreCase(pEnv)) {
                pluginEnv = "test";
            }

            String re = args.get(KEY_REGION);
            if ("singapore".equalsIgnoreCase(re)) {
                region = "singapore";
            }

            String lan = args.get(KEY_LANGUAGE);
            if (!TextUtils.isEmpty(lan)) {
                language = lan;
            } else {
                JSONObject opts = sdkConfigure.opts;
                JSONObject reg = opts.optJSONObject(region);

                do {
                    if (null == reg) {
                        break;
                    }

                    lan = reg.optString("language");
                    if (!TextUtils.isEmpty(lan)) {
                        language = lan;
                    }
                } while (false);
            }
        }

        InitializationHelper.initialize(app, pluginEnv, serverEnv, language);

        return 0;

    }

    int initDownstream(final Application app, SDKConfigure configure, final Map<String, String> args) {

        int ret = 0;
        // 读取环境变量
        String env = args == null ? "RELEASE" : args.get(KEY_ENV);
        String authCode = args == null ? "114d" : args.get(KEY_AUTH_CODE);
        String appKey = APIGatewayHttpAdapterImpl.getAppKey(app, authCode);

        MobileConnectConfig config = new MobileConnectConfig();
        config.appkey = appKey;
        config.securityGuardAuthcode = authCode;

        JSONObject opts = configure.opts;
        JSONObject json = null;
        try {
            if (null != opts) {
                json = opts.getJSONObject(env.toLowerCase());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String channelHost = "";
        boolean autoHost = false;
        boolean isCheckRootCrt = false;
        if (null != json) {
            try {
                channelHost = json.getString(KEY_CHANNEL_HOST);
                autoHost = !"false".equalsIgnoreCase(json.getString(KEY_AUTO_HOST));
                isCheckRootCrt = !"false".equalsIgnoreCase(json.getString(KEY_CHECK_ROOT_CRT));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 三元组，指定apiclient host
        config.autoSelectChannelHost = autoHost;
        config.channelHost = channelHost;
        config.isCheckChannelRootCrt = isCheckRootCrt;

        // 注意:长连接的初始化只能在主进程中执行，否则会导致互踢问题
        MobileChannel.getInstance().startConnect(app, config, new IMobileConnectListener() {
            @Override
            public void onConnectStateChange(MobileConnectState state) {
                ALog.d(TAG, "onConnectStateChange(), state = " + state.toString());
                args.put("KEY_TRACE_ID", MobileChannel.getInstance().getClientId());
            }
        });

        ALog.d(TAG, "initialized");
        return ret;
    }

    void initBreeze(Application app) {
        Log.i(TAG, "initGateWayChannel app：" + app);
        String clientId = MobileChannel.getInstance().getClientId();
        if (TextUtils.isEmpty(clientId)) {
            //请检查长连接通道是否初始化成功
            com.aliyun.iot.ble.util.Log.w(TAG, "请检查长连接通道是否初始化成功");
            Log.w(TAG, "请检查长连接通道是否初始化成功");
            return;
        }

        String mobileProductKey = clientId.split("&")[1];
        String mobileDeviceName = clientId.split("&")[0];

        GatewayConnectConfig config = new GatewayConnectConfig(mobileProductKey, mobileDeviceName, "");
        GatewayChannel.getInstance().startConnect(app, config, new IGatewayConnectListener() {
            @Override
            public void onConnectStateChange(GatewayConnectState state) {
                Log.d(TAG, "IGatewayConnectListener onConnectStateChange(), state = " + state.toString());
                if (state == GatewayConnectState.CONNECTED) {
                    com.aliyun.iot.ble.util.Log.d(TAG, "网关建联成功");
                    Log.d(TAG, "网关建联成功");
                } else if (state == GatewayConnectState.CONNECTFAIL) {
                    Log.i(TAG, "Gateway connect failed");
                }
            }
        });

    }

}