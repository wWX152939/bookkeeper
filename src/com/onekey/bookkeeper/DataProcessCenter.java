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
	
	// resource�б��棬��ͬDateDir�б�һ��
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
	 * ���resource
	 * @param resource
	 * @param dirId
	 */
	public void add(Resource resource, int dirId) {
		KJDB.create().save(resource);
		mResourceList = sqlWhere(dirId);
	}
	
	/**
	 * ˢ���б����ܽ�������ݹ�ʱ����Ҫ�Զ��Ŵ���id����1,2,3 ��ȡ�µ�����
	 * @param ids
	 * @return
	 */
	public List<Resource> refresh(String ids) {
		return mKJDB.findAllByWhere(Resource.class, "id in (" + ids + ")");
	}
	
	/**
	 * ���Ŀ¼
	 * @param dateDir
	 * @return
	 */
	public long addDir(DateDir dateDir) {
		long id = KJDB.create().save(dateDir);
		mDateDirList = mKJDB.findAll(DateDir.class);
		return id;
	}
	
	/**
	 * ��ȡĿ¼�б�
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
	 * ���ݴ���ĵ�ǰ�����Ŀ¼id����ȡresource�б� ��ǰ�����¼�뻺��
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
	 * ���µ�ǰ�������ݿ⣬ͬʱ����¸������Լ���������
	 * @param currentId
	 * @param number ��ֵ
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
			case "����":
				income = resource;
				break;
			case "֧��":
				expense = resource;
				break;
			case "Ͷ��":
				invest = resource;
				break;
			case "ӯ��":
				total = resource;
				break;
			}
		}
		total.setNumber(income.getNumber() - expense.getNumber() + invest.getNumber());
		mKJDB.update(total);
	}
	
	/**
	 * ��ȡ��ǰ�ĸ�
	 * @return
	 */
	public Resource getCurParentBean() {
		return mParentBean;
	}
	
	private List<Resource> sqlWhere(int dirId) {
		return mKJDB.findAllByWhere(Resource.class, "dir_id in (" + "'" + dirId + "', " + dirId + ')');
	}
	
	/**
	 * �½�Ŀ¼����ʼ������
	 * @param dirId
	 */
	private void initialData(int dirId) {
		// ��ʼ��һ��Ŀ¼
		
		Resource resource1 = getBean(1, "����", 1, true, 0, dirId);

		Resource resource2 = getBean(2, "֧��", 1, true, 0, dirId);

		getBean(3, "Ͷ��", 1, true, 0, dirId);
		
		getBean(4, "ӯ��", 1, dirId);
		
		Log.i("wzw", "resource1:" + resource1);
		// ��ʼ������Ŀ¼
		getBean(5, "����", 2, false, resource1.getId(), dirId);
		Resource resource6 = getBean(6, "����", 2, true, resource2.getId(), dirId);
		Resource resource7 = getBean(7, "����", 2, true, resource2.getId(), dirId);
		
		// ��ʼ������Ŀ¼
		getBean(8, "�Ա�", 3, false, resource6.getId(), dirId);
		getBean(9, "����", 3, false, resource6.getId(), dirId);
		getBean(10, "�չ�", 3, false, resource7.getId(), dirId);
		
		mResourceList = sqlWhere(dirId);
	}
	
	/**
	 * ����resource����
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
	
	/**����resource����
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
