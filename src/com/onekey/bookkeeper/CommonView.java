package com.onekey.bookkeeper;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onekey.bookkeeper.entity.Resource;

/**
 * @author onekey
 * 
 */
public class CommonView extends LinearLayout {

	public static final int BASE_LINE_ID = 0x100;
	public static final int BASE_TEXT_VIEW_1_ID = 0x200;
	public static final int BASE_TEXT_VIEW_2_ID = 0x300;

	private List<Resource> mResourceList;
	private Context mContext;
	private ViewInterface mViewInterface;

	public CommonView(Context context) {
		this(context, null, 0);
	}

	public CommonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}
	
	public void setViewInterface(ViewInterface viewInterface) {
		mViewInterface = viewInterface;
	}
	
	public void setResourceList(List<Resource> resourceList) {
		mResourceList = resourceList;
		initView();
	}
	
	/**
	 * 初始化界面
	 */
	@SuppressLint("InlinedApi")
	private void initView() {
		removeAllViews();
		// 设置父布局
		setOrientation(LinearLayout.VERTICAL);
		FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(parentParams);

		Log.i("wzw", "initView");
		setBackgroundColor(Color.WHITE);
		if (mResourceList == null) {
			return;
		}
		
		int size = mResourceList.size();
		for (int i = 0; i < size; i++) {
			if (mResourceList.get(i).getNumber() == -1) {
				LinearLayout line = new LinearLayout(mContext);
				line.setId(BASE_LINE_ID + i);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 5;
				layoutParams.rightMargin = 5;
				layoutParams.topMargin = 10;
				layoutParams.bottomMargin = 10;
				line.setOrientation(LinearLayout.HORIZONTAL);
				line.setBackgroundColor(Color.WHITE);
				
				TextView tv = new TextView(mContext);
				
				LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
				tv.setText(mResourceList.get(i).getName());
				initCommonTextView(tv);
				
				line.addView(tv, tvLayoutParams);
				addView(line, layoutParams);
			} else {
				if (mResourceList.get(i).isHas_child()) {
					LinearLayout line = new LinearLayout(mContext);
					line.setTag(mResourceList.get(i).getId());
					line.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mViewInterface.onLineClick(v);
						}
					});
					line.setId(BASE_LINE_ID + i);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 5;
					layoutParams.rightMargin = 5;
					layoutParams.topMargin = 10;
					layoutParams.bottomMargin = 10;
					line.setOrientation(LinearLayout.HORIZONTAL);
					line.setBackgroundColor(Color.WHITE);
					createSubWidget(line, i);
					addView(line, layoutParams);
				} else {
					LinearLayout line = new LinearLayout(mContext);
					line.setId(BASE_LINE_ID + i);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 5;
					layoutParams.rightMargin = 5;
					layoutParams.topMargin = 10;
					layoutParams.bottomMargin = 10;
					line.setOrientation(LinearLayout.HORIZONTAL);
					line.setBackgroundColor(Color.WHITE);
					createSubWidgetWithEditText(line, i);
					addView(line, layoutParams);
				}
				
			}
		}
	}

	
	/**
	 * @param parentView
	 * @param i
	 */
	private void createSubWidget(LinearLayout parentView, int i) {
		TextView tv1 = new TextView(mContext);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT);
		layoutParams1.weight = 1;
		tv1.setId(BASE_TEXT_VIEW_1_ID + i);
		tv1.setText(mResourceList.get(i).getName());
		initCommonTextView(tv1);
		parentView.addView(tv1, layoutParams1);
		
		TextView tv2 = new TextView(mContext);
		Log.i("wzw", "tv2：" + tv2 + " text:" + mContext + " res:" + mResourceList.get(i));
		tv2.setId(BASE_TEXT_VIEW_2_ID + i);
		if (mResourceList.get(i).getNumber() != 0) {
			tv2.setText(mResourceList.get(i).getNumber());
		}
		
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT);
		layoutParams1.weight = 1;
		initCommonTextView(tv2);
		parentView.addView(tv2, layoutParams2);
	}
	
	private void createSubWidgetWithEditText(LinearLayout parentView, int i) {
		TextView tv1 = new TextView(mContext);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT);
		layoutParams1.weight = 1;
		tv1.setId(BASE_TEXT_VIEW_1_ID + i);
		tv1.setText(mResourceList.get(i).getName());
		initCommonTextView(tv1);
		parentView.addView(tv1, layoutParams1);
		
		EditText tv2 = new EditText(mContext);
		tv2.setId(BASE_TEXT_VIEW_2_ID + i);
		if (mResourceList.get(i).getNumber() != 0) {
			tv2.setText(mResourceList.get(i).getNumber());
		}
		
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT);
		layoutParams1.weight = 1;
		initCommonTextView(tv2);
		parentView.addView(tv2, layoutParams2);
	}
	
	private void initCommonTextView(TextView tv) {
		tv.setTextColor(Color.BLACK);
	}
	
	public interface ViewInterface {
		public void onLineClick(View v);
	}

}
