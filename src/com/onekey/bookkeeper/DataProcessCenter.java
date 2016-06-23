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
		// 初始化一级目录
		Resource level1 = getBean("记账单", -1);
		
		Resource level2 = getBean("时间", -1);

		Resource level3 = getBean("收入", 0);

		Resource level4 = getBean("支出", 0);

		Resource level5 = getBean("投资", 0);
		
		Resource level6 = getBean("盈亏", -1);
		
		Resource level7 = getBean("退出登录", -1);
		
	}
	
	private static Resource getBean(String name, int number) {
		Resource level = new Resource();
		level.setName("记账单");
		level.setNumber(-1);
		return level;
	}
	
	private static Resource getBean(String name, int number, int isChild) {
		Resource level = new Resource();
		level.setName("记账单");
		level.setNumber(-1);
		return level;
	}

}
