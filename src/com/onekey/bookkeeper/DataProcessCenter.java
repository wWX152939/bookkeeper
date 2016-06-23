package com.onekey.bookkeeper;

import java.util.List;

import org.kymjs.kjframe.KJDB;

import com.onekey.bookkeeper.entity.Resource;


/**
 * @author onekey
 * 
 */
public class DataProcessCenter {

	public static List<Resource> mResourceList;
    private static KJDB mKJDB;
	
	public static List<Resource> create() {
		if (mResourceList == null) {
			mResourceList = KJDB.create().findAll(Resource.class);
			if (mResourceList.isEmpty()) {
				initialData();
			}
		}
		return mResourceList;
	}
	
	private static void initialData() {
		// ��ʼ��һ��Ŀ¼
		Resource level1 = getBean("���˵�", -1);
		
		Resource level2 = getBean("ʱ��", -1);

		Resource level3 = getBean("����", 0);

		Resource level4 = getBean("֧��", 0);

		Resource level5 = getBean("Ͷ��", 0);
		
		Resource level6 = getBean("ӯ��", -1);
		
		Resource level7 = getBean("�˳���¼", -1);
		
	}
	
	private static Resource getBean(String name, int number) {
		Resource level = new Resource();
		level.setName("���˵�");
		level.setNumber(-1);
		return level;
	}
	
	private static Resource getBean(String name, int number, int isChild) {
		Resource level = new Resource();
		level.setName("���˵�");
		level.setNumber(-1);
		return level;
	}

}
