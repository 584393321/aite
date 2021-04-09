package com.aliyun.ayland.global;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author lisc
 * @date 2015/12/15
 * @version 1.0
 */

public class ATViewHolder {
	
	private final SparseArray<View> mViews;
	private View mConvertView;
	private ViewGroup mParent;
	
	public ATViewHolder(View rootView)
	{		
		this.mConvertView = rootView; 
		this.mViews = new SparseArray<>();
	}
	
	/**  
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param
	 * @return
	 */
	public static ATViewHolder get(Context context, View convertView,
                                   ViewGroup parent, int layoutId)
	{
		if(convertView == null){
			return new ATViewHolder(context, parent, layoutId);
		}
		return (ATViewHolder)convertView.getTag();
	}
		
	/**
	 * @param context
	 * @param parent
	 * @param layoutId
	 * @param
	 */
	private ATViewHolder(Context context, ViewGroup parent, int layoutId)
	{
		
		this.mParent = parent ; 
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		//AutoUtils.autoSize(this.mConvertView );
		this.mConvertView.setTag(this);
	}
	
	@SuppressWarnings("unchecked")
	public  <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view ; 
	}
	
	public View getConvertView()
	{
		return mConvertView; 
	}
	
	public ViewGroup getParent()
	{
		return mParent ;
	}
	
    
	/**if listview
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ListView> T getListView(int viewId)
	{
		return (T)getView(viewId);
	}
	
	public void setVisibility(int viewId,int visibility)
	{
		getView(viewId).setVisibility(visibility);
	}
	
	public void setChecked(int viewId,boolean flag)
	{
		((CheckBox)getView(viewId)).setChecked(flag);
	}
	
	/**
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ATViewHolder setTvText(int viewId, String text){
		//Log.d("setTvText_viewId",viewId+"");
		TextView view = getView(viewId);
		view.setText(text);
		return this; 
	}
	
	/**
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ATViewHolder setEtText(int viewId, String text)
	{
		EditText view = getView(viewId);
		view.setText(text);
		return this;
	}
	
	
	/** 
     *   
     * @param viewId 
     * @param drawableId 
     * @return 
     */  
    public ATViewHolder setImageResource(int viewId, int drawableId)
    {  
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);    
        return this;  
    } 
    
    public void setTextColor(int viewId,int color)
    {
    	TextView tv = getView(viewId);
    	tv.setTextColor(color);
    }
    
    
    /** 
     * 
     * @param viewId 
     * @param
     * @return 
     */  
    public ATViewHolder setImageBitmap(int viewId, Bitmap bm)
    {  
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);  
        return this;  
    }  
    
}
