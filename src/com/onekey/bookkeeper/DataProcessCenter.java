package com.onekey.bookkeeper;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJDB;

import android.util.Log;

import com.onekey.bookkeeper.entity.DateDir;
import com.onekey.bookkeeper.entity.Resource;


/**
 * @author onekey
 * 
 */
public class DataProcessCenter {
	
	public static DataProcessCenter instance;
	
	public KJDB mKJDB;
	
	// resource列表缓存，不同DateDir列表不一样
	private List<Resource> mResourceList;
	private List<DateDir> mDateDirList;
	
	private Resource mParentBean;
	
	public static DataProcessCenter create() {
		if (instance == null) {
			instance = new DataProcessCenter();
		}
		return instance;
	}
	
	/**
	 * 添加resource
	 * @param resource
	 * @param dirId
	 */
	public void add(Resource resource, int dirId) {
		KJDB.create().save(resource);
		mResourceList = sqlWhere(dirId);
	}
	
	/**
	 * 刷新列表，可能界面的数据过时，需要以逗号传入id，例1,2,3 获取新的数据
	 * @param ids
	 * @return
	 */
	public List<Resource> refresh(String ids) {
		return mKJDB.findAllByWhere(Resource.class, "id in (" + ids + ")");
	}
	
	/**
	 * 添加目录
	 * @param dateDir
	 * @return
	 */
	public long addDir(DateDir dateDir) {
		long id = KJDB.create().save(dateDir);
		mDateDirList = mKJDB.findAll(DateDir.class);
		return id;
	}
	
	/**
	 * 获取目录列表
	 * @return
	 */
	public List<DateDir> getDirList() {
		if (mKJDB == null) {
			mKJDB = KJDB.create();
		}
		if (mDateDirList == null) {
			mDateDirList = mKJDB.findAll(DateDir.class);
		}
		return mDateDirList;
	}
	
	/**
	 * 根据传入的当前对象和目录id，获取resource列表 当前对象会录入缓存
	 * @param currentBean
	 * @param dirId
	 * @return
	 */
	public List<Resource> getChildList(Resource currentBean, int dirId) {
		if (currentBean == null) {
			currentBean = new Resource();
		}
		mParentBean = currentBean;
		if (mKJDB == null) {
			mKJDB = KJDB.create();
		}

		mResourceList = sqlWhere(dirId);
		LogUtil.i("list:" + mResourceList.isEmpty() + " dirId:" + dirId);
		if (mResourceList.isEmpty()) {
			initialData(dirId);
		}
		LogUtil.i("list:" + mResourceList);
			
//		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.onekey.bookkeeper/databases/KJLibrary.db",null);
//		Cursor c = db.rawQuery("SELECT * FROM com_onekey_bookkeeper_entity_Resource", null); 
//		while (c.moveToNext()) { 
//		int _id = c.getInt(c.getColumnIndex("id"));
//		int dir_id = c.getInt(c.getColumnIndex("dir_id")); 
//		String age = c.getString(c.getColumnIndex("name")); 
//		Log.i("wzw", "_id=>" + _id + ", age=>" + age + ", dir=>" + dir_id); 
//		} 
//		c.close();
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
	 * 更新当前对象到数据库，同时会更新父对象以及关联对象
	 * @param currentId
	 * @param number 差值
	 */
	public void update(Resource curResource, int number) {
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
			switch(resource.getName()) {
			case "收入":
				income = resource;
				break;
			case "支出":
				expense = resource;
				break;
			case "投资":
				invest = resource;
				break;
			case "盈亏":
				total = resource;
				break;
			}
		}
		total.setNumber(income.getNumber() - expense.getNumber() + invest.getNumber());
		mKJDB.update(total);
	}
	
	/**
	 * 获取当前的父
	 * @return
	 */
	public Resource getCurParentBean() {
		return mParentBean;
	}
	
	private List<Resource> sqlWhere(int dirId) {
		return mKJDB.findAllByWhere(Resource.class, "dir_id in (" + "'" + dirId + "', " + dirId + ')');
	}
	
	/**
	 * 新建目录，初始化数据
	 * @param dirId
	 */
	private void initialData(int dirId) {
		// 初始化一级目录
		
		Resource resource1 = getBean(1, "收入", 1, true, 0, dirId);

		Resource resource2 = getBean(2, "支出", 1, true, 0, dirId);

		getBean(3, "投资", 1, true, 0, dirId);
		
		getBean(4, "盈亏", 1, dirId);
		
		Log.i("wzw", "resource1:" + resource1);
		// 初始化二级目录
		getBean(5, "工资", 2, false, resource1.getId(), dirId);
		Resource resource6 = getBean(6, "线上", 2, true, resource2.getId(), dirId);
		Resource resource7 = getBean(7, "线下", 2, true, resource2.getId(), dirId);
		
		// 初始化三级目录
		getBean(8, "淘宝", 3, false, resource6.getId(), dirId);
		getBean(9, "京东", 3, false, resource6.getId(), dirId);
		getBean(10, "苏果", 3, false, resource7.getId(), dirId);
		
		mResourceList = sqlWhere(dirId);
	}
	
	/**
	 * 创建resource对象
	 * @param id
	 * @param name
	 * @param level
	 * @param dirId
	 * @return
	 */
	private Resource getBean(int id, String name, int level, int dirId) {
		Resource resource = new Resource();
//		resource.setId(id);
		resource.setName(name);
		resource.setNumber(-1);
		resource.setLevel(level);
		resource.setHas_child(0);
		resource.setDir_id(dirId);
		
		long rId = KJDB.create().save(resource);
		resource.setId((int) rId);
		return resource;
	}
	
	/**创建resource对象
	 * @param id
	 * @param name
	 * @param level
	 * @param hasChild
	 * @param parentId
	 * @param dirId
	 * @return
	 */
	private Resource getBean(int id, String name, int level, boolean hasChild, int parentId, int dirId) {
		Resource resource = new Resource();
//		resource.setId(id);
		resource.setName(name);
		resource.setNumber(0);
		resource.setLevel(level);
		if (hasChild) {
			resource.setHas_child(1);
		} else {
			resource.setHas_child(0);
		}
		
		resource.setParent_id(parentId);
		resource.setDir_id(dirId);

		long rId = KJDB.create().save(resource);
		resource.setId((int) rId);
		return resource;
	}

}
