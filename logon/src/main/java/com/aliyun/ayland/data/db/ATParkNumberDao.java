package com.aliyun.ayland.data.db;

import android.content.Context;

import com.aliyun.ayland.data.ATParkNumberBean;
import com.aliyun.ayland.utils.ATDatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


public class ATParkNumberDao {
	private Dao<ATParkNumberBean, Integer> parkNumberDao;
	private ATDatabaseHelper helper;
	private Context context ;
	public ATParkNumberDao(Context context){
		this.context = context;
		try
		{
			helper = ATDatabaseHelper.getHelper(context);
			parkNumberDao = helper.getDao(ATParkNumberBean.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//保存
	public void add(ATParkNumberBean a){
		try {
			parkNumberDao.create(a);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改
	public void update(ATParkNumberBean data) {
		try {
			parkNumberDao.createOrUpdate(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//查询全部
	public List<ATParkNumberBean> getAll(){
		try {
			 QueryBuilder<ATParkNumberBean, Integer> builder = parkNumberDao.queryBuilder();
			 builder.orderBy("create_time", false);
			return builder.query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ATParkNumberBean getByParkNumber(String park_number){
		try {
			QueryBuilder<ATParkNumberBean, Integer> builder = parkNumberDao.queryBuilder();
			builder.where().eq("park_number", park_number);
			return builder.queryForFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void del(ATParkNumberBean bean){
		try {
			parkNumberDao.delete(bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}























