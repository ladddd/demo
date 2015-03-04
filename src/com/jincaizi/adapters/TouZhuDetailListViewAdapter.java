package com.jincaizi.adapters;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.R;

/**
 * 投注详情适配器
 *
 * @author yj
 */
public class TouZhuDetailListViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<String> mRedBall;
    private final LinkedList<String>mBlueBall;
    private final LinkedList<Integer> mCount;
    private boolean isSSQ = true;

    public boolean isSSQ() {
		return isSSQ;
	}

	public void setSSQ(boolean isSSQ) {
		this.isSSQ = isSSQ;
	}

	public TouZhuDetailListViewAdapter(Context context,LinkedList<String> redBall,LinkedList<String> blueBall, LinkedList<Integer> count) {
        this.mContext = context;
        this.mRedBall = redBall;
        this.mBlueBall = blueBall;
        this.mCount = count;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRedBall.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mRedBall.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.touzhu_detail_listview_item, null, false);
            holder.tv_redball = (TextView) convertView.findViewById(R.id.tv_red_ball_number);
            holder.tv_blueball = (TextView)convertView.findViewById(R.id.tv_blue_ball_number);
            holder.tv_type_count = (TextView)convertView.findViewById(R.id.tv_type_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(isSSQ) {
        	convertView.findViewById(R.id.tv_title_redball).setVisibility(View.VISIBLE);
        } else {
        	convertView.findViewById(R.id.tv_title_redball).setVisibility(View.GONE);
        }
        holder.tv_redball.setText(mRedBall.get(position));
        if(mBlueBall == null || mBlueBall.size() == 0) {
        	convertView.findViewById(R.id.lv_blueball_bet).setVisibility(View.GONE);
        } else {
        holder.tv_blueball.setText(mBlueBall.get(position));
        }
        if(Integer.valueOf(mCount.get(position)) <= 1) {
         holder.tv_type_count.setText("单式投注   " + mCount.get(position) + " 注  " + mCount.get(position)*2 + " 元");
        } else {
         holder.tv_type_count.setText("复式投注   " + mCount.get(position) + " 注  " + mCount.get(position)*2 + " 元");
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_redball;
        TextView tv_blueball;
        TextView tv_type_count;
        
    }
}
