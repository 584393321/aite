package at.smarthome;

import org.json.JSONObject;

public interface IHttpConnectServer {
public void serverResult(JSONObject jsonObject);
public void connectSuccess();
public void disconnectError();
public void someone_login();
public void sendResultBackCall(int result);
}
