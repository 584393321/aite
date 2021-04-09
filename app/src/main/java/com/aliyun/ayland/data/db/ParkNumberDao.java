package com.aliyun.ayland.data.db;

import android.content.Context;

import com.aliyun.ayland.data.ParkNumberBean;
import com.aliyun.ayland.utils.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


public class ParkNumberDao {
	private Dao<ParkNumberBean, Integer> parkNumberDao;
	private DatabaseHelper helper;
	private Context context ;
	public ParkNumberDao(Context context){
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			parkNumberDao = helper.getDao(ParkNumberBean.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//保存
	public void add(ParkNumberBean a){
		try {
			parkNumberDao.create(a);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改
	public void update(ParkNumberBean data) {
		try {
			parkNumberDao.createOrUpdate(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//查询全部
	public List<ParkNumberBean> getAll(){
		try {
			 QueryBuilder<ParkNumberBean, Integer> builder = parkNumberDao.queryBuilder();
			 builder.orderBy("create_time", false);
			return builder.query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ParkNumberBean getByParkNumber(String park_number){
		try {
			QueryBuilder<ParkNumberBean, Integer> builder = parkNumberDao.queryBuilder();
			builder.where().eq("park_number", park_number);
			return builder.queryForFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void del(ParkNumberBean bean){
		try {
			parkNumberDao.delete(bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}























