package at.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.apache.commons.net.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class HttpConnectServer1 {
	 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
	private boolean getServerData = false;
	Vector<JSONObject> serverCommands = new Vector<JSONObject>();
	ExecutorService singleSendDataThread = Executors.newSingleThreadExecutor();
	ExecutorService singlegetServerDataThread = Executors.newSingleThreadExecutor();
	private String account;
	private String token;
	String password;
	String dynamic_passwd;
	private Context context;
	IHttpConnectServer mIHttpConnectServer;
	private boolean isConnect = false;
	private boolean isNetwork = false;// 判断有网没网
	private boolean isSending = false;// 发送线程是否正在执行
	long rando = System.currentTimeMillis();

	String loginHttp;
	String postHttp;
	long sss = System.currentTimeMillis();
	int remote_her = 0;
	String pid = null;
	String vid = null;
	Handler handler = null;
	String currAccount;
    ArrayList<String>unDecode=new ArrayList<String>();//待解密的字符串
    Pattern mpPattern=Pattern.compile("\\{(.*?)\\}");//(.*?)这个拿出来，左边括号左边是左边界，右边括号右边是右边界，选对后直接匹配，即可拿到
	/**
	 * 获取当前网络类型
	 * @return 0：没有网络 1：WIFI网络 2：NET网络
	 */
	public void getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			isNetwork = false;
		} else {
			int nType = networkInfo.getType();
			if (nType == ConnectivityManager.TYPE_MOBILE) {

				String extraInfo = networkInfo.getExtraInfo();
				if (null != extraInfo) {
					isNetwork = true;
				}
			} else if (nType == ConnectivityManager.TYPE_WIFI) {
				isNetwork = true;
			}
		}
	}

	public HttpConnectServer1(Context context, String vid, String pid) {
		this.context = context;
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(Intent.ACTION_TIME_TICK);
		context.registerReceiver(networkChange, filter);
		this.vid = vid;
		this.pid = pid;
		getNetworkType();
		handler = new Handler(context.getMainLooper()) {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					getDynamicPWD();
				} else if (msg.what == 2) {
					getDynamicPWD();
				} else if (msg.what == 3) {
					if (!isConnect) {
						login();
					}
					if(handler!=null)
					handler.sendEmptyMessageDelayed(3, 5000);
				}
			};
		};
		handler.sendEmptyMessageDelayed(3, 2000);
	}

	BroadcastReceiver networkChange = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				++remote_her;
				LogUitl.d("remote_her===="+remote_her);
				if (remote_her > 2) {
					if (mIHttpConnectServer != null) {
						mIHttpConnectServer.disconnectError();
					}
				}
			} else {
				ConnectivityManager connManager = (ConnectivityManager) context
						.getSystemService(context.CONNECTIVITY_SERVICE);
				boolean isBreak = intent.getBooleanExtra(connManager.EXTRA_NO_CONNECTIVITY, false);
				if (!isBreak && handler != null) {// 有网络
					handler.sendEmptyMessageDelayed(2, 5000);
					LogUitl.d("loginOut by networkChange");
					loginOut();
					isNetwork = true;
					dynamic_passwd = null;
					token = null;
				} else {
					isNetwork = false;
					if (mIHttpConnectServer != null) {
						mIHttpConnectServer.disconnectError();
					}
				}
			}
		}
	};

	public boolean init(Context context, String account, String password) {
		if (account != null && password != null) {
			this.account = account;
			this.password = password;
			this.context = context;
			handler.removeMessages(3);
			handler.sendEmptyMessageDelayed(3, 5000);
			return true;
		}
		return false;
	}

	public void setCallListener(IHttpConnectServer mIHttpConnectServer) {
		this.mIHttpConnectServer = mIHttpConnectServer;
	}

	public synchronized void close() {
		LogUitl.d("close HttpConnectServer by SocketServer");
		loginOut();
		getServerData = false;
		isConnect = false;
		token = null;
		synchronized (serverCommands) {
			serverCommands.clear();
		}
		if (handler != null) {
			handler.removeMessages(1);
			handler.removeMessages(2);
			handler.removeMessages(3);
			handler.removeCallbacksAndMessages(null);
		}
		try {
			if (context != null)
				context.unregisterReceiver(networkChange);
		} catch (Exception e) {

		}
		handler = null;
		singleSendDataThread.shutdown();
		singlegetServerDataThread.shutdown();
		this.context = null;
		isNetwork = false;
		mIHttpConnectServer = null;
		LogUitl.d("httpConnectserver is close");
	}

	public void loginOut() {
		try {
			if (token != null && dynamic_passwd != null && postHttp != null) {
				LogUitl.d("logout============");
				JSONObject jsonLogin = new JSONObject();
				jsonLogin.put("cmd", "logout");
				jsonLogin.put("token", token);
				final String result = AT_Aes.setEncodeByKey(jsonLogin.toString(), dynamic_passwd);
				new Thread() {
					public void run() {
						if (result != null && result.length() > 0) {
							if (postHttp != null) {
								try {
									String result1 = HttpUtils2.doHttpPost(postHttp, result);
									result1 = AT_Aes.getDecodeByKey(result, dynamic_passwd);
								} catch (Exception e) {

								}
							}
						}
					};
				}.start();
			}
		} catch (Exception e) {

		}
	}

	public void login() {
		try {

			// HttpUrlUtil.setServerGetDefault();
			if ((account == null || password == null) && context != null) {
				account = context.getSharedPreferences("config", 0).getString("account", null);
				password = context.getSharedPreferences("config", 0).getString("password", null);
			}
			if (account != null && account.length() > 0 && password != null && password.length() > 0) {

				JSONObject jsonLogin = new JSONObject();
				jsonLogin.put("cmd", "login");
				jsonLogin.put("from_username", account);
				jsonLogin.put("password", password);
				jsonLogin.put("vid", vid);
				jsonLogin.put("pid", pid);
				LogUitl.d(jsonLogin.toString());
				serverCommands.clear();
				send(jsonLogin);
			}
		} catch (Exception e) {
			LogUitl.d("login error==" + e.getMessage());
		}
	}

	/**
	 * 与协调器无关的，直接与公网服器相关的数据发送接口
	 *
	 * @param jsonObject
	 * @return
	 */
	public synchronized boolean send(JSONObject jsonO) {
		try {//
			if (isNetwork) {
				if (jsonO != null) {
					if (jsonO.getString("cmd").equals("login")) {
						if (token != null) {
							if (jsonO.getString("from_username").equals(currAccount)) {
								return true;
							} else {
								token = null;
							}
						}
					}
					serverCommands.add(jsonO);
					if (dynamic_passwd == null) {
						getDynamicPWD();
					}
				}
				LogUitl.d("isSending====" + isSending);
				LogUitl.d("serverCommands.size====" + serverCommands.size());
				if (!isSending) {
					isSending = true;
					singleSendDataThread.execute(sendThread);
				}
				return true;
			} else {
				serverCommands.clear();
			}
		} catch (Exception e) {

		}
		return false;
	}

	public boolean isConnect() {
		return isConnect;
	}

	private void getDynamicPWD() {
		// {"cmd":"login","from_username":"xxx","password":"xxx"}
		try {

			JSONObject jsonLogin = new JSONObject();
			jsonLogin.put("cmd", "get_dynamic_passwd");
			jsonLogin.put("time", System.currentTimeMillis() + "");
			serverCommands.add(0, jsonLogin);
			send(null);
		} catch (Exception e) {

		}
	}

	private void getUnFriendAndFriend() {
		try {
			// {"cmd":"get_unhandle_friend","token":"xxx","offset":"0","total":"10"}
			JSONObject jsonO = new JSONObject();
			jsonO.put("cmd", "get_unhandle_friend");
			jsonO.put("token", token);
			jsonO.put("offset", "0");
			jsonO.put("total", "1000");
			send(jsonO);
			jsonO = new JSONObject();
			jsonO.put("cmd", "get_allfriend");
			jsonO.put("token", token);
			jsonO.put("offset", "0");
			jsonO.put("total", "1000");
			send(jsonO);
		} catch (Exception e) {

		}
	}

	private void callbackResult(JSONObject jsonO) {
		if (mIHttpConnectServer != null) {
			mIHttpConnectServer.serverResult(jsonO);
		} else {
			LogUitl.d("mIHttpConnectServer  is null");
		}
	}

	Runnable sendThread = new Runnable() {
		@Override
		public void run() {
			String result = null;
			JSONObject srcJson = null;
			while (serverCommands.size() > 0 && isNetwork) {
				result = null;
				try {
					if (dynamic_passwd == null) {
						getDynamicPWD();
					}
					srcJson = null;
					synchronized (serverCommands) {
						if (serverCommands.size() > 0)
							srcJson = serverCommands.get(0);
					}
					if (srcJson == null) {
						continue;
					}
					String srcCmd = srcJson.getString("cmd");
					if(srcCmd.equals("login")&&token!=null)
					{
						if(srcJson.getString("from_username").equals(currAccount))
						{							
							synchronized (serverCommands) {
								if (serverCommands.size() > 0)
									serverCommands.remove(0);
							}
							continue;
						}
					}else
					{
						LogUitl.d("token======="+token);
					}
					LogUitl.d(isNetwork + " getServerData========" + getServerData);
					LogUitl.d("cmd==="+srcCmd);
					if (srcCmd.equals("login") || srcCmd.equals("get_dynamic_passwd")) {
						if ((srcCmd.equals("login") && token == null)
								|| (srcCmd.equals("get_dynamic_passwd") && dynamic_passwd == null)) {
							if (loginHttp == null)
								loginHttp = HttpUrlUtil.getSeverPostUrlByLogin();
							if (loginHttp == null || loginHttp.length() == 0) {
								serverCommands.clear();
								continue;
							}
							synchronized (serverCommands) {
								if (serverCommands.size() > 0)
									serverCommands.remove(0);
							}
							LogUitl.d("srcJson.toString()==="+srcJson.toString());
							result = new String(
									Base64.encodeBase64(RSAUtil.encryptData(srcJson.toString().getBytes())));
							LogUitl.d("send======" + result);
							result = HttpUtils2.doHttpPost(loginHttp, result);
							if (result.equals("403")) {
								getDynamicPWD();
							}
							if (result.length() > 0) {
								byte[] bys = RSAUtil.decryptData(Base64.decodeBase64(result.getBytes()));
								if (bys == null) {
									LogUitl.d("bys.length==="+bys.length);
									synchronized (serverCommands) {
										if (serverCommands.size() > 0)
											serverCommands.remove(0);
									}
									continue;
								}
								result = new String(bys);
								LogUitl.d("http post  success " + result);
								JSONObject jsonResult = new JSONObject(result);
								String cmd = jsonResult.getString("cmd");
								String res = jsonResult.getString("result");
								if (res.equals(AT_ResultFinalManger.SUCCESS)) {
									if (cmd.equals("get_dynamic_passwd")) {										
										if(jsonResult.has("dynamic_passwd_sub"))
										{
											if(jsonResult.getString("dynamic_passwd").equals(dynamic_passwd))
											{
												dynamic_passwd = jsonResult.getString("dynamic_passwd_sub");
											}else
											{
												dynamic_passwd = jsonResult.getString("dynamic_passwd");
											}
										}else{
											dynamic_passwd = jsonResult.getString("dynamic_passwd");
										}
										// "expire":1142808
										handler.removeMessages(1);
										handler.sendEmptyMessageDelayed(1, jsonResult.getInt("expire") + 5000);
										// 如果有登录命令，则不再加入登 入命令
										boolean isexists = false;
										for (JSONObject jsonObject : serverCommands) {
											if (jsonObject.getString("cmd").equals("login")) {
												isexists = true;
												break;
											}
										}
										for (int i = 0; i < unDecode.size(); i++) {
											if (unDecode.get(i) != null) {
												result = AT_Aes.getDecodeByKey(unDecode.get(i), dynamic_passwd);
											}					
											LogUitl.d("result==="+result);
											if (result != null && !result.equals("null")&&mpPattern.matcher(result).matches()) {						
												exeResult(result);
											}
										}
										unDecode.clear();
										if (!isexists && token == null)
										{
											login();
										}else
										{
											if (!getServerData) {
												getServerData = true;
												singlegetServerDataThread.execute(getThread);
											}
										}

									} else {
										token = jsonResult.getString("token");
                                        currAccount=srcJson.getString("from_username");
										if (!getServerData) {
											getServerData = true;
											singlegetServerDataThread.execute(getThread);
										}
										account = null;
										password = null;
										callbackResult(jsonResult);
										getUnFriendAndFriend();
										isConnect = true;
										if (mIHttpConnectServer != null) {
											mIHttpConnectServer.connectSuccess();
										}
									}
								} else if (cmd.equals("login")) {
									account = null;
									password = null;
								}
								callbackResult(jsonResult);

							} else {
								LogUitl.d("get data length is 0");
							}
						} else {
							synchronized (serverCommands) {
								if (serverCommands.size() > 0)
									serverCommands.remove(0);
							}

						}

					} else {
						synchronized (serverCommands) {
							if (serverCommands.size() > 0)
								serverCommands.remove(0);
						}
						LogUitl.d(token + " dynamic_password========" + dynamic_passwd);
						if (dynamic_passwd == null) {
							getDynamicPWD();
							continue;
						}
						if (srcCmd.equals("get_code_to_register") || srcCmd.equals("register_from_phone")
								|| srcCmd.equals("register") || srcCmd.equals("get_code_to_modify_password")
								|| srcCmd.equals("reset_password")) {
							// 目前不需要加任何前期处理
						} else {
							if (token == null) {
								LogUitl.d("token is null");
								login();
								continue;
							}
							if (!srcJson.has("token"))
								srcJson.put("token", token);
						}
						LogUitl.d("srcJson.toString()==="+srcJson.toString());
						result = AT_Aes.setEncodeByKey(srcJson.toString(), dynamic_passwd);
						if (result != null && result.length() > 0) {
							if (postHttp == null)
								postHttp = HttpUrlUtil.getSeverPostUrl();
							if (postHttp == null || postHttp.length() == 0) {
								continue;
							}

							result = HttpUtils2. doHttpPost(postHttp, result);
							LogUitl.d("post success=="+result);
							LogUitl.d(postHttp + "  dynamic_passwd=======" + dynamic_passwd);
							
							result = AT_Aes.getDecodeByKey(result, dynamic_passwd);
							LogUitl.d("post success=="+result);
							if (result == null) {
								dynamic_passwd = null;
								getDynamicPWD();
							} else {
								if (result.length() > 0) {
									JSONObject jsonResult = new JSONObject(result);
									String cmd = jsonResult.getString("cmd");
									String res = jsonResult.getString("result");
									if (res.equals("token_error")) {
										LogUitl.d("loginOut by token_error");
										loginOut();
										Thread.sleep(2000);
										token = null;
										login();
									}
									if (cmd.equals("register") && res.equals(AT_ResultFinalManger.SUCCESS)) {
										login();
									}

									callbackResult(jsonResult);
								} else {
									LogUitl.d("get data length is 0");
								}

							}
						} else {
							LogUitl.d("send sourcs is null");

						}
					}

				} catch (SocketTimeoutException e) {
					if (mIHttpConnectServer != null) {
						mIHttpConnectServer.sendResultBackCall(-1);
					}
					synchronized (serverCommands) {
						if (serverCommands.size() > 0)
							serverCommands.remove(0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loginHttp = null;
					postHttp = null;
					LogUitl.d("sendThread=HttpConnectServer=error util==" + e.getMessage());
					synchronized (serverCommands) {
						if (serverCommands.size() > 0)
							serverCommands.remove(0);
					}

				}

			}
			isSending = false;
		}
	};
	String getHttp = null;
	Runnable getThread = new Runnable() {

		@Override
		public void run() {
			String result = null;
			String resultTemp=null;
			while (getServerData && isNetwork) {
				try {
					result=null;
					getHttp = HttpUrlUtil.getSeverGetUrl(token);
					LogUitl.d(sss + " do get server==" + getHttp);
					if (getHttp == null || getHttp.length() == 0 || token == null) {
						// Thread.sleep(2000);
						getServerData = false;
						continue;
					}

					LogUitl.d("begin get start time=" + sdf.format(new Date(System.currentTimeMillis())));
					resultTemp = HttpUtils2.doHttpGet(getHttp);
					LogUitl.d("get data success time=" + sdf.format(new Date(System.currentTimeMillis())));
					LogUitl.d(dynamic_passwd+" do get server=1=" + resultTemp);
					if (resultTemp != null) {
						result = AT_Aes.getDecodeByKey(resultTemp, dynamic_passwd);
					}
					LogUitl.d("get result==="+result);
					if (result != null && !result.equals("null")&&mpPattern.matcher(result).matches()) {						
						exeResult(result);
					} else {
						dynamic_passwd = null;
						getServerData = false;
						isConnect = false;
						if(resultTemp!=null)
						unDecode.add(resultTemp);
						// 重登不能断开
						getDynamicPWD();
					}
				} /*catch (SocketTimeoutException e) {
					//throws ClientProtocolException, IOException,SocketTimeoutException
					LogUitl.d("getThread server time out");
				}*/ catch (Exception e) {
					LogUitl.d("getThread=HttpConnectServer=" + e.getMessage());
					loginOut();
					getServerData = false;
					isConnect = false;
					token = null;
					getHttp = null;
					if (mIHttpConnectServer != null) {
						mIHttpConnectServer.disconnectError();
					}
					e.printStackTrace();
					LogUitl.d("getThread=HttpConnectServer=error==" + e.getMessage());
				}

			}
			getServerData = false;
		}
	};
	private void exeResult(String result)
	{
		try{
		JSONObject jsonResult = new JSONObject(result);
		if (jsonResult.has("result")) {
			if (jsonResult.getString("result").equals("token_error")) {
				LogUitl.d("loginOut by token_error2");
				loginOut();
				token = null;
				getHttp = null;
				isConnect = false;
				getServerData = false;
				// 重登不能断开
				login();
				// continue;
			} else if (jsonResult.getString("result").equals("someone_login")) {// someone_login

				if (mIHttpConnectServer != null) {
					LogUitl.d(rando + " someone_login");
					LogUitl.d("loginOut by someone_login");
					loginOut();
					token = null;
					getHttp = null;
					mIHttpConnectServer.someone_login();
					getServerData = false;
				} else {
					LogUitl.d("mIHttpConnectServer is null");
				}
			} else if (jsonResult.getString("result").equals("recv_timeout")) {
				remote_her = 0;
			}
		}
		LogUitl.d("begin send to ui time=" + sdf.format(new Date(System.currentTimeMillis())));
		remote_her = 0;
		callbackResult(jsonResult);
		LogUitl.d("end send to ui time=" + sdf.format(new Date(System.currentTimeMillis())));
		isConnect = true;
		}catch(JSONException e){
			LogUitl.d(e.getMessage());
		}catch(Exception e)
		{
			LogUitl.d("getThread error="+e.getMessage());
			loginOut();
			getServerData = false;
			isConnect = false;
			token = null;
			getHttp = null;
			if (mIHttpConnectServer != null) {
				mIHttpConnectServer.disconnectError();
			}
			e.printStackTrace();
			LogUitl.d("getThread=HttpConnectServer=exeResult error==" + e.getMessage());
		}
	}
}
