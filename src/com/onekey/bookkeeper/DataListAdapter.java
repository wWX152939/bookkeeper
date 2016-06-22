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
    // ��Ҫ�Ľӿڣ�����adapter��adapterview�����ഫ������
    private ListAdapterInterface<T> mManager;
    // �������еĶ���
    private List<T> mDataAllList = new ArrayList<T>();
    // ����Ҫ��ʾ����
    private List<T> mDataShowList = new ArrayList<T>();
    // ����ѡ�е��б�������
    private List<Integer> mSelectedPositionList = new ArrayList<Integer>();
    // �Ƿ�����ÿ�еı���ɫΪ �Ұ����, Ĭ��Ϊ���
    private boolean mIsInterphase = true;


    /**
     * ���캯��
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
     * ���߳�֪ͨ���߳�ˢ�½���Ĵ�����
     */
    public Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };

    /**
     * ��������б���������
     */
    public void clearAll() {
        mDataAllList.clear();
        mDataShowList.clear();
        clearSelection();
        mHander.sendEmptyMessage(0);
    }

    /**
     * ���������б�
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
     * ����������ʾ�б�
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
     * ��������б�������������
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
     * ������ݣ�������������
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
     * ��������б�������������
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
     * ������ݵ������б�
     * 
     * @param t
     */
    public void addDataToList(T t) {
        mDataAllList.add(t);
        mDataShowList.add(t);
        mHander.sendEmptyMessage(0);
    }

    /**
     * ɾ��ָ�������ݶ���
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
     * ɾ��ָ�������ݶ���
     * @param t
     */
    public void deleteData(int mLine) {
    	mDataShowList.remove(mLine);
        mHander.sendEmptyMessage(0);
    }

    /**
     * ɾ���б��е����ݶ���
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
     * �������ݶ���
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
     * ����ָ�������ݶ���
     * 
     * @param t
     */
    public void updateData(int location, T t) {
    	mDataShowList.set(location, t);
        mHander.sendEmptyMessage(0);
    }
    
    /**
     * ��ʾ��ѯ�Ľ��
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
     * ��ȡ�������ݶ����б�
     * 
     * @return
     */
    public final List<T> getDataList() {
        return mDataAllList;
    }
    
    /**
     * ��ȡ������ʾ�б�
     * @return
     */
    public final List<T> getDataShowList() {
    	return mDataShowList;
    }

    /**
     * Ҫ��ʾ���б�������
     */
    @Override
    public int getCount() {
        return mDataShowList.size();
    }

    /**
     * ����ָ�����������ݶ���
     */
    @Override
    public T getItem(int position) {
        return mDataShowList.get(position);
    }

    /**
     * ��ȡָ��������
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ��ʾ�б�����ͼ�����庯��
     */
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
    	// ��������Tag���Ա�ѭ�������Ѿ����ص��б�����ͼ����
        ViewHolder holder;

        if (convertView != null) {
        	// ѭ���������ص��б���
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
            				.inflate(mManager.getLayoutId(), parent, false);
            
            mManager.initLayout(convertView, holder);
            convertView.setTag(holder);
        }

        // ��ʼ���б�����ͼ��ʾ����
        mManager.initListViewItem(convertView, holder, this, position);
        
        // AdapterView�����б���ļ���
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
	 * �Ƿ�����ÿ�еı���ɫΪ �Ұ����
	 * @param interphase
	 */
	public void setConvertViewBackground(boolean interphase) {
	    mIsInterphase = interphase;
	}

    /**
     * ��ѡ
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
     * ��ѡ
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
     * �ж�ѡ���б����Ƿ����ָ��λ��
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
     * ��ȡ��ѡ��λ���б�
     * 
     * @return
     */
    public final List<Integer> getSelectedList() {
        return mSelectedPositionList;
    }

    /**
     * ��ȡ��ѡ����ļ��б�
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
     * ѡ�������ļ�
     */
    public void setSelectedAll() {
        mSelectedPositionList.clear();
        for (int i = 0; i < getCount(); i++) {
            mSelectedPositionList.add(i);
        }
        notifyDataSetChanged();
    }

    /**
     * �������ѡ����
     */
    public void clearSelection() {
        mSelectedPositionList.clear();
    }
    
    public interface TickListAdapterInterface<T> extends ListAdapterInterface<T> {
    	int[] getEditTextNums();
    	boolean isDataValid();
    }

    /**
     * �ļ��б��������ӿڣ���Ҫ��fragment��ʵ��
     * 
     * @author yuanlu
     * 
     */
    public interface ListAdapterInterface<T> {
    	// ��ȡ�б�����ԴID
        int getLayoutId();

        // ��ȡ�б�ͷ��������ӵ����һ��������
        View getHeaderView();

        // ע������ļ�����
        void regesterListeners(ViewHolder viewHolder, final int position);
        
        // ��ʼ���б�����ͼ
        void initListViewItem(View convertView, 
        						ViewHolder holder,
        						DataListAdapter<T> adapter,
        						int position);
        
        // ���ֳ�ʼ��
        void initLayout(View convertView, ViewHolder holder);
        
        // ��ѯ�ӿ�
        List<T> findByCondition(Object... condition);
        
        // �ж����������Ƿ���ͬһ������ʵ����Ҫ���ݶ��������ID���Ƚϣ����������������ϵ���ȫ��ͬ�Ķ���
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
