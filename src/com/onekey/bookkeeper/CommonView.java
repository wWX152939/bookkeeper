package com.onekey.bookkeeper;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onekey.bookkeeper.entity.Resource;

/**
 * @author onekey
 * 
 */
public abstract class CommonView extends LinearLayout {

	public final int BASE_LINE_ID = 0x100;
	public final int BASE_TEXT_VIEW_1_ID = 0x200;
	public final int BASE_TEXT_VIEW_2_ID = 0x300;

	private List<Resource> mResourceList;
	private Context mContext;

	public CommonView(Context context) {
		this(context, null, 0);
	}

	public CommonView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mResourceList = getResourceList();
		initView();
	}

	public abstract List<Resource> getResourceList();

	/**
	 * 初始化界面
	 */
	@SuppressLint("InlinedApi")
	private void initView() {
		// 设置父布局
		setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(parentParams);
		setBackgroundColor(Color.WHITE);
		if (mResourceList == null) {
			return;
		}
		
		int size = mResourceList.size();
		for (int i = 0; i < size; i++) {
			LinearLayout line = new LinearLayout(mContext);
			line.setId(BASE_LINE_ID + i);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			layoutParams.topMargin = 10;
			layoutParams.bottomMargin = 10;
			line.setOrientation(LinearLayout.HORIZONTAL);
			line.setBackgroundColor(Color.WHITE);
			createSubWidget(line, i);
			addView(line, layoutParams);
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
		layoutParams1.weight = 3;
		tv1.setId(BASE_TEXT_VIEW_1_ID + i);
		tv1.setText(mResourceList.get(i).getName());
		initCommonTextView(tv1);
		parentView.addView(tv1, layoutParams1);
		
		TextView tv2 = new TextView(mContext);
		tv2.setId(BASE_TEXT_VIEW_2_ID + i);
		tv2.setText(mResourceList.get(i).getNumber());
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT);
		layoutParams1.weight = 1;
		initCommonTextView(tv2);
		parentView.addView(tv2, layoutParams2);
	}
	
	private void initCommonTextView(TextView tv) {
		tv.setTextColor(Color.BLACK);
	}

}
