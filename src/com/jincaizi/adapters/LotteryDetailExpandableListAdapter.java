package com.jincaizi.adapters;

import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jincaizi.R;

public class LotteryDetailExpandableListAdapter extends BaseExpandableListAdapter {
	
	//单元类
	class ExpandableListHolder {
		TextView child_content;
	} 
	
	//父单元
	class ExpandableGroupHolder {
		TextView title_content;
		TextView extraInfo;
	} 
	
	private LinkedList<Map<String, String>> groupData;//组显示
	private LinkedList<String[]> childData;//子列表
	
	private LayoutInflater mGroupInflater; //用于加载group的布局xml
	private LayoutInflater mChildInflater; //用于加载listitem的布局xml
	
	//自宝义构造
	public LotteryDetailExpandableListAdapter(Context context, LinkedList<Map<String, String>> groupData, LinkedList<String[]> childData) {
		this.childData=childData;
		this.groupData=groupData;
		
		mChildInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	//必须实现 得到子数据
	@Override
	public Object getChild(int groupPosition, int j) {
		return childData.get(groupPosition)[j];
	}

	@Override
	public long getChildId(int groupPosition, int j) {
		return groupPosition;
	}

	@Override
	public int getChildrenCount(int i) {
		return childData.get(i).length;
	}

	@Override
	public Object getGroup(int i) {
		return groupData.get(i);
	}

	@Override
	public int getGroupCount() {
		return groupData.size();
	}

	@Override
	public long getGroupId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {//行是否具有唯一id
		return false;
	}

	@Override
	public boolean isChildSelectable(int i, int j) {//行是否可选
		return false;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean flag, View convertView, ViewGroup viewgroup) {
		ExpandableGroupHolder holder = null; //清空临时变量holder
		if (convertView == null) { //判断view（即view是否已构建好）是否为空

			convertView = mGroupInflater.inflate(R.layout.detail_parent_item, null);
			holder = new ExpandableGroupHolder();
			holder.title_content=(TextView) convertView.findViewById(R.id.goumaiId_content);
			holder.extraInfo = (TextView)convertView.findViewById(R.id.extraInfo);
			convertView.setTag(holder);
		} else { //若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (ExpandableGroupHolder) convertView.getTag();
		}
		String title=(String)this.groupData.get(groupPosition).get("title");
		holder.title_content.setText(title);
		String extraStr = (String)this.groupData.get(groupPosition).get("extra");
		holder.extraInfo.setText(extraStr);
		//notifyDataSetChanged();
		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup viewgroup) {
		ExpandableListHolder holder = null;
		if (convertView == null) {
			convertView = mChildInflater.inflate(R.layout.detail_child_item, null);

			holder = new ExpandableListHolder();
			holder.child_content = (TextView) convertView.findViewById(R.id.child_content);
			convertView.setTag(holder);
		} else {//若行已初始化，直接从tag属性获得子视图的引用
			holder = (ExpandableListHolder) convertView.getTag();
		} 
		String str =this.childData.get(groupPosition)[childPosition];
		holder.child_content.setText(str);
		return convertView;
	}
}