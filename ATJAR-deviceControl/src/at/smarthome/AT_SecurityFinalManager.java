package at.smarthome;

/**
 * 安防模式及探头常开常闭
 * @author CHT
 *
 */
public class AT_SecurityFinalManager {
	//security_mode 位图 第1位在家  第2位外出   第3位就寝     第4位常设（ 为1代表选中,0未选中, 常设4个位都 为1）
	public interface Mode{
		public final static String HOME="home";//1
		public final static String OUT="out";//2
		public final static String SLEEP="sleep";//3
		public final static String DISARM="disarm";
		public final static String FIXSET="fixset";
	}
	
	public interface State{
		public final static String OPEN="open";//对应不短接 0
		public final static String CLOSE="close"; //对应短接 1
		public final static String DEFAULT="default"; //不区分
	}

	public static boolean isShowSensorType(String dev_class_type)
	{
		if(dev_class_type!=null)
		{
			if(dev_class_type.equals(AT_DeviceClassType.COORDIN_AT_SENSOR)
					||dev_class_type.equals(AT_DeviceClassType.Sensor.SENSOR_BUILTIN)
					||dev_class_type.equals(AT_DeviceClassType.Sensor.SENSOR_BUILTOUT)
					||dev_class_type.equals(AT_DeviceClassType.Sensor.SENSOR_MC))
				
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 选中安防模式
	 * @param src 源数据
	 * @param security_mode  安防模式
	 * @return 返回处理后的数据
	 */
    public static int checkSecurity(int src,String security_mode)
    {
    	if(security_mode!=null)
    	{
    		if(security_mode.equals(AT_SecurityFinalManager.Mode.HOME))
    		{
    			src|=1;		
    		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.OUT))
    		{
    			src|=2;		
    		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.SLEEP))
    		{
    			src|=4;		
    		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.DISARM))
    		{
    			src|=8;		
    		}else if(Mode.FIXSET.equals(security_mode)){
				src=15;
			}
    	}
    	return src;
    }
    /**
	 * 反选中安防模式
	 * @param src 源数据
	 * @param security_mode  安防模式
	 * @return 返回处理后的数据
	 */
    public static int unCheckSecurity(int src,String security_mode)
    {
    	if(security_mode!=null)
    	{
    		if(security_mode.equals(AT_SecurityFinalManager.Mode.HOME))
    		{
    			src&=~1;	
    		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.OUT))
    		{
    			src&=~2;	
    		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.SLEEP))
    		{
    			src&=~4;
    		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.DISARM))
    		{
    			src&=~8;
    		}else if(Mode.FIXSET.equals(security_mode)){
				src=0;
			}
    	}
    	return src;
    }
    /**
   	 * 反选中安防模式
   	 * @param src 源数据
   	 * @param security_mode  安防模式
   	 * @return 返回处理后的数据
   	 */
       public static boolean getCheckSecurity(int src,String security_mode)
       {
    	   boolean bool=false;
       	if(security_mode!=null)
       	{
       		if(security_mode.equals(AT_SecurityFinalManager.Mode.HOME))
       		{
       			if((src&1)==1)	
       			{
       				bool=true;
       			}else
       			{
       				bool=false;
       			}
       		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.OUT))
       		{
       			if((src>>1&1)==1)	
       			{
       				bool=true;
       			}else
       			{
       				bool=false;
       			}	
       		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.SLEEP))
       		{
       			if((src>>2&1)==1)	
       			{
       				bool=true;
       			}else
       			{
       				bool=false;
       			}
       		}else if(security_mode.equals(AT_SecurityFinalManager.Mode.DISARM))
       		{
       			if((src>>3&1)==1)	
       			{
       				bool=true;
       			}else
       			{
       				bool=false;
       			}
       		}else if(Mode.FIXSET.equals(security_mode)){
				if((src>>3&1) == 1){
					bool = true;
				}else{
					bool = false;
				}
			}
       	}
       	return bool;
       }
}
