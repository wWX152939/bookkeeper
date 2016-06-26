package com.onekey.bookkeeper;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJDB;

import android.util.Log;

import com.onekey.bookkeeper.entity.Resource;


/**
 * @author onekey
 * 
 */
public class DataProcessCenter {
	
	public static DataProcessCenter instance;
	
	public KJDB mKJDB;
	
	private List<Resource> mResourceList;
	
	private Resource mParentBean;
	
	public static DataProcessCenter create() {
		if (instance == null) {
			instance = new DataProcessCenter();
		}
		return instance;
	}
	
	public void add(Resource resource) {
		KJDB.create().save(resource);
		mResourceList = mKJDB.findAll(Resource.class);
	}
	
	public List<Resource> getAllList() {
		return mResourceList;
	}
	
	public List<Resource> getChildList(Resource currentBean) {
		if (currentBean == null) {
			currentBean = new Resource();
		}
		mParentBean = currentBean;
		if (mKJDB == null) {
			mKJDB = KJDB.create();
		}

		if (mResourceList == null) {
			mResourceList = mKJDB.findAll(Resource.class);
			if (mResourceList.isEmpty()) {
				initialData();
			}
			Log.i("wzw", "list:" + mResourceList);
		}
		
		int currentId = currentBean.getId();
		List<Resource> resourceList = new ArrayList<Resource>();
		for (Resource resource : mResourceList) {
			if (resource.getParent_id() == currentId) {
				resourceList.add(resource);
			}
		}
		return resourceList;
	}
	
	/**
	 * @param currentId
	 * @param number 差值
	 */
	public void updateParentBean(Resource curResource, int number) {
		if (mKJDB == null) {
			mKJDB = KJDB.create();
		}
		mKJDB.update(curResource);
		
		Resource tmpResource = curResource;
		do {
			for (Resource resource : mResourceList) {
				if (resource.getId() == tmpResource.getParent_id()) {
					resource.setNumber(resource.getNumber() + number);
					mKJDB.update(resource);
					tmpResource = resource;
					break;
				}
			}
		} while (tmpResource.getParent_id() != 0);
		
		Resource income = null;
		Resource expense = null;
		Resource invest = null;
		Resource total = null;
		for (Resource resource : mResourceList) {
			switch(resource.getId()) {
			case 3:
				income = resource;
				break;
			case 4:
				expense = resource;
				break;
			case 5:
				invest = resource;
				break;
			case 6:
				total = resource;
				break;
			}
		}
		Log.i("wzw", "total:" + total.getNumber());
		total.setNumber(income.getNumber() - expense.getNumber() + invest.getNumber());
		Log.i("wzw", "total:" + total.getNumber());
		mKJDB.update(total);
	}
	
	public Resource getCurParentBean() {
		return mParentBean;
	}
	
	private void initialData() {
		// 初始化一级目录
		getBean(1, "记账单", 1);
		
		getBean(2, "时间", 1);

		getBean(3, "收入", 1, true, 0);

		getBean(4, "支出", 1, true, 0);

		getBean(5, "投资", 1, true, 0);
		
		getBean(6, "盈亏", 1);
		
		getBean(7, "退出登录", 1);
		
		// 初始化二级目录
		getBean(8, "工资", 2, false, 3);
		getBean(9, "线上", 2, true, 4);
		getBean(10, "线下", 2, true, 4);
		
		// 初始化三级目录
		getBean(11, "淘宝", 3, false, 9);
		getBean(12, "京东", 3, false, 9);
		getBean(13, "苏果", 3, false, 10);
		
		mResourceList = KJDB.create().findAll(Resource.class);
	}
	
	private Resource getBean(int id, String name, int level) {
		Resource resource = new Resource();
//		resource.setId(id);
		resource.setName(name);
		resource.setNumber(-1);
		resource.setLevel(level);
		resource.setHas_child(false);
		
		KJDB.create().save(resource);
		return resource;
	}
	
	private Resource getBean(int id, String name, int level, boolean hasChild, int parentId) {
		Resource resource = new Resource();
//		resource.setId(id);
		resource.setName(name);
		resource.setNumber(0);
		resource.setLevel(level);
		resource.setHas_child(hasChild);
		resource.setParent_id(parentId);
		
		KJDB.create().save(resource);
		return resource;
	}

}
