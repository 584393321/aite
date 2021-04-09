package com.aliyun.ayland.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.ui.adapter.ATMyStringAdapter;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;


public class ATChoiseContryDialog extends Dialog {

	private RecyclerView rcView;
	private ATMyStringAdapter adapter;
	private List<String> listStr = new ArrayList<String>();
	private ATOnRecyclerViewItemClickListener<String> lis;
	public ATChoiseContryDialog(Context context , List<String> list , ATOnRecyclerViewItemClickListener<String> lis) {
		super(context , R.style.wifiDialog);
		if(list != null){
			listStr.addAll(list);
		}
		this.lis = lis;
		initDialog(context);
		
		
	}
	
	private void initDialog(Context context){
		this.setContentView(R.layout.at_just_dialog_recyleview);
		adapter = new ATMyStringAdapter(listStr, context);
		adapter.setOnRecyclerViewItemClickListener(this.lis);
		rcView = findViewById(R.id.recyclerview);
		rcView.setAdapter(adapter);
		rcView.setLayoutManager(new LinearLayoutManager(context));
	}
}
