package com.onekey.bookkeeper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.onekey.bookkeeper.entity.DateDir;

public class LaunchActivity extends KJActivity {

	private LinearLayout mParentLayout;
	private Button mAddView;
	private TextView mTitleView;
	private int mYear;
	private int mMonth;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }
    
    private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
		        ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.launch_actionbar);
		actionBar.getCustomView().findViewById(
		        R.id.actionbar_layout);
		mAddView = (Button) actionBar.getCustomView().findViewById(R.id.add);
		mTitleView = (TextView) actionBar.getCustomView().findViewById(
		        R.id.actionbar_title);
		mTitleView.setText("收支管理");
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.add:
					add();
            		break;
				}
			}
		};
		mAddView.setOnClickListener(listener);
	}
    
    private void add() {
    	AlertDialog.Builder addPopup = new AlertDialog.Builder(this);
    	View dialogView = getLayoutInflater().inflate(R.layout.launch_dialog, null);
		addPopup.setView(dialogView);
		addPopup.setCancelable(false);
		final AlertDialog dialog = addPopup.create();
		dialog.show();
		final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
		
		((ViewGroup)((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE); 
		
		Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        mYear = year;
        int monthOfYear=calendar.get(Calendar.MONTH);
        mMonth = monthOfYear + 1;
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
            	mYear = year;
            	mMonth = monthOfYear + 1;
            }
            
        });
		Button btn = (Button) dialogView.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

		        List<DateDir> dirList = DataProcessCenter.create().getDirList();
		        for (DateDir dir : dirList) {
		        	Date date = dir.getName();
		        	int year = date.getYear();
		        	int month = date.getMonth();
		        	if ((year == mYear) && (month + 1) == mMonth) {
		        		Toast.makeText(LaunchActivity.this, "不能创建相同日期目录", Toast.LENGTH_SHORT).show();
		        		return;
		        	}
		        }
				DateDir dateDir = new DateDir();
				@SuppressWarnings("deprecation")
				Date date = new Date(mYear, mMonth, 0);
				dateDir.setName(date);
				long id = DataProcessCenter.create().addDir(dateDir);
				dateDir.setId((int) id);
				TextView tv = new TextView(getBaseContext());
				tv.setTextSize(16);
				tv.setTextColor(Color.BLACK);
				String text = "日期:" + mYear + "年" + mMonth + "月";
				tv.setText(text);
				tv.setTag(dateDir);
				tv.setOnClickListener(listener);
				Log.i("wzw", "dateDir:" + dateDir);
				mParentLayout.addView(tv);
				dialog.dismiss();
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
    
    private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Bundle extras = new Bundle();
			DateDir dateDir = (DateDir) v.getTag();
			extras.putInt(MainActivity.KEY_DIR_ID, dateDir.getId());
			showActivity(LaunchActivity.this, MainActivity.class, extras);
		}
	};
    
	@Override
	public void setRootView() {
        setContentView(R.layout.launch);

        mParentLayout = (LinearLayout) findViewById(R.id.parent_layout);
        List<DateDir> dirList = DataProcessCenter.create().getDirList();
        for (DateDir dir : dirList) {
        	TextView tv = new TextView(getBaseContext());
			tv.setTextSize(16);
			tv.setTextColor(Color.BLACK);
			@SuppressWarnings("deprecation")
			int year = dir.getName().getYear();
			int month = dir.getName().getMonth() + 1;
			String text = "日期:" + year + "年" + month + "月";
			tv.setText(text);
			tv.setOnClickListener(listener);
			tv.setTag(dir);
			
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(8, 8, 8, 8);
			mParentLayout.addView(tv, params);
        }
	}
	
}
