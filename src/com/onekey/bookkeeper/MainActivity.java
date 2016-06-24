package com.onekey.bookkeeper;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.onekey.bookkeeper.CommonView.ViewInterface;
import com.onekey.bookkeeper.entity.Resource;

public class MainActivity extends KJActivity {

	private List<Resource> mResourceList = new ArrayList<Resource>();
	private CommonView mCommonView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public void setRootView() {
		mCommonView = new CommonView(getBaseContext());
		mResourceList = DataProcessCenter.create().getChildList(0);
		mCommonView.setResourceList(mResourceList);
		mCommonView.setViewInterface(new ViewInterface() {
			
			@Override
			public void onLineClick(View v) {
				onLClick(v);
			}
		});
		ResourceStack.create().add(mResourceList);
        setContentView(mCommonView);
	}
	
    public void onLClick(View v) {
        for (int i = 0; i < mResourceList.size(); i++) {
        	if (mResourceList.get(i).isHas_child()) {
        		if (mResourceList.get(i).getId() == (Integer)v.getTag()) {
            		mResourceList = DataProcessCenter.create().getChildList(mResourceList.get(i).getId());

            		ResourceStack.create().add(mResourceList);
            		Log.i("wzw", "onClick");
            		mCommonView.setResourceList(mResourceList);
            	}
        	}
        }
    }
	
	@Override
	public void onBackPressed() {
		Log.i("wzw", "onBackPressed");
		ResourceStack.create().removeTop();
		mResourceList = ResourceStack.create().top();
		mCommonView.setResourceList(mResourceList);
	}
	
}
