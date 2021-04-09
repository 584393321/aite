package at.smarthome;
/**
 * 周位图
 * @author th
 *
 */
public class AT_week_SelectFinal {
	
	public static int oncheck1(int src,int week)
	{		
		if(src>=0&&week>0)
		{
		  src|=(1<<(week-1));		
		  return src;
		}else
		{
			return -1;
		}
	}
	public static int onuncheck0(int src,int week)
	{
		if(src>=0&&week>0)
		{
		src&=~(1<<(week-1));
		return src;
		}else
		{
			return -1;
		}
	}
	public static boolean getWeekCheck(int src,int week)
	{		
		if(src==0||week<=0)
		{
			return false;
		}else 
		{
			return ((src >> (week - 1) & 1)==1) ? true : false;
		}
	}
	
	
	
	
	
	
	/*
	public static int modifyWeekValue(int src,int week)
	{
		if(week<0||src<0)
		{
			return -1;
		}
		
		if(getWeekCheck(src, week)==1)
		{//取消
			return modify0(week, src);
		}else
		{//
			return modify1(week,src);
		}
	}
	private static int modify1(int scene_value, int dev_scene) {
		return dev_scene=dev_scene|(int)Math.pow(2,(scene_value-1));
		
	}

	private static int modify0(int src, int week) {
		int targer=65535;
		//先用16位  65535		
			targer-=((int)Math.pow(2,week-1));			
		return src=(src&targer);
		
	}
	
	*/
}
