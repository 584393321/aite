package com.aliyun.ayland.global;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Common Adapter 
 * @author lisc
 * @date 2015/12/15
 * @version 1.0
 */

public abstract class ATCommonAdapter<T> extends BaseAdapter {
	
	protected Context mContext ;
	protected List<T> mData;
	protected final int mItemLayoutId;
	
	public ATCommonAdapter(Context context, List<T> datas , int itemLayoutId){
		this.mContext = context ;
		this.mData = datas ;
		this.mItemLayoutId = itemLayoutId;
	}
	
	public void refreshView(List<T> data)
	{
		this.mData = data;  
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData==null ? 0 :mData.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		if(mData != null && position< mData.size()){
			return mData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public abstract void convert(ATViewHolder holder, T item, int position);
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {  
        final ATViewHolder viewHolder = ATViewHolder.get(mContext, convertView, parent, mItemLayoutId);
        convert(viewHolder, getItem(position),position);
        return viewHolder.getConvertView();
    }
	
}
