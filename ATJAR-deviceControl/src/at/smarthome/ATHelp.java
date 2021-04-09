package at.smarthome;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xhc.sbh.tool.lists.R;

public class ATHelp {
	
	/**
	 * @param cmd
	 * 超级权限 需要运行在子线程中
	 */
	public static void rootcmd(String cmd){		
		DatagramSocket rootsocket=null;
		try {
			rootsocket=new DatagramSocket ();
			InetAddress serverAddress;
			serverAddress = InetAddress.getByName("127.0.0.1");
			byte data[] = cmd.getBytes();
			DatagramPacket  dp = new DatagramPacket (data , data.length , serverAddress , 5555);
			rootsocket.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置按钮延迟
	 * 
	 * @param v
	 */
	public static void setClickDelay(final View v, long delay) {
		if (v == null)
			return;
		v.setEnabled(false);
		v.postDelayed(new Runnable() {
			@Override
			public void run() {
				v.setEnabled(true);
			}
		}, delay);
	}
	
	private static WindowManager wMgr ;
	
	public static ImageView createFloatBackButton(final Context appContext,int marginBottom, int marginRight) {
		// TODO Auto-generated method stub
		ImageView floatView = new ImageView(appContext);
		floatView.setImageResource(R.drawable.ic_float_back);
		LayoutParams imgParams = new LayoutParams(dp2px(appContext, 48), dp2px(appContext, 48));
		floatView.setScaleType(ScaleType.CENTER_CROP);
		floatView.setLayoutParams(imgParams);
		floatView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//rootcmd("input keyevent " + KeyEvent.KEYCODE_BACK);
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
					}
				}).start();								
				//removeFloatButton(view);
			}
		});
				
		wMgr = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wMgr.getDefaultDisplay().getMetrics(dm);
		
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; 
		params.format = PixelFormat.RGBA_8888; 
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; 
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.x = dm.widthPixels-marginRight;
		params.y = dm.heightPixels-marginBottom;		
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		wMgr.addView(floatView, params);
		return floatView;
	}	
		
	public static void removeFloatButton(View view)
	{
		if(view != null){			
			wMgr.removeView(view);
			view = null;
		}
	}
		
	public static int dp2px(Context context, float dpVal)  
    {  
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  
                dpVal, context.getResources().getDisplayMetrics());  
    }  
}
