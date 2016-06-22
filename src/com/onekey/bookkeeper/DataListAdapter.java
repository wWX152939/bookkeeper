package com.onekey.bookkeeper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class DataListAdapter<T> extends BaseAdapter {
    private Context mContext;
    // 重要的接口，用于adapter向adapterview主体类传递数据
    private ListAdapterInterface<T> mManager;
    // 保存所有的对象
    private List<T> mDataAllList = new ArrayList<T>();
    // 保存要显示对象
    private List<T> mDataShowList = new ArrayList<T>();
    // 保存选中的列表项索引
    private List<Integer> mSelectedPositionList = new ArrayList<Integer>();
    // 是否设置每行的背景色为 灰白相间, 默认为相间
    private boolean mIsInterphase = true;


    /**
     * 构造函数
     * @param context
     * @param isScroll
     * @param manager
     * @param datalist
     * @param resourseId
     */
    public DataListAdapter(Context context, ListAdapterInterface<T> manager, List<T> datalist) {
        mContext = context;
        mManager = manager;
        if (datalist != null) {
        	mDataAllList = datalist;
        }
        
    }

    /**
     * 子线程通知最线程刷新界面的处理函数
     */
    public Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };

    /**
     * 清除数据列表所有数据
     */
    public void clearAll() {
        mDataAllList.clear();
        mDataShowList.clear();
        clearSelection();
        mHander.sendEmptyMessage(0);
    }

    /**
     * 设置数据列表
     * 
     * @param List
     */
    public void setDataList(List<T> list) {
        mDataAllList.clear();
        mDataShowList.clear();
        clearSelection();
        addDataList(list);
    }
    
    /**
     * 设置数据显示列表
     * @param list
     */
    public void setShowDataList(List<T> list) {
    	mDataShowList.clear();
    	if (list != null) {
    		mDataShowList.addAll(list);
    	}
    	clearSelection();
    	mHander.sendEmptyMessage(0);
    }
    
    /**
     * 添加数据列表，不带清除的添加
     * 
     * @param List
     */
    public void addShowDataList(List<T> list) {
        if (list != null) {
        	mDataShowList.addAll(list);
            mHander.sendEmptyMessage(0);
        }
    }
    
    /**
     * 添加数据，不带清除的添加
     * 
     * @param t
     */
    public void addShowData(T t) {
        if (t != null) {
        	mDataShowList.add(t);
            mHander.sendEmptyMessage(0);
        }
    }

    /**
     * 添加数据列表，不带清除的添加
     * 
     * @param List
     */
    public void addDataList(List<T> list) {
        if (list != null) {
            mDataAllList.addAll(list);
            mHander.sendEmptyMessage(0);
        }
    }

    /**
     * 添加数据到数据列表
     * 
     * @param t
     */
    public void addDataToList(T t) {
        mDataAllList.add(t);
        mDataShowList.add(t);
        mHander.sendEmptyMessage(0);
    }

    /**
     * 删除指定的数据对象
     * @param t
     */
    public void deleteData(T t) {
        for (int i = 0; i < mDataAllList.size(); i++) {
            if (mManager.isSameObject(mDataAllList.get(i), t)) {
                mDataAllList.remove(i);
                break;
            }
        }

        for (int i = 0; i < mDataShowList.size(); i++) {
            if (mManager.isSameObject(mDataShowList.get(i), t)) {
                mDataShowList.remove(i);
                break;
            }
        }

        mHander.sendEmptyMessage(0);
    }
    
    /**
     * 删除指定行数据对象
     * @param t
     */
    public void deleteData(int mLine) {
    	mDataShowList.remove(mLine);
        mHander.sendEmptyMessage(0);
    }

    /**
     * 删除列表中的数据对象
     * 
     * @param list
     */
    public void deleteData(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            deleteData(t);
        }
        
        mHander.sendEmptyMessage(0);
    }

    /**
     * 更新数据对象
     * 
     * @param t
     */
    public void updateData(T t) {
        for (int i = 0; i < mDataAllList.size(); i++) {
            if (mManager.isSameObject(mDataAllList.get(i), t)) {
                mDataAllList.set(i, t);
                break;
            }
        }
        
        for (int i = 0; i < mDataShowList.size(); i++) {
            if (mManager.isSameObject(mDataShowList.get(i), t)) {
                mDataShowList.set(i, t);
                break;
            }
        }
        mHander.sendEmptyMessage(0);
    }
    

    /**
     * 更新指定行数据对象
     * 
     * @param t
     */
    public void updateData(int location, T t) {
    	mDataShowList.set(location, t);
        mHander.sendEmptyMessage(0);
    }
    
    /**
     * 显示查询的结果
     */
    public void showSearchResult(Object... condition) {
		List<T> list = (List<T>) mManager.findByCondition(condition);
    	if (list != null) {
    		mDataShowList.clear();
    		mDataShowList.addAll(list);
    	}
    	mHander.sendEmptyMessage(0);
    }

    /**
     * 获取所有数据对象列表
     * 
     * @return
     */
    public final List<T> getDataList() {
        return mDataAllList;
    }
    
    /**
     * 获取数据显示列表
     * @return
     */
    public final List<T> getDataShowList() {
    	return mDataShowList;
    }

    /**
     * 要显示的列表项数量
     */
    @Override
    public int getCount() {
        return mDataShowList.size();
    }

    /**
     * 返回指定索引的数据对象
     */
    @Override
    public T getItem(int position) {
        return mDataShowList.get(position);
    }

    /**
     * 获取指定的索引
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 显示列表项视图的主体函数
     */
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
    	// 用来设置Tag，以便循环利用已经隐藏的列表项视图对象
        ViewHolder holder;

        if (convertView != null) {
        	// 循环利用隐藏的列表项
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
            				.inflate(mManager.getLayoutId(), parent, false);
            
            mManager.initLayout(convertView, holder);
            convertView.setTag(holder);
        }

        // 初始化列表项视图显示内容
        mManager.initListViewItem(convertView, holder, this, position);
        
        // AdapterView设置列表项的监听
        mManager.regesterListeners(holder, position);
        
        if (mSelectedPositionList.contains((Integer) position)) {
            convertView.setBackgroundResource(R.color.listview_selected_bg); 
        } else {
            if (!mIsInterphase) {
                convertView.setBackgroundResource(R.color.tree_listview_bg);
            } else {
                if (position % 2 == 1) {
                    convertView.setBackgroundResource(R.color.content_listview_single_line_bg);
                } else {
                    convertView.setBackgroundColor(Color.WHITE);
                }
            }
        }
     
        return convertView;
    }
	
	/**
	 * 是否设置每行的背景色为 灰白相间
	 * @param interphase
	 */
	public void setConvertViewBackground(boolean interphase) {
	    mIsInterphase = interphase;
	}

    /**
     * 单选
     * 
     * @param position
     * @param isSeleced
     */
    public void setSelected(int position, boolean isSeleced) {
        mSelectedPositionList.clear();
        if (isSeleced) {
            mSelectedPositionList.add((Integer) position);
        }
        notifyDataSetChanged();
    }

    /**
     * 多选
     * 
     * @param position
     */
    public void setPickSelected(int position) {
        boolean isSeleced = false;
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            if (position == mSelectedPositionList.get(i)) {
                mSelectedPositionList.remove(i);
                isSeleced = true;
            }
        }
        if (!isSeleced) {
            mSelectedPositionList.add((Integer) position);
        }
        notifyDataSetChanged();
    }
    
    /**
     * 判断选择列表中是否包含指定位置
     * @param position
     * @return
     */
    public boolean isContainPosition(int position) {
    	for (Integer temp : mSelectedPositionList) {
    		if (position == temp) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * 获取已选择位置列表
     * 
     * @return
     */
    public final List<Integer> getSelectedList() {
        return mSelectedPositionList;
    }

    /**
     * 获取已选择的文件列表
     * 
     * @return
     */
    public final List<T> getSelectedDatas() {
        List<T> selectedDatas = new ArrayList<T>();
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            selectedDatas.add(getItem(mSelectedPositionList.get(i)));
        }
        return selectedDatas;
    }

    /**
     * 选中所有文件
     */
    public void setSelectedAll() {
        mSelectedPositionList.clear();
        for (int i = 0; i < getCount(); i++) {
            mSelectedPositionList.add(i);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除所有选择项
     */
    public void clearSelection() {
        mSelectedPositionList.clear();
    }
    
    public interface TickListAdapterInterface<T> extends ListAdapterInterface<T> {
    	int[] getEditTextNums();
    	boolean isDataValid();
    }

    /**
     * 文件列表适配器接口，需要在fragment中实现
     * 
     * @author yuanlu
     * 
     */
    public interface ListAdapterInterface<T> {
    	// 获取列表布局资源ID
        int getLayoutId();

        // 获取列表头，用于添加到左右滑动组件中
        View getHeaderView();

        // 注册组件的监听器
        void regesterListeners(ViewHolder viewHolder, final int position);
        
        // 初始化列表项视图
        void initListViewItem(View convertView, 
        						ViewHolder holder,
        						DataListAdapter<T> adapter,
        						int position);
        
        // 布局初始化
        void initLayout(View convertView, ViewHolder holder);
        
        // 查询接口
        List<T> findByCondition(Object... condition);
        
        // 判断两个对象是否是同一个对象，实际上要根据对象里面的ID来比较，而不是真正意义上的完全相同的对象
        boolean isSameObject(T t1, T t2);
    }

    public static class ViewHolder {
    	public View root;
        public TextView[] tvs;
        public ImageView[] ivs;
        public EditText[] ets;
        public Button[] bs;
        public CheckBox[] cbs;
        public AutoCompleteTextView[] actvs;
        public DatePickText[] dpts;
        public Spinner sp;
    }
}
