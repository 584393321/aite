package at.smarthome;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProviderData {  
    
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/at.smarthome.gateway.model.provider";  
  
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/at.smarthome.gateway.model.provider";  
  
    /** 
     * 保存leave_message表中用到的常量 
     * @author  
     */  
    public static final class LeaveMessageColumns implements BaseColumns {    
        public static final String AUTHORITY = "at.smarthome.gateway.model.provider.leavemessage";  
        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/leave_message");  
        public static final String TABLE_NAME = "leave_message";
        
        //filed
        public static final String ID = "_id";  
        public static final String TYPE = "type";  
        public static final String CONTENT = "content";   
        public static final String IS_READ = "is_read";
        public static final String ROLE_TYPE = "role_type";
        public static final String CREATE_TIME = "create_time";
        public static final String SENDER = "sender";
        public static final String RECEIVER = "receiver";
        
        public static final class RoleType
        {
        	public static final String GATEWAY = "gateway";
        	public static final String DOORDOG = "door";
        	public static final String MOBILE = "mobile";
        	public static final String LOCAL = "local";
        	public static final String CENTER = "center";
        }
        //消息类型
        public static final class MessageType{
        	public static final int TYPE_VEDIO = 1000; //视频
        	public static final int TYPE_AUDIO = 1001; //语音
        	public static final int TYPE_TEXT = 1002;  //文本
        }
    }  
      
    /** 
     * 保存call_log表中用到的常量 
     * @author 
     */  
    public static final class CallLogColumns implements BaseColumns {  
        public static final String AUTHORITY = "at.smarthome.gateway.model.provider.calllog";  
        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/call_log");  
        public static final String TABLE_NAME = "call_log";  
          
        //filed 
        public static final String ID = "_id";
        public static final String FROM_ACCOUNT = "from_account";  
        public static final String TO_ACCOUNT = "to_account";  
        public static final String IS_READ = "is_read";
        public static final String CALL_TYPE = "call_type";
        
        public static final class CallType
        {
        	public static final String OUT = "call_out"; //呼出
        	public static final String IN = "call_in"; //呼入
        }
    }  
      
}  
