package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;


/**
 * 十一运夺金投注详情适配器
 * 
 * @author yj
 */
public class SyyTouZhuAdapter extends BaseAdapter {
	private final Context mContext;
	private final LinkedList<String> mBall;
	private final LinkedList<ShiyiyunType> mType;

	public SyyTouZhuAdapter(Context context, LinkedList<String> ball,
			LinkedList<ShiyiyunType> type) {
		this.mContext = context;
		this.mBall = ball;
		this.mType = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBall.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBall.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.pls_touzhu_item, null, false);
			holder.tv_ball = (TextView) convertView
					.findViewById(R.id.tv_pls_item);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_pls_type);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_ball.setText(mBall.get(position));
		if (mType.get(position) == Constants.ShiyiyunType.ANYTWO) {
			holder.tv_type.setText("[任二]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYTHREE) {
			holder.tv_type.setText("[任三]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFOUR) {
			holder.tv_type.setText("[任四]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFIVE) {
			holder.tv_type.setText("[任五]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSIX) {
			holder.tv_type.setText("[任六]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSEVEN) {
			holder.tv_type.setText("[任七]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYEIGHT) {
			holder.tv_type.setText("[任八]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTONEZHI) {
			holder.tv_type.setText("[前一直选]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTWOZHI) {
			holder.tv_type.setText("[前二直选]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTWOZU) {
			holder.tv_type.setText("[前二组选]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTHREEZHI) {
			holder.tv_type.setText("[前三直选]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTHREEZU) {
			holder.tv_type.setText("[前三组选]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYTWODRAG) {
			holder.tv_type.setText("[任二-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYTHREEDRAG) {
			holder.tv_type.setText("[任三-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFOURDRAG) {
			holder.tv_type.setText("[任四-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFIVEDRAG) {
			holder.tv_type.setText("[任五-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSIXDRAG) {
			holder.tv_type.setText("[任六-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSEVENDRAG) {
			holder.tv_type.setText("[任七-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYEIGHTDRAG) {
			holder.tv_type.setText("[任八-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTWOZUDRAG) {
			holder.tv_type.setText("[前二组选-胆拖]");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTHREEZUDRAG) {
			holder.tv_type.setText("[前三组选-胆拖]");
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_ball;
		TextView tv_type;

	}
}
