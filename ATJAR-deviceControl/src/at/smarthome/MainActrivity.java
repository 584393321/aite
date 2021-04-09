package at.smarthome;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActrivity extends Activity{
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	Log.d("sbh","test scene=="+AT_DeviceControl.getDevControlCmdByTV("gateway","sbh","testroom","devname", "mute",1,0));
	try{
	JSONObject jsonO=new JSONObject();
	jsonO.put("command", "query");
	jsonO.put("name", "孙波海");
	
	jsonO.put("mst_type", "12435575");
	jsonO.put("from_account", "testsbh");
	String result=AT_Aes.setEncode(jsonO.toString());
	Log.d("sbh", "encode============="+result);
	Log.d("sbh", "dencode=====bykey1========"+AT_Aes.getDecodeByKey(result, "atsmart201511049"));
	Log.d("sbh", "dencode============="+AT_Aes.getDecode(result));
	//'a','t','s','m','a','r','t','2','0','1','5','1','1','0','4','9'
	 result=AT_Aes.setEncodeByKey(jsonO.toString(),"sbhsbhsbh1234567");
	Log.d("sbh", "encode=====bykey========"+result);
	Log.d("sbh", "encode=======error======"+AT_Aes.getDecode(result));
	Log.d("sbh", "dencode=====bykey========"+AT_Aes.getDecodeByKey(result, "sbhsbhsbh1234567"));
	
	            //94O6b4DwNkAkIjI3YWK6GUUZxMKJJoo+S47FGUqhF2CQujkBSZmR+arOvdwTjPjIBDbi2Y09cIT72dXnQSLJEIDuNg==
	//String den="94O6b4DwNkAkIjI3YWK6GUUZxMKJJoo+S47FGUqhF2CQujkBSZmR+arOvdwTjPjIBDbi2Y09cIT72dXnQSLJEIDuNg==";
	
	
	
	}catch (Exception e) {
		Log.d("sbh", e.getMessage());
	}
	
}
}
