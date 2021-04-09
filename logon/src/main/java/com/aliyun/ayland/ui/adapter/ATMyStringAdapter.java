package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anthouse.xuhui.R;

import java.util.List;


public class ATMyStringAdapter extends ATMyBaseAdapter<String, ATMyStringAdapter.ViewHolder> {

	public ATMyStringAdapter(List<String> list, Context context) {
		super(list, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup container, int type) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.at_just_textview, container , false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder,final int position) {
		// TODO Auto-generated method stub
		final String str = list.get(position);
		holder.tv.setText(str);
		holder.tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(lis != null){
					lis.onItemClick(v, str, position);
				}
			}
		});
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView tv;
		
		public ViewHolder(View itemView) {
			super(itemView);
			tv = itemView.findViewById(R.id.tv);
		}
	}
}








