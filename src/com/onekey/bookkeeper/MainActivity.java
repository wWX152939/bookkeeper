package com.onekey.bookkeeper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onekey.bookkeeper.CommonView.ViewInterface;
import com.onekey.bookkeeper.entity.Resource;

public class MainActivity extends KJActivity {

	public final static String KEY_DIR_ID = "key_dir_id";
	
	/**
	 * View
	 */
	private CommonView mCommonView;
	
	private Button mAddView;
	private Button mSaveView;
	private TextView mTitleView;
	private ImageButton mBackView;
	
	/**
	 * Data
	 */
	private List<Resource> mResourceList = new ArrayList<Resource>();
	private int mDirId;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Bundle bundle = getIntent().getExtras();
    	mDirId = (Integer) bundle.get(KEY_DIR_ID);
        super.onCreate(savedInstanceState);
        initActionBar();
    }
    
    private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
		        ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.activity_actionbar);
		actionBar.getCustomView().findViewById(
		        R.id.actionbar_layout);
		mAddView = (Button) actionBar.getCustomView().findViewById(R.id.add);
		mSaveView = (Button) actionBar.getCustomView().findViewById(R.id.save);
		mTitleView = (TextView) actionBar.getCustomView().findViewById(
		        R.id.actionbar_title);
		mTitleView.setText("收支管理");
		mBackView = (ImageButton) actionBar.getCustomView().findViewById(
		        R.id.back);
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.add:
					add();
            		break;
				case R.id.save:
					save();
            		break;
				case R.id.back:
					onBackPressed();
					break;
				}
			}
		};
		mAddView.setOnClickListener(listener);
		mSaveView.setOnClickListener(listener);
		mBackView.setOnClickListener(listener);
	}
    
    /**
     * 添加弹出界面初始化
     */
    private void add() {
    	AlertDialog.Builder addPopup = new AlertDialog.Builder(this);
    	View dialogView = getLayoutInflater().inflate(R.layout.main_dialog, null);
		addPopup.setView(dialogView);
		addPopup.setCancelable(false);
		final AlertDialog dialog = addPopup.create();
		dialog.show();
		final EditText et = (EditText) dialogView.findViewById(R.id.et);
		final CheckBox cb = (CheckBox) dialogView.findViewById(R.id.cb);
		Button btn = (Button) dialogView.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!et.getText().toString().isEmpty()) {
					Resource resource = new Resource();
					resource.setName(et.getText().toString());
					if (cb.isChecked()) {
						resource.setHas_child(1);
					} else {
						resource.setHas_child(0);
					}
					Resource parent = DataProcessCenter.create().getCurParentBean();
					resource.setLevel(parent.getLevel() + 1);
					resource.setParent_id(parent.getId());
					resource.setTime(new Date(System.currentTimeMillis()));
					mResourceList.add(resource);
					mCommonView.setResourceList(mResourceList);
					DataProcessCenter.create().add(resource, mDirId);
					dialog.dismiss();
					Toast.makeText(getBaseContext(), "添加成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
		ImageView iv = (ImageView) dialogView.findViewById(R.id.btn_close);
		
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
    }
    
    /**
     * actionBar保存按钮初始化
     */
    private void save() {
    	boolean changeFlag = false;
    	for (int i = 0; i < mResourceList.size(); i++) {
    		if (0 == mResourceList.get(i).getHas_child() && mResourceList.get(i).getParent_id() != 0) {
    			TextView tv = (TextView) mCommonView.findViewById(CommonView.BASE_TEXT_VIEW_2_ID + i);
    			if (EditText.class.isInstance(tv)) {
    				String number = (String) tv.getText().toString();
    				if (!number.isEmpty()) {
    					int differenceNumber = Integer.parseInt(number) - mResourceList.get(i).getNumber();
        				mResourceList.get(i).setNumber(Integer.parseInt(number));
        				mResourceList.get(i).setTime(new Date(System.currentTimeMillis()));
        				DataProcessCenter.create().update(mResourceList.get(i), differenceNumber);
        				changeFlag = true;
    				}
    			}
    		}
    	}
    	mSaveView.setVisibility(View.GONE);
    	mAddView.setVisibility(View.VISIBLE);
    	if (changeFlag) {
    		Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
    	}
    }

	@Override
	public void setRootView() {
		mCommonView = new CommonView(this);
		mResourceList = DataProcessCenter.create().getChildList(null, mDirId);
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
	
    /**
     * 有子的情况下，点击响应事件实现
     * @param v
     */
    public void onLClick(View v) {
        for (int i = 0; i < mResourceList.size(); i++) {
        	if (mResourceList.get(i).getHas_child() == 1) {
        		if (mResourceList.get(i).getId() == (Integer)v.getTag()) {
            		mResourceList = DataProcessCenter.create().getChildList(mResourceList.get(i), mDirId);

            		ResourceStack.create().add(mResourceList);
            		mCommonView.setResourceList(mResourceList);
            	}
        	}
        }
    }
	
	@Override
	public void onBackPressed() {
		mResourceList = ResourceStack.create().top();
		if (mResourceList != null && !mResourceList.isEmpty()) {
			if (mResourceList.get(0).getLevel() != 1) {
				ResourceStack.create().removeTop();
				mResourceList = ResourceStack.create().top();
				String ids = "";
				for (Resource resource : mResourceList) {
					ids += resource.getId() + ",";
				}
				if (ids.endsWith(",")) {
					ids = ids.substring(0, ids.length() - 1);
				}
				mResourceList = DataProcessCenter.create().refresh(ids);
				mCommonView.setResourceList(mResourceList);
			} else {
				finish();
			}
		} else {
			ResourceStack.create().removeTop();
			mResourceList = ResourceStack.create().top();
			if (mResourceList != null) {
				String ids = "";
				for (Resource resource : mResourceList) {
					ids += resource.getId() + ",";
				}
				if (ids.endsWith(",")) {
					ids = ids.substring(0, ids.length() - 1);
				}
				mResourceList = DataProcessCenter.create().refresh(ids);
				mCommonView.setResourceList(mResourceList);
			} else {
				finish();
			}
			
		}
		
	}
	
}
